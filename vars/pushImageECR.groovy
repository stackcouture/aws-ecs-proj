def call(String imageName, String imageTag, String awsAccountId, String region) {
    def ecrUri = "${awsAccountId}.dkr.ecr.${region}.amazonaws.com/ecs-test-repo:${imageTag}"
    echo "Logging in to AWS ECR..."
    sh """
        docker tag diwali-wishes:latest ${ecrUri}:${imageTag}
        docker tag diwali-wishes:latest ${ecrUri}:latest

        echo "Pushing images to ECR..."
        docker push ${ecrUri}:${imageTag}
        docker push ${ecrUri}:latest
    """
    return ecrUri
}
