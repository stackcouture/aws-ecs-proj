GitHub credentials in Jenkins

1️⃣ Create a GitHub Personal Access Token (PAT)

Log in to GitHub.

Go to Settings → Developer settings → Personal access tokens → Tokens (classic) → Generate new token

Set:

Expiration: choose as per your policy (e.g., 90 days, no expiration)

Scopes:

repo → full control of private repositories (for cloning)

workflow → if you want Jenkins to trigger GitHub Actions

Optional: admin:repo_hook if managing webhooks

Click Generate token and copy the token immediately.

⚠️ You won’t be able to see it again after leaving the page.

2️⃣ Add GitHub Credentials in Jenkins

Open Jenkins → Manage Jenkins → Credentials → System → Global credentials → Add Credentials

Select Kind → Username with password

Fill in:

Username: your GitHub username or x-access-token

Password: paste the PAT you generated

ID (optional): e.g., github-cred → use this in Jenkinsfiles

Description: e.g., GitHub Personal Access Token for private repo access

Click OK to save.