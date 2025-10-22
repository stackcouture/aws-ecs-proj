@Library('my-shared-lib') _

pipeline {
    agent any

    environment {
        IMAGE_NAME        = 'diwali-wishes'
        IMAGE_TAG          = "1.0.${BUILD_NUMBER}"
        GIT_URL            = "https://github.com/stackcouture/aws-ecs-proj.git"
        TF_DIR             = 'ecs-terraform'
        AWS_CREDENTIALS_ID = 'aws-credentials-id'
    }

    parameters {
        string(name: 'AWS_ACCOUNT_ID', defaultValue: '104824081961', description: 'Enter AWS Account ID')
        string(name: 'AWS_DEFAULT_REGION', defaultValue: 'ap-south-1', description: 'Enter region')
        string(name: 'BRANCH', defaultValue: 'main', description: 'Deployment branch for CD repo')
    }

    stages {

        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Git Checkout') {
            steps {
                    checkoutGit(params.BRANCH, env.GIT_URL, 'github-cred')
            }
        }

        stage('Authenticate with AWS and ECR') {
            steps {
                script {
                    authenticateAWS(params.AWS_DEFAULT_REGION, params.AWS_ACCOUNT_ID, env.AWS_CREDENTIALS_ID)
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                 script {
                    buildDockerImage(env.IMAGE_NAME)
                }
            }
        }

        stage('Trivy Scan') {
            steps {
                script {
                    def criticalCount = scanDockerImage("${env.IMAGE_NAME}:latest")
                    if (criticalCount > 0) {
                        error("Critical vulnerabilities detected: ${criticalCount}. Build stopped.")
                    }
                }
            }
        }

        stage('Push Docker Image to AWS ECR') {
            steps {
                script {
                    env.ECR_URI = pushImageECR(
                        env.IMAGE_NAME,
                        env.IMAGE_TAG,
                        params.AWS_ACCOUNT_ID,
                        params.AWS_DEFAULT_REGION
                    )
                }
            }
        }

        stage('Snyk Container Scan') {
            steps {
                script {
                    snykDockerScan(
                        ecrUri: env.ECR_URI,
                        imageTag: env.IMAGE_TAG,
                        credentialsId: 'SNYK_TOKEN'
                    )
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                script {
                    def planExitCode = terraformProvisionPlan(env.TF_DIR, env.AWS_CREDENTIALS_ID, params.AWS_DEFAULT_REGION)
                    echo "Terraform plan exit code: ${planExitCode}"
                }
            }
        }

        stage('Terraform Apply') {
            steps {
                script {
                    // Apply only if changes exist (exit code 2) and plan file exists
                    def planExitCode = terraformProvisionPlan(env.TF_DIR, env.AWS_CREDENTIALS_ID, params.AWS_DEFAULT_REGION)
                    if (planExitCode == 2 && fileExists("${env.TF_DIR}/tfplan")) {
                        input message: 'Terraform plan shows changes. Approve Apply?', ok: 'Apply'
                        terraformApply(env.TF_DIR, env.AWS_CREDENTIALS_ID)
                    } else {
                        echo "No changes detected. Skipping Terraform apply."
                    }
                }
            }
        }

        stage('Deploy to ECS') {
            steps {
                script {
                    deployToECS(env.TF_DIR, env.AWS_CREDENTIALS_ID, params.AWS_DEFAULT_REGION, env.ECR_URI, env.IMAGE_TAG)
                }
            }
        }
    }   

    post {
        always {
            echo "Build completed with result: ${currentBuild.currentResult}"
        }
    }
}