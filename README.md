## Static Diwali Wishes Website - AWS ECS Deployment

This project is a **festive DevOps exercise** — a static Diwali wishes website deployed on **AWS ECS** with a complete **CI/CD pipeline** using **Jenkins** and **Terraform**.

---

### Project Overview

The goal of this project is to:

- Build a **static HTML5, CSS3, JavaScript, and Three.js Diwali wishes website**.
- Deploy it securely and automatically on **AWS ECS**.
- Provision infrastructure using **Terraform (IaC)**.
- Implement CI/CD and container security with **Jenkins, Docker, Trivy, and Snyk**.

---

### Tech Stack

**AWS Services:**
- EC2, ECS, ECR
- VPC, Subnets, Internet Gateway, Route Tables
- Application Load Balancer (ALB)

**Tools & Technologies:**
- Jenkins, Docker, Trivy, Terraform, GitHub
- GitHub Shared Libraries for CI/CD automation

**Programming / Markup:**
- HTML5, CSS3, JavaScript, Three.js

---

### Architecture

#### 1️⃣ EC2 Setup
- Ubuntu EC2 instance
- Install Jenkins, Docker, Trivy, Terraform

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
- Automatic deployment across dev/test/prod environments via shared library functions

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

This project is licensed under the **MIT License** – feel free to use, adapt, or extend it for your organization or personal learning.
---
