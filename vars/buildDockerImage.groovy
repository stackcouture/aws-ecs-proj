def call(String imageName) {
    echo "Building Docker image: ${imageName}:latest"
    sh """
        docker build -t ${imageName}:latest .
    """
    return "${imageName}:latest"
}