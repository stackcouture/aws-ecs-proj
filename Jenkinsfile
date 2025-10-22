@Library('my-shared-lib') _

pipeline {
    agent any

    environment {
        IMAGE_TAG          = "1.0.${BUILD_NUMBER}"
        GIT_URL            = "https://github.com/stackcouture/aws-ecs-proj.git"
        TF_DIR             = 'ecs-terraform'
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
                    authenticateAWS(params.AWS_DEFAULT_REGION, params.AWS_ACCOUNT_ID, 'aws-credentials-id')
                }
            }
        }
    }
}