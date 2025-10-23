## Jenkins Installation & Shared Library Setup Guide

This guide walks you through installing Jenkins on Ubuntu, setting up essential plugins, and configuring a **Jenkins Shared Library** for reusable pipeline code.

---

### 1️⃣ Install Jenkins

Update packages and install dependencies:

```bash
sudo apt update -y
sudo apt install fontconfig openjdk-21-jre -y
sudo wget -O /etc/apt/keyrings/jenkins-keyring.asc \
  https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key

echo "deb [signed-by=/etc/apt/keyrings/jenkins-keyring.asc] \
https://pkg.jenkins.io/debian-stable binary/" | sudo tee /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt update -y
sudo apt install jenkins -y
```

### 2️⃣ Enable and Start Jenkins

```bash
sudo systemctl enable jenkins
sudo systemctl start jenkins
sudo systemctl status jenkins --no-pager
```

### 3️⃣ Configure Firewall
```bash
sudo ufw allow 8080/tcp
sudo ufw enable -y
sudo ufw status
```

### 4️⃣ Retrieve Initial Admin Password
```bash
JENKINS_PASSWORD=$(sudo cat /var/lib/jenkins/secrets/initialAdminPassword)
echo "🔑 Jenkins Initial Admin Password: $JENKINS_PASSWORD"

Access Jenkins at:
http://<EC2_Public_IP>:8080
```

---

## Install Required Jenkins Plugins

Install the following plugins in Jenkins to support CI/CD pipelines, Docker, and AWS integration:

- Amazon ECR Plugin
- AWS Credentials Plugin
- Build With Parameters
- Docker API Plugin
- Docker Pipeline
- Docker Plugin
- Git Plugin
- Groovy
- HTML Publisher Plugin
- Pipeline Graph View Plugin
- Pipeline: Groovy
- SSH Build Agents Plugin
- Workspace Cleanup Plugin

---

## Jenkins Shared Library Setup

Follow these steps to create and use a shared library in Jenkins.

### 1️⃣ Prepare the Shared Library Repository

Create a Git repository to host your shared library. Example:

jenkins-shared-library

**Repository Structure:**
```bash 
(root)
├── vars/
│ └── myFunction.groovy # callable in Jenkinsfile as "myFunction()"

> `vars/` – scripts that can be called directly in Jenkinsfiles.
```

### 2️⃣ Add the Library to Jenkins

1. Open **Jenkins → Manage Jenkins → Configure System**  
2. Scroll down to **Global Pipeline Libraries**  
3. Click **Add** and configure:  
   - **Name:** `my-shared-lib` (used in Jenkinsfile)  
   - **Default version:** branch name (e.g., `main`)  
   - **Retrieval method:** Modern SCM → Git  
   - **Project Repository:** `https://github.com/your-org/jenkins-shared-library.git`  
   - **Credentials:** Optional, if the repo is private  
4. Click **Save**

### 3️⃣ Use the Shared Library in a Jenkinsfile

At the top of your Jenkinsfile, import the library:

```groovy
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
```
### 4️⃣ Example vars/myFunction.groovy
```groovy
def call(String message) {
    echo "Shared Library says: ${message}"
}
```