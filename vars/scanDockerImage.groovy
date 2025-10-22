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
    """

    archiveArtifacts artifacts: "${textReport}, ${htmlReport}", allowEmptyArchive: true

    def critCount = sh(
        script: "grep -c 'CRITICAL' ${textReport} || true",
        returnStdout: true
    ).trim()

    echo "Image ${imageRef} has ${critCount} CRITICAL vulnerabilities."

    // DO NOT stop the pipeline
    // return the number for reporting or optional warnings
    return critCount.isInteger() ? critCount.toInteger() : 0
}
