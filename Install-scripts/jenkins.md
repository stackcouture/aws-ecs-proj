1. Install Jenkins 

   sudo apt update -y
sudo apt install fontconfig openjdk-21-jre -y

sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key
echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc]" \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null
sudo apt update -y
sudo apt install jenkins -y

sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins

echo "✅ Checking Jenkins status..."
sudo systemctl status jenkins --no-pager

echo "🌐 Configuring firewall (Allow port 8080)..."
sudo ufw allow 8080/tcp
sudo ufw enable -y
sudo ufw status

echo "🔑 Retrieving Jenkins admin password..."
JENKINS_PASSWORD=$(sudo cat /var/lib/jenkins/secrets/initialAdminPassword)

echo "🎉 Jenkins installed successfully!"
echo "🔗 Access Jenkins at: http://$(curl -s ifconfig.me):8080"
echo "🛠 Initial Admin Password: $JENKINS_PASSWORD"
echo "💡 Save this password to log in for the first time."

echo "✅ Done!"
Connect to Jenkins using <EC2_Public_IP:8080>

Install the Below plugins 

Amazon ECR plugin
AWS Credentials Plugin
Build With Parameters
Docker API Plugin
Docker Pipeline
Docker plugin
Git plugin
Groovy
HTML Publisher plugin
Pipeline Graph View Plugin
Pipeline: Groovy
SSH Build Agents plugin
Workspace Cleanup Plugin


1️⃣ Prepare the Shared Library Repository

Create a Git repository to host your shared library. For example:

jenkins-shared-library


Structure the repository like this:

(root)
├── vars/
│   └── myFunction.groovy        # callable in Jenkinsfile as "myFunction()"


vars/ – scripts that can be called directly in Jenkinsfiles.

2️⃣ Add the Library to Jenkins
Open Jenkins → Manage Jenkins → Configure System
Scroll down to Global Pipeline Libraries
Click Add:
Name: my-shared-lib (this is used in Jenkinsfile)
Default version: branch name (e.g., main)
Retrieval method: Modern SCM → Git
Project Repository: https://github.com/your-org/jenkins-shared-library.git
Optional: Credentials if private repo
Save the configuration.

3️⃣ Use the Shared Library in a Jenkinsfile
At the top of your Jenkinsfile, import the library:

@Library('my-shared-lib') _


Call a function defined in vars/myFunction.groovy:

pipeline {
    agent any
    stages {
        stage('Test Shared Library') {
            steps {
                script {
                    myFunction("Hello from shared library!")
                }
            }
        }
    }
}

4️⃣ Example vars/myFunction.groovy
def call(String message) {
    echo "Shared Library says: ${message}"
}


