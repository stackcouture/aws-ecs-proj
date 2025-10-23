## Snyk CLI Installation & Authentication

Follow these steps to install Snyk on a Linux/Ubuntu machine and authenticate using your personal token.

---

### 1️⃣ Install Snyk CLI

```bash
sudo apt-get update
curl https://static.snyk.io/cli/latest/snyk-linux -o snyk
chmod +x ./snyk
sudo mv ./snyk /usr/local/bin/
```

✅ Verify installation:
```bash
snyk --version
```

### 2️⃣ Generate Snyk Token

1. Go to [Snyk](https://snyk.io/) and create an account (if you don’t have one).  
2. Navigate to the **Dashboard** → click on your account name on the left menu → **Account Settings**.  
3. Under the **General** tab, click **Generate Auth Token**.  
4. Copy the token and save it securely (e.g., in Notepad).  

### 3️⃣ Authenticate Snyk CLI
```bash
    snyk auth <YOUR_GENERATED_TOKEN>
```
✅ After authentication, you can start scanning your projects:
```bash
snyk test
```

### 4️⃣ Store Snyk Token in Jenkins Credentials

1. Open Jenkins → **Manage Jenkins** → **Credentials** → **System** → **Global credentials (unrestricted)**  
2. Click **➕ Add Credentials**  
3. Select **Kind** → **Secret text**  
4. Paste your **Snyk token** in the **Secret** field  
5. Set **ID** (e.g., `snyk-token`) → use this in your Jenkins pipeline  
6. Optionally, add a **Description** (e.g., `Snyk API token for scans`)  
7. Click **✅ OK** to save  

> 💡 Now your Jenkins pipelines can securely access Snyk using the stored token.