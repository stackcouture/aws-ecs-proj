pipeline {
    agent any

    environment {
        AWS_DEFAULT_REGION = 'ap-south-1'
        AWS_ACCOUNT_ID     = '104824081961'
        IMAGE_TAG          = "1.0.${BUILD_NUMBER}"
        ECR_URI            = "${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com/ecs-test-repo"
        TF_DIR             = 'ecs-terraform'
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Git Checkout') {
            steps {
                    git branch: 'main',
                    credentialsId: 'github-cred',
                    url: 'https://github.com/stackcouture/aws-ecs-proj.git'
            }
        }

        stage('Authenticate with AWS and ECR') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials-id']]) {
                    sh '''
                        echo "Authenticating with AWS..."
                        export AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION}
                        aws sts get-caller-identity

                        echo "Logging into ECR..."
                        aws ecr get-login-password --region $AWS_DEFAULT_REGION | \
                        docker login --username AWS --password-stdin ${AWS_ACCOUNT_ID}.dkr.ecr.${AWS_DEFAULT_REGION}.amazonaws.com
                    '''
                }
            }
        }

        stage('Build, Tag & Push app Docker Image to AWS ECR') {
            steps {
                dir('diwali-wishes') {
                    sh """
                        echo "Building Docker image..."
                        docker build -t diwali-wishes:latest .

                        echo "Tagging image with ${IMAGE_TAG} and latest..."
                        docker tag diwali-wishes:latest ${ECR_URI}:${IMAGE_TAG}
                        docker tag diwali-wishes:latest ${ECR_URI}:latest

                        echo "Pushing images to ECR..."
                        docker push ${ECR_URI}:${IMAGE_TAG}
                        docker push ${ECR_URI}:latest
                    """
                }
            }
        }

        stage('Scan Latest Diwali Docker Image using Trivy') {
            steps {
                sh "trivy image ${ECR_URI}:${IMAGE_TAG}"
            }
        }

        stage('Provision Plan') {
            steps {
                dir("${env.TF_DIR}") {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials-id']]) {
                        script {
                            echo "Initializing Terraform..."
                            sh '''
                                set -e
                                export AWS_DEFAULT_REGION=${AWS_DEFAULT_REGION}
                                terraform init -input=false
                            '''

                            echo "Running Terraform plan..."
                            def planExitCode = sh(
                                script: '''
                                    set +e
                                    terraform plan -input=false -no-color -detailed-exitcode -out=tfplan
                                    exit_code=$?
                                    echo $exit_code
                                ''',
                                returnStdout: true
                            ).trim()

                            if (planExitCode == '2') {
                                echo "Infrastructure changes detected — saved to tfplan file."
                            } else if (planExitCode == '0') {
                                echo "ℹNo infrastructure changes detected."
                            } else if (planExitCode == '1') {
                                error("Terraform plan failed — check syntax or provider credentials.")
                            } else {
                                echo "Unexpected exit code from Terraform plan: ${planExitCode}"
                            }
                        }
                    }
                }
            }
        }

        stage('Terraform Apply') {
            when { expression { fileExists("${env.TF_DIR}/tfplan") } }
            steps {
                input message: 'Approve Terraform Apply?', ok: 'Apply'
                dir("${env.TF_DIR}") {
                    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-credentials-id']]) {
                        script {
                            sh 'terraform apply -auto-approve tfplan'
                        }
                    }
                }
            }
        }
    }
}