def call(Map config = [:]) {
    def tfDir = config.tfDir ?: error("Missing 'tfDir'")
    def awsCredsId = config.awsCredsId ?: error("Missing 'awsCredsId'")
    def region = config.region ?: error("Missing 'region'")

    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Initializing Terraform in ${tfDir}..."
            sh "export AWS_DEFAULT_REGION=${region} && terraform init"

            // Terraform plan with detailed exit code
            def planStatus = sh(
                script: 'terraform plan -detailed-exitcode -out=tfplan || echo $?',
                returnStdout: true
            ).trim()

            if (planStatus == '2') {
                echo "Terraform plan shows changes. Applying..."
                sh 'terraform apply -auto-approve tfplan'
            } else {
                echo "No infrastructure changes needed."
            }
        }
    }
}