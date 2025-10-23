## GitHub Credentials Setup in Jenkins

Follow these steps to create a GitHub Personal Access Token (PAT) and add it securely in Jenkins.

---

### 1️⃣ Create a GitHub Personal Access Token (PAT)

1. Log in to **GitHub**.  
2. Go to:  
   `Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token`  
3. Configure the token:

   - **Expiration:** choose as per your policy (e.g., 90 days or no expiration)  
   - **Scopes:**  
     - `repo` → full control of private repositories (for cloning)  
     - `workflow` → if you want Jenkins to trigger GitHub Actions  
     - Optional: `admin:repo_hook` if managing webhooks  

4. Click **Generate token** and **copy it immediately**.  
    **Note:** You won’t be able to see it again after leaving the page.

---

### 2️⃣ Add GitHub Credentials in Jenkins

1. Open Jenkins → **Manage Jenkins → Credentials → System → Global credentials → Add Credentials**  
2. Select **Kind → Username with password**  
3. Fill in the fields:

   - **Username:** your GitHub username or `x-access-token`  
   - **Password:** paste the PAT you generated  
   - **ID (optional):** `github-cred` → use this in Jenkinsfiles  
   - **Description:** `GitHub Personal Access Token for private repo access`  

4. Click **✅ OK** to save.

> Jenkins now securely stores your GitHub credentials and you can use them in pipelines to access private repositories.
