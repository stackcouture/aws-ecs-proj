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

        stage('Trivy Scan (Pre-Push)') {
            steps {
                script {
                    scanDockerImage("${env.IMAGE_NAME}:latest")
                }
            }
        }

        stage('Push Docker Image to AWS ECR') {
            when {
                expression {
                    currentBuild.currentResult == 'SUCCESS'
                }
            }
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

        stage('Scan Docker Image with Snyk') {
            when {
                expression {
                    currentBuild.currentResult == 'SUCCESS'
                }
            }
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
                    terraformProvisionPlan(env.TF_DIR, env.AWS_CREDENTIALS_ID, params.AWS_DEFAULT_REGION)
                }
            }
        }

        stage('Terraform Apply') {
            when {
                expression { env.TF_PLAN_EXIT_CODE == '2' && fileExists("${env.TF_DIR}/tfplan") }
            }
            steps {
                script {
                    input message: 'Approve Terraform Apply?', ok: 'Apply'
                    terraformApply(env.TF_DIR, env.AWS_CREDENTIALS_ID)
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

        stage('Post-Deployment Validation') {
            steps {
                script {
                    echo "Deployment completed. You can optionally add ECS service/task validation here."
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