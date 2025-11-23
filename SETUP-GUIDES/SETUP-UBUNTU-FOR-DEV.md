# Setup Ubuntu for Development

## 1. Update system
```bash
sudo apt update && sudo apt upgrade -y
```

## 2. Install essential dev tools
```bash
sudo apt install -y build-essential curl wget git unzip zip htop net-tools dnsutils ca-certificates software-properties-common apt-transport-https lsb-release gnupg
```

## 3. Check Ubuntu release
```bash
lsb_release -a
```

---

## 4. Install OpenJDK (17 & 21) and manage with alternatives
```bash
sudo apt install -y openjdk-21-jdk openjdk-17-jdk

# Configure default java / javac
sudo update-alternatives --config java
sudo update-alternatives --config javac

# Verify
java -version
javac -version
```

---

## 5. Install Pyenv + Python 3.10.5
```bash
# Install dependencies for building Python
sudo apt install -y make build-essential libssl-dev zlib1g-dev libbz2-dev libreadline-dev libsqlite3-dev wget curl llvm libncurses5-dev libncursesw5-dev xz-utils tk-dev libffi-dev liblzma-dev python3-openssl git

# Install pyenv
curl https://pyenv.run | bash

# Add to shell startup
echo 'export PATH="$HOME/.pyenv/bin:$PATH"' >> ~/.bashrc
echo 'eval "$(pyenv init -)"' >> ~/.bashrc
echo 'eval "$(pyenv virtualenv-init -)"' >> ~/.bashrc
source ~/.bashrc

# Install specific Python
pyenv install 3.10.5
pyenv global 3.10.5

# Verify
python --version
pip --version
```

---

## 6. Install Node.js via NVM
```bash
# Install nvm
curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.7/install.sh | bash
source ~/.bashrc

# Install Node.js LTS
nvm install --lts
nvm use --lts
nvm alias default 'lts/*'

# Verify
node -v
npm -v
```

---

## ✅ Final Setup

After completing this guide, you’ll have:
- Essential dev tools (`build-essential`, `git`, etc.)
- OpenJDK 17 & 21 (switchable via `update-alternatives`)
- Python 3.10.5 with `pyenv`
- Node.js (latest LTS) with `nvm`  
