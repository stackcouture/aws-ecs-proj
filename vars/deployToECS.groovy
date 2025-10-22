def call(String tfDir, String awsCredsId, String region, String ecrUri, String imageTag) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Deploying ECS service using Terraform..."
            sh """
                set -e
                export AWS_DEFAULT_REGION=${region}
                terraform init -input=false
                terraform apply -auto-approve -var="container_image=${ecrUri}:${imageTag}"
            """
            echo "ECS Deployment completed — Service updated with ${ecrUri}:${imageTag}"
        }
    }
}
