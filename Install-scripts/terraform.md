## Install Terraform
---

### 1️⃣ Add HashiCorp GPG key
```bash
wget -O - https://apt.releases.hashicorp.com/gpg | sudo gpg --dearmor -o /usr/share/keyrings/hashicorp-archive-keyring.gpg
```
### 2️⃣ Add the Terraform repository
```bash
echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/hashicorp-archive-keyring.gpg] https://apt.releases.hashicorp.com $(lsb_release -cs) main" | sudo tee /etc/apt/sources.list.d/hashicorp.list
```
### 3️⃣ Update and install Terraform
```bash 
sudo apt update && sudo apt install terraform -y
```
### 4️⃣ Verify the installation
```bash 
terraform --version
```
✅ Terraform is now installed and ready to use!

---