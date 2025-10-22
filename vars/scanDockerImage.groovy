def call(String imageRef) {
    // Replace colon in tag to make safe filename
    def safeTag = imageRef.replaceAll(':','-')
    def textReport = "trivy-scan-${safeTag}.txt"
    def htmlReport = "trivy-scan-${safeTag}.html"

    // Run trivy scan
    sh """
        mkdir -p contrib
        curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

        echo "Scanning Docker image ${imageRef} with Trivy..."

        # Text report
        trivy image --exit-code 0 -f table -o ${textReport} ${imageRef}
        cat ${textReport}

        # HTML report
        trivy image --exit-code 0 -f template -o ${htmlReport} --template "@contrib/html.tpl" ${imageRef}
    """

    // Archive reports in Jenkins
    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", allowEmptyArchive: true

    // Count CRITICAL vulnerabilities
    def critCount = sh(
        script: "grep -c 'CRITICAL' ${textReport} || true",
        returnStdout: true
    ).trim()

    // Print summary in Jenkins console
    echo "Image ${imageRef} has ${critCount} CRITICAL vulnerabilities."

    // Return the number of critical vulnerabilities (but do not fail pipeline)
    return critCount.isInteger() ? critCount.toInteger() : 0
}
