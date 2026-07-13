## 📖 Project Overview

This project demonstrates a complete **AWS ECS DevSecOps CI/CD pipeline** that automates the build, security scanning, infrastructure provisioning, and deployment of a containerized web application on **Amazon ECS**.

The solution follows **Infrastructure as Code (IaC)** principles using **Terraform** and implements **DevSecOps** best practices by integrating automated container vulnerability scanning into the CI/CD pipeline.

The **Jenkins pipeline** automatically checks out the source code from GitHub, builds a Docker image, performs security scans using **Trivy** and **Snyk**, pushes the verified image to **Amazon ECR**, provisions or updates AWS infrastructure using **Terraform**, and deploys the latest application version to **Amazon ECS** behind an **Application Load Balancer (ALB)**. This provides a fully automated, repeatable, secure, and production-ready deployment workflow.

The infrastructure is designed to be **modular**, **scalable**, and **reproducible**, provisioning networking components, ECS resources, IAM roles, security groups, Amazon ECR repositories, and load balancing through Terraform. By combining CI/CD automation, container security, and Infrastructure as Code, this project demonstrates how production-grade containerized applications can be deployed consistently on AWS with minimal manual intervention.

---
### 🚀 Key Highlights

- ⚙️ Automated CI/CD pipeline using **Jenkins**
- 🐳 Containerized application with **Docker**
- ☁️ Infrastructure provisioning using **Terraform**
- 📦 Container image storage with **Amazon ECR**
- 🚀 Application deployment on **Amazon ECS**
- 🌐 Load balancing with **Application Load Balancer (ALB)**
- 🔒 Automated container vulnerability scanning using **Trivy**
- 🛡️ Security analysis using **Snyk**
- 🔑 Secure AWS authentication with **IAM Roles & Policies**
- 🔄 End-to-end deployment automation from **code commit to production**
- 📁 Modular Infrastructure as Code (IaC) architecture
- 📈 Production-ready, repeatable deployment workflow

---
## Architecture

![Project Overview](docs/images/ecs_deployment.png "Architecture")


---
### Tech Stack

**AWS Services:**
- EC2, ECS, ECR
- VPC, Subnets, Internet Gateway, Route Tables
- Application Load Balancer (ALB)

**Tools & Technologies:**
- Jenkins, Docker, Trivy, Terraform, Snyk, GitHub
- Jenkins Shared Libraries for CI/CD automation

**Programming / Markup:**
- HTML5, CSS3, JavaScript

---

### Architecture

#### 1️⃣ EC2 Setup
- Ubuntu EC2 instance
- Install Jenkins, Docker, Trivy, Snyk, Terraform

#### 2️⃣ CI/CD Pipeline
- Jenkins pipeline pulls source code from GitHub
- Authenticates with AWS and builds Docker image
- Pushes Docker image to **AWS ECR**
- Runs **Trivy & Snyk scans** for container security

#### 3️⃣ Infrastructure Provisioning (Terraform)
- VPC with public subnets
- Internet Gateway & Route Tables
- ALB & Target Groups
- ECS Cluster and Task Definitions

#### 4️⃣ Deployment
- Jenkins deploys the latest Docker image to ECS
- Automatic deployment via shared library functions

---

### Jenkins Pipeline Highlights

- `@Library('my-shared-lib') _` for reusable CI/CD functions
- Environment variables: `IMAGE_NAME`, `IMAGE_TAG`, `TF_DIR`, `AWS_CREDENTIALS_ID`
- Stages:
  1. Clean Workspace
  2. Git Checkout
  3. AWS Authentication
  4. Build Docker Image
  5. Trivy Scan
  6. Push Docker Image to ECR
  7. Snyk Container Scan
  8. Provision ECS (Terraform)
  9. Deploy to ECS

---

### Security

- Container vulnerability scanning using **Trivy** and **Snyk**

---

### Contributing

Contributions to this **festive DevOps project** are welcome! You can help improve the project by enhancing CI/CD, security, or documentation.

---
### Areas to Contribute
- Add **new CI/CD pipeline stages** or improve existing Jenkins shared library functions.
- Integrate additional **container security scans** (e.g., trivy, snyk).
- Extend **monitoring dashboards** for ECS, ALB, Docker, or application metrics.
- Optimize **Terraform modules** for reusability and efficiency.

💡 **Tip:** Keep the focus on **automation, DevOps best practices, and containerized deployments**, while maintaining the festive spirit of the Diwali website.

---
### License

- This project is licensed under the **MIT License** – feel free to use, adapt, or extend it for   
  your organization or personal learning.
---
