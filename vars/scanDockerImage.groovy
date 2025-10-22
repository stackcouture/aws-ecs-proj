def call(String imageRef) {

    def safeTag = imageRef.replaceAll(':','-')
    def textReport = "trivy-scan-${safeTag}.txt"
    def htmlReport = "trivy-scan-${safeTag}.html"
    
    try {
        sh """
            mkdir -p contrib
            curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

            echo "Scanning Docker image ${imageRef} with Trivy..."

            trivy image -f table -o ${textReport} ${imageRef}
            cat ${textReport}

            trivy image -f template -o ${htmlReport} --template "@contrib/html.tpl" ${imageRef}

            
            if grep -q "CRITICAL" ${textReport}; then
                echo "Critical vulnerabilities detected in ${imageRef}! Failing the build."
                exit 1
            fi
        """
    } catch (Exception e) {
        echo "Trivy scan failed: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
    }

    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", onlyIfSuccessful: true
}