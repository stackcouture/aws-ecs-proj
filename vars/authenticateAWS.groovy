def authenticateAWS(String awsRegion, String awsAccountId, String credentialsId) {
    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: credentialsId]]) {
        sh """
            echo "Authenticating with AWS..."
            export AWS_DEFAULT_REGION=${awsRegion}
            aws sts get-caller-identity

            echo "Logging into ECR..."
            aws ecr get-login-password --region ${awsRegion} | \\
            docker login --username AWS --password-stdin ${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com
        """
    }
}
