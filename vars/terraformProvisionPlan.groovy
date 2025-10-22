def call(String tfDir, String awsCredsId, String region) {
    dir(tfDir) {
        withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: awsCredsId]]) {
            echo "Initializing Terraform in ${tfDir}..."
            sh """
                set -e
                export AWS_DEFAULT_REGION=${region}
                terraform init -input=false
            """

            echo "Running Terraform plan..."
            def planExitCode = sh(
                script: '''
                    set +e
                    terraform plan -input=false -no-color -detailed-exitcode -out=tfplan
                    exit_code=$?
                    echo $exit_code
                ''',
                returnStdout: true
            ).trim()

            echo "Terraform plan exit code: ${planExitCode}"

            if (planExitCode == '1') {
                error("Terraform plan failed — check syntax or provider credentials.")
            } else if (planExitCode != '0' && planExitCode != '2') {
                echo "⚠ Unexpected exit code from Terraform plan: ${planExitCode}"
            }

            return planExitCode
        }
    }
}
