## Docker Installation on Ubuntu

Follow these steps to install **Docker** on Ubuntu:

---

### 1️⃣ Update Packages

```bash
sudo apt-get update -y
sudo apt-get install ca-certificates curl -y
```

2️⃣ Set Up Docker Repository
```bash
Copy code
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc
```

Add the Docker repository to your Apt sources:

```bash
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \
  $(. /etc/os-release && echo "${UBUNTU_CODENAME:-$VERSION_CODENAME}") stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```
Update package information:

```bash 
sudo apt-get update -y
```

3️⃣ Install Docker Engine
```bash 
sudo apt-get install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin -y
```

4️⃣ Enable and Start Docker
```bash 
sudo systemctl enable docker
sudo systemctl start docker
```

✅ Docker is now installed and running on your Ubuntu machine.
You can verify by running:
```bash 
docker --version
docker run hello-world
```
---