def call(Map config = [:]) {
    // Input approval message
    input message: config.approvalMessage ?: 'Approve Terraform Destroy?', ok: 'Destroy'

    // Navigate to Terraform folder
    dir(config.tfDir ?: 'ecs-terraform') {
        // Authenticate with AWS using credentials
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: config.awsCred ?: 'aws-credentials-id']]) {
            sh """
                echo "Authenticating with AWS..."
                export AWS_DEFAULT_REGION=${config.awsRegion ?: 'ap-south-1'}
                aws sts get-caller-identity

                echo "Initializing Terraform..."
                terraform init

                echo "Destroying Terraform resources..."
                terraform destroy -auto-approve
            """
        }
    }
}
