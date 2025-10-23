## AWS CLI Installation & Configuration Guide

This guide will help you install the **AWS CLI** on Linux/Ubuntu and configure your AWS credentials for **Terraform, SDKs, or CI/CD pipelines**.

---

### 1️⃣ Install AWS CLI (if not already installed)

Check if AWS CLI is already installed:

```bash
aws --version

If not installed, for Linux / Ubuntu:
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install

```
```bash 
✅ Verify installation:
aws --version
```

### 2️⃣ Configure AWS Credentials
Run the following command:
aws configure

You will be prompted to enter:

Prompt	Example Value
```bash 
AWS Access Key ID	AKIAxxxxxxxxxxxx
AWS Secret Access Key	xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
Default region name	ap-south-1
Default output format	json
```
Note: Credentials are saved in ~/.aws/credentials and configuration in ~/.aws/config.

### 3️⃣ Verify Configuration
aws sts get-caller-identity

✅ This command returns your AWS account ID and ARN to confirm that the credentials are working.

### 4️⃣ Optional: Use Environment Variables Instead

For temporary sessions or CI/CD pipelines, you can export AWS credentials as environment variables:

export AWS_ACCESS_KEY_ID=AKIAxxxxxxxxxxxx
export AWS_SECRET_ACCESS_KEY=xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
export AWS_DEFAULT_REGION=ap-south-1


### 5️⃣ Add AWS Credentials in Jenkins

Follow these steps to securely store your AWS credentials in Jenkins:

1. Open Jenkins → **Manage Jenkins → Credentials → System → Global credentials (unrestricted)**
2. Click **➕ Add Credentials**
3. Select **Kind → Username with password**
4. Fill in the fields:

   - **Username:** `AWS_ACCESS_KEY_ID` value (e.g., `AKIAxxxxxxxxxxxx`)  
   - **Password:** `AWS_SECRET_ACCESS_KEY` value  
   - **ID:** `aws-credentials` (this will be used in your Jenkins pipeline)  
   - **Description:** `AWS credentials for Terraform / AWS CLI`

5. Click **✅ OK** to save.


