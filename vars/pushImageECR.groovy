def call(String imageName, String imageTag, String awsAccountId, String awsRegion) {
    def ECR_URI = "${awsAccountId}.dkr.ecr.${awsRegion}.amazonaws.com/ecs-test-repo"

    dir(imageName) {
        sh """
            echo "Building Docker image..."
 
            echo "Tagging image with ${imageTag} and latest..."
            docker tag ${imageName}:latest ${ECR_URI}:${imageTag}
            docker tag ${imageName}:latest ${ECR_URI}:latest

            echo "Pushing images to ECR..."
            docker push ${ECR_URI}:${imageTag}
            docker push ${ECR_URI}:latest
        """
    }

    return ECR_URI
}