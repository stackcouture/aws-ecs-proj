def call(String imageName, String imageTag, String awsAccountId, String region) {
    def ecrUri = "${awsAccountId}.dkr.ecr.${region}.amazonaws.com/ecs-test-repo"

    withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: 'aws-creds']]) {
        echo "Logging in to AWS ECR..."
        sh """
            aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${awsAccountId}.dkr.ecr.${region}.amazonaws.com
        """

        echo "Tagging images..."
        sh """
            docker tag ${imageName}:latest ${ecrUri}:${imageTag} || echo "Failed to tag ${imageName}:${imageTag}"
            docker tag ${imageName}:latest ${ecrUri}:latest || echo "Failed to tag ${imageName}:latest"
        """

        echo "Pushing images to ECR (pipeline will continue on failure)..."
        sh """
            docker push ${ecrUri}:${imageTag} || echo "Warning: Failed to push ${ecrUri}:${imageTag}"
            docker push ${ecrUri}:latest || echo "Warning: Failed to push ${ecrUri}:latest"
        """
    }

    echo "Docker push finished (errors, if any, were logged above)."
    return ecrUri
    
}
