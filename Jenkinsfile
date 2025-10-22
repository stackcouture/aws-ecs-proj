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
                    // Scan Docker image
                    def scanResults = scanDockerImage("${env.IMAGE_NAME}:latest")

                    // Report CRITICALs clearly, but do NOT stop the pipeline
                    if (scanResults.critical > 0) {
                        echo "Warning: ${scanResults.critical} CRITICAL vulnerabilities detected in ${env.IMAGE_NAME}:latest"
                    }

                    if (scanResults.high > 0) {
                        echo "Note: ${scanResults.high} HIGH vulnerabilities detected in ${env.IMAGE_NAME}:latest"
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
                        params.AWS_DEFAULT_REGION,
                        env.AWS_CREDENTIALS_ID
                    )
                    echo "Docker image pushed to ECR: ${env.ECR_URI}"
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

        stage('Provision ECS') {
            steps {
                dir("${env.TF_DIR}") {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials-id']]) {
                        script {
                            // Initialize Terraform
                            sh "export AWS_DEFAULT_REGION=${params.AWS_DEFAULT_REGION} && terraform init"

                            // Run Terraform plan with detailed-exitcode
                            def planStatus = sh(
                                script: 'terraform plan -detailed-exitcode -out=tfplan || echo $?',
                                returnStdout: true
                            ).trim()

                            // Only apply if changes exist (exit code 2)
                            if (planStatus == '2') {
                                echo "Terraform plan shows changes. Applying..."
                                sh 'terraform apply -auto-approve tfplan'
                            } else {
                                echo "No infrastructure changes needed."
                            }
                        }
                    }
                }
            }
        }

        stage('Deploy to ECS') {
            steps {
                dir("${env.TF_DIR}") {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials-id']]) {
                        script {
                            // Deploy ECS task with updated container image
                            def fullImage = "${ECR_URI}:${IMAGE_TAG}"
                            sh """
                                export AWS_DEFAULT_REGION=${params.AWS_DEFAULT_REGION}
                                terraform init
                                terraform apply -auto-approve -var="container_image=${fullImage}"
                            """
                            echo "ECS deployment triggered with image: ${fullImage}"
                        }
                    }
                }
            }
        }

        // stage('Deploy to ECS') {
        //     steps {
        //         script {
        //             deployToECS(env.TF_DIR, env.AWS_CREDENTIALS_ID, params.AWS_DEFAULT_REGION, env.ECR_URI, env.IMAGE_TAG)
        //         }
        //     }
        // }
    }   

    post {
        always {
            echo "Build completed with result: ${currentBuild.currentResult}"
        }
    }
}