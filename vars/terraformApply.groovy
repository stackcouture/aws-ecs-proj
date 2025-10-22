def terraformApply(String tfDir, String awsCredsId) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            script {
                echo "Applying Terraform changes..."
                sh '''
                    set -e
                    terraform apply -auto-approve tfplan
                '''
                echo "Terraform apply completed successfully."
            }
        }
    }
}