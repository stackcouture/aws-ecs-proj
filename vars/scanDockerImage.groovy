def call(String imageRef) {

    def safeTag = imageRef.replaceAll(':','-')
    def textReport = "trivy-scan-${safeTag}.txt"
    def htmlReport = "trivy-scan-${safeTag}.html"
    
    sh """
        mkdir -p contrib
        curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

        echo "Scanning Docker image ${imageRef} with Trivy..."

        trivy image --exit-code 0 -f table -o ${textReport} ${imageRef}
        cat ${textReport}

        trivy image --exit-code 0 -f template -o ${htmlReport} --template "@contrib/html.tpl" ${imageRef}

        CRIT_COUNT=\$(grep -c "CRITICAL" ${textReport} || true)
        if [ "\$CRIT_COUNT" -gt 0 ]; then
            echo "Found \$CRIT_COUNT CRITICAL vulnerabilities in ${imageRef}! Build will fail."
            exit 1
        else
            echo "No critical vulnerabilities found. Safe to push."
        fi
    """

    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", onlyIfSuccessful: true
}