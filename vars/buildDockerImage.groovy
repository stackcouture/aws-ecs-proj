def call(String imageName) {
    dir(imageName) {
        sh """
            docker build --no-cache -t ${imageName}:latest .
        """
    }
    return "${imageName}:latest"
}