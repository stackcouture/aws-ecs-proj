def call(String imageRef) {
    def safeTag = imageRef.replaceAll(':','-')
    def textReport = "trivy-scan-${safeTag}.txt"
    def htmlReport = "trivy-scan-${safeTag}.html"

    sh """
        mkdir -p contrib
        curl -sSL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/html.tpl -o contrib/html.tpl

        echo "Scanning Docker image ${imageRef} with Trivy..."

        # Generate text report
        trivy image --exit-code 0 -f table -o ${textReport} ${imageRef}
        cat ${textReport}

        # Generate HTML report
        trivy image --exit-code 0 -f template -o ${htmlReport} --template "@contrib/html.tpl" ${imageRef}
    """

    // Archive the reports
    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", allowEmptyArchive: true

    // Count CRITICAL vulnerabilities
    def critCount = sh(
        script: "grep -c 'CRITICAL' ${textReport} || true",
        returnStdout: true
    ).trim()

    // Count HIGH vulnerabilities
    def highCount = sh(
        script: "grep -c 'HIGH' ${textReport} || true",
        returnStdout: true
    ).trim()

    // Print summary
    echo "======================================="
    echo "Trivy Scan Summary for ${imageRef}:"
    echo "CRITICAL vulnerabilities: ${critCount}"
    echo "HIGH vulnerabilities: ${highCount}"
    echo "Text report: ${textReport}"
    echo "HTML report: ${htmlReport}"
    echo "======================================="

    // Return a map for reporting
    return [
        critical: critCount.isInteger() ? critCount.toInteger() : 0,
        high: highCount.isInteger() ? highCount.toInteger() : 0
    ]
}
