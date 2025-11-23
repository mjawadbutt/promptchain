# Install 'Task' and Pyenv/Python on Windows 11

## 1. Allow execution of local shell scripts
```powershell
Set-ExecutionPolicy RemoteSigned -Scope CurrentUser
```

---

## 2. Install Scoop
```powershell
irm get.scoop.sh | iex
scoop --version
```

### (Optional) Add Scoop extras bucket
```powershell
scoop bucket add extras
```

---

## 3. Install Task
```powershell
scoop install task
task --version
```

---

## 4. Check if Python is already installed
```powershell
python --version
# OR
python3 --version
```

If Python is **not installed**, continue with **Pyenv** installation.

---

## 5. Install Pyenv (Windows version manager for Python)

### Manual installation
```powershell
git clone https://github.com/pyenv-win/pyenv-win.git C:\Users\Administrator\.pyenv
```

Add the following to your **user-level PATH** environment variable:
```
C:\Users\Administrator\.pyenv\pyenv-win\bin
```

Set user-level environment variables:
```
PYENV=C:\Users\Administrator\.pyenv\pyenv-win
PYENV_HOME=%PYENV%\shims
PYENV_ROOT=%PYENV%
```

Reload environment, then check:
```powershell
pyenv --version
```

---

## 6. Using Pyenv

### List installed versions
```powershell
pyenv versions
```

### List all available versions
```powershell
pyenv install --list
```

### Install Python 3.10.5
```powershell
pyenv install 3.10.5
```

👉 If you want to set it globally for all shells:
```powershell
pyenv global 3.10.5
```

👉 Or set per project:
```powershell
cd my-project
pyenv local 3.10.5
```

---

## 7. Troubleshooting

If you get an error like:
```text
:: [Info] ::  Mirror: https://www.python.org/ftp/python
:: [Installing] ::  3.10.5 ...
:: [Error] :: error installing "core" component MSI.
:: [Error] :: couldn't install 3.10.5
```

Download and install the **Visual C++ Redistributable**:
👉 [Download vc_redist.x64.exe](https://aka.ms/vs/17/release/vc_redist.x64.exe)

---

## ✅ Final Setup

After completing this guide, you’ll have:
- [Scoop](https://scoop.sh) (Windows package manager)  
- [Task](https://taskfile.dev) (task runner)  
- [Pyenv for Windows](https://github.com/pyenv-win/pyenv-win) to manage multiple Python versions  
- Python 3.10.5 installed via Pyenv  
