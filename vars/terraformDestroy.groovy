def call(Map config = [:]) {
    input message: config.approvalMessage ?: 'Approve Terraform Destroy?', ok: 'Destroy'

    def tfDir = config.tfDir ?: 'ecs-terraform'
    dir(tfDir) {
        def awsCred = config.awsCred ?: 'aws-credentials-id'
        def awsRegion = config.awsRegion ?: 'ap-south-1'

        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCred]]) {
            sh """
                echo "Authenticating with AWS..."
                export AWS_DEFAULT_REGION=${awsRegion}
                aws sts get-caller-identity

                echo "Initializing Terraform..."
                terraform init

                echo "Destroying Terraform resources..."
                terraform destroy -auto-approve
            """
        }
    }
}
