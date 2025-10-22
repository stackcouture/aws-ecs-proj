def call(String ecrUri, String imageTag) {
    
    def textReport = "trivy-scan-${imageTag}.txt"
    def htmlReport = "trivy-scan-${imageTag}.html"
    
    try {
        sh """
            mkdir -p contrib
            curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

            echo "Scanning Docker image ${ecrUri}:${imageTag} with Trivy..."
            trivy image -f table -o ${textReport} ${ecrUri}:${imageTag}
            cat ${textReport}

            trivy image -f template -o ${htmlReport} --template "@contrib/html.tpl" ${ecrUri}:${imageTag}
        """
    } catch (Exception e) {
        echo "Trivy scan failed: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
    }

    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", onlyIfSuccessful: true
}