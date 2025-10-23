# AWS ECS Fargate with ALB - Modular Terraform Setup

This repository provides a **modular Terraform configuration** to deploy an **AWS ECS Fargate cluster** with an **Application Load Balancer (ALB)**, **VPC**, subnets, and all required networking components.

---

## Module Structure

- **`modules/vpc_module`**:  
  Creates VPC, public subnets, Internet Gateway, route table, and a security group for ALB.

- **`modules/alb_module`**:  
  Provisions an Application Load Balancer, target group, and listener.

- **`modules/ecs_module`**:  
  Deploys ECS cluster, IAM role, task definition, and ECS service (Fargate) integrated with the ALB.

---

## Features

- VPC with **configurable CIDR block**  
- Public subnets across multiple **Availability Zones**  
- Internet Gateway & route table for public internet access  
- Security group allowing **HTTP (port 80)** access  
- Application Load Balancer (ALB) for traffic distribution  
- ECS Cluster (Fargate launch type)  
- IAM Role for ECS task execution  
- ECS Task Definition with **configurable Docker image**  
- ECS Service integrated with ALB  

---

## Usage

### 1. Clone the Repository
```bash
git clone https://github.com/stackcouture/aws-ecs-proj/
cd ecs-terraform
```

### 2. Configure 
```bash
Configure your AWS credentials (via environment variables, AWS CLI, or shared credentials file).
```
### 3. Edit main.tf
```bash 
    * Adjust VPC CIDR, subnet CIDRs, AZs, ECS parameters, and Docker image as needed.
    * The backend is configured for S3; update the bucket/key/region if required.
```
### 4. Initialize Terraform
```bash 
    terraform init
```
### 5. Plan the deployment
```bash 
    terraform plan -out=tfplan
```
### 6. Apply the configuration
```bash 
    terraform apply -auto-approve -out=tfplan
```
### 7. Outputs
```bash 
    * VPC ID, subnet IDs, ECS cluster/service/task ARNs, and ALB DNS name will be displayed after   apply.
```
---

## Module Inputs & Outputs

### **VPC Module (`modules/vpc_module`)**
**Inputs:**
- `vpc_cidr` – CIDR block for the VPC  
- `public_subnet_cidrs` – List of public subnet CIDRs  
- `azs` – List of Availability Zones  
- `tags` (optional) – Map of tags  

**Outputs:**
- `vpc_id` – ID of the created VPC  
- `public_subnet_ids` – List of public subnet IDs  
- `alb_sg_id` – Security group ID for ALB  

---

### **ALB Module (`modules/alb_module`)**
**Inputs:**
- `security_group_ids` – Security group IDs for ALB  
- `subnet_ids` – Subnet IDs where ALB will be deployed  
- `tags` – Optional tags  
- `target_group_port` – Port for ALB target group  
- `vpc_id` – VPC ID  

**Outputs:**
- `alb_arn` – ARN of the Application Load Balancer  
- `alb_dns_name` – DNS name of the ALB  
- `target_group_arn` – ARN of the target group  
- `listener_arn` – ARN of the listener  

---

### **ECS Module (`modules/ecs_module`)**
**Inputs:**
- ECS cluster, service, and task parameters  
- Subnet IDs and Security Group IDs  
- ALB target group and listener ARNs  

**Outputs:**
- `ecs_cluster_id` – ID of the ECS cluster  
- `ecs_service_name` – Name of the ECS service  
- `ecs_task_definition_arn` – ARN of the ECS task definition  

---

## Notes
- Ensure your AWS account has **sufficient permissions** to create all resources.  
- The ALB and ECS service are configured for **HTTP (port 80)** by default.  
- ECS service uses **Fargate launch type** and a sample **NGINX image**. Update the Docker image as needed.  

---

## Cleanup
To destroy all resources created by Terraform:

```bash
terraform destroy








