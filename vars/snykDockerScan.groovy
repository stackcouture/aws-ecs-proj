def call(Map params) {
    echo "Scanning image ${params.ecrUri}:${params.imageTag} with Snyk..."
    withCredentials([string(credentialsId: params.credentialsId, variable: 'SNYK_TOKEN')]) {
        sh """
            snyk container test ${params.ecrUri}:${params.imageTag} --org=my-org --severity-threshold=high --all-projects
        """
    }
}
