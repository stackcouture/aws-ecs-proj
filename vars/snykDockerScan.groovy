def call(Map config = [:]) {
    // Mandatory parameters
    def ecrUri = config.ecrUri ?: error("Missing 'ecrUri'")
    def imageTag = config.imageTag ?: error("Missing 'imageTag'")
    def credentialsId = config.credentialsId ?: error("Missing 'credentialsId' (Jenkins Snyk token)")

    def reportDir = "reports/snyk/${env.BUILD_NUMBER}"
    def jsonReport = "${reportDir}/snyk-${imageTag}.json"
    def htmlReport = "${reportDir}/snyk-${imageTag}.html"

    withCredentials([string(credentialsId: credentialsId, variable: 'SNYK_TOKEN')]) {
        sh """
            mkdir -p '${reportDir}'

            # Authenticate Snyk
            snyk auth \$SNYK_TOKEN > /dev/null 2>&1 || { echo "Snyk auth failed"; exit 1; }

            echo "Scanning Docker image ${ecrUri}:${imageTag} with Snyk..."
            
            set +e  # continue even if vulnerabilities are found
            snyk container test '${ecrUri}:${imageTag}' --severity-threshold=high --exclude-base-image-vulns --json > '${jsonReport}'
            SCAN_EXIT_CODE=\$?
            set -e

            if [ \$SCAN_EXIT_CODE -ne 0 ]; then
                echo "Warning: Snyk scan detected issues or failed (exit code \$SCAN_EXIT_CODE), but pipeline will continue."
            else
                echo "Snyk scan completed successfully."
            fi

            # Convert JSON to simple HTML report
            cat <<EOF > '${htmlReport}'
<html><body><pre>
EOF

            if [ -s '${jsonReport}' ]; then
                cat '${jsonReport}' | jq . >> '${htmlReport}'
            else
                echo "Snyk scan failed or returned no data. Please check Jenkins logs." >> '${htmlReport}'
            fi

            echo "</pre></body></html>" >> '${htmlReport}'
        """
    }

    // Archive reports
    archiveArtifacts artifacts: "${jsonReport},${htmlReport}", allowEmptyArchive: true

    // Optional: Publish HTML report in Jenkins UI
    publishHTML(target: [
        allowMissing: true,
        alwaysLinkToLastBuild: true,
        keepAll: true,
        reportDir: reportDir,
        reportFiles: htmlReport.replace("${reportDir}/", ""),
        reportName: "Snyk Image Scan - Build ${env.BUILD_NUMBER}"
    ])
}
