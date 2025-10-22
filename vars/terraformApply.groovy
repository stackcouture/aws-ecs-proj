def call(String tfDir, String awsCredsId) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Applying Terraform plan..."
            sh 'terraform apply -auto-approve tfplan'
        }
    }
}
