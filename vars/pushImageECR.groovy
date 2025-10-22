def call(String imageName, String imageTag, String awsAccountId, String region) {
    def ecrUri = "${awsAccountId}.dkr.ecr.${region}.amazonaws.com/ecs-test-repo:${imageTag}"
    echo "Logging in to AWS ECR..."
    sh """
        aws ecr get-login-password --region ${region} | docker login --username AWS --password-stdin ${awsAccountId}.dkr.ecr.${region}.amazonaws.com
        docker tag ${imageName}:${imageTag} ${ecrUri}
        docker push ${ecrUri}
    """
    return ecrUri
}
