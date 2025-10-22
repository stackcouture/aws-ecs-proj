def call(String tfDir, String awsCredsId, String region) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            sh "export AWS_DEFAULT_REGION=${region}"
            sh "terraform init -input=false"

            def exitCode = sh(
                script: '''
                    set +e
                    terraform plan -input=false -no-color -detailed-exitcode -out=tfplan
                    echo $?
                ''',
                returnStdout: true
            ).trim()

            return exitCode.isInteger() ? exitCode.toInteger() : 1
        }
    }
}
