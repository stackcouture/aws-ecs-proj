def call(Map config = [:]) {
    // Optional: folder to delete (default to TF_DIR if provided)
    def folderToDelete = config.tfDir ?: ''

    echo "Cleaning up workspace after build..."

    sh 'chown -R jenkins:jenkins . || true'

    if (folderToDelete) {
        sh "rm -rf ${folderToDelete} || true"
    }
   cleanWs()
}