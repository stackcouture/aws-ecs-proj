def call(Map config = [:]) {
    def tfDir = config.tfDir ?: error("Missing 'tfDir'")
    def awsCredsId = config.awsCredsId ?: error("Missing 'awsCredsId'")
    def region = config.region ?: error("Missing 'region'")
    def ecrUri = config.ecrUri ?: error("Missing 'ecrUri'")
    def imageTag = config.imageTag ?: error("Missing 'imageTag'")

    def fullImage = "${ecrUri}:${imageTag}"

    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Deploying ECS task with image: ${fullImage}"
            sh """
                export AWS_DEFAULT_REGION=${region}
                terraform init
                terraform apply -auto-approve -var="container_image=${fullImage}"
            """
        }
    }
}
