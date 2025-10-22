def call(String tfDir, String awsCredsId, String region, String ecrUri, String imageTag) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            sh """
                export AWS_DEFAULT_REGION=${region}
                terraform apply -auto-approve -var "image_uri=${ecrUri}" -var "image_tag=${imageTag}"
            """
            echo "Deployment to ECS triggered via Terraform."
        }
    }
}
