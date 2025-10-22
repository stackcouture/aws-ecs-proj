def call(String imageUri, String imageTag) {
    
    def textReport = "trivy-scan-${imageTag}.txt"
    def htmlReport = "trivy-scan-${imageTag}.html"
    
    try {
        sh """
            mkdir -p contrib
            curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

            echo "Scanning Docker image ${imageUri}:${imageTag} with Trivy..."

            trivy image -f table -o ${textReport} ${imageUri}:${imageTag}
            cat ${textReport}

            trivy image -f template -o ${htmlReport} --template "@contrib/html.tpl" ${imageUri}:${imageTag}

            
            if grep -q "CRITICAL" ${textReport}; then
                echo "Critical vulnerabilities detected! Failing the build."
                exit 1
            fi
        
        """
    } catch (Exception e) {
        echo "Trivy scan failed: ${e.getMessage()}"
        currentBuild.result = 'UNSTABLE'
    }

    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", onlyIfSuccessful: true
}