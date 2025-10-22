def call(String ecrUri, String imageTag) {
    def outputFile = "trivy-scan-${imageTag}.txt"

    try {
        sh """
            echo "Scanning Docker image ${ecrUri}:${imageTag} with Trivy..."
            trivy image -f table -o ${outputFile} ${ecrUri}:${imageTag}
            cat ${outputFile}
        """
    } catch (Exception e) {
        echo "Trivy scan failed: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
    }

    archiveArtifacts artifacts: outputFile, onlyIfSuccessful: true
}