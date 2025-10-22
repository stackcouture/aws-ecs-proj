def deployToECS(String tfDir, String awsCredsId, String region, String ecrUri, String imageTag) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            script {
                echo "Starting ECS Deployment using Terraform..."
                sh """
                    export AWS_DEFAULT_REGION=${region}
                    terraform init -input=false
                    terraform apply -auto-approve -var="container_image=${ecrUri}:${imageTag}"
                """
                echo "ECS Deployment completed successfully — Service updated with ${ecrUri}:${imageTag}"
            }
        }
    }
}