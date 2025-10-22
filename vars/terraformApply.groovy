def call(String tfDir, String awsCredsId) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Applying Terraform changes..."
            sh 'terraform apply -auto-approve tfplan'
            echo "Terraform apply completed successfully."
        }
    }
}
