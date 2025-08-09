# Git Setup Guide

## 1. Set Default Branch to `main`

In Git, the branch that gets created by default when running `git init` is named **`master`**.  
However, when a repository is created via **GitHub.com**, the default branch is named **`main`**.  
To follow a consistent naming pattern, update the **global Git config** and add or modify the `[init]` section:

```ini
[init]
    defaultBranch = main
```

**Tip:**  
You can also use the `git config` command instead of editing the file directly:

```bash
git config --global init.defaultBranch main
```

Both methods work fine.

---

## 2. Check and Configure Git Credential Manager

When Git is installed on Windows (or macOS or Linux), it **may** install a global credentials helper  
(**Git Credential Manager Core**) by default.

### Check if Git Credential Manager Core is Installed
```bash
git credential-manager-core --version
```
You should see `manager-core` in the output.  
GCM Core is included by default since **Git v2.28**.

---

### Check if Credential Helper is Enabled Globally
```bash
git config --global credential.helper
```

If not enabled globally, check at the repository level by navigating to your project’s root directory  
(where the `.git` folder is) and running:
```bash
git config credential.helper
```

---

### Enable Git Credential Manager Core Globally
If it’s not enabled:
```bash
git config --global credential.helper manager-core
```

---

## 3. About Git Credential Manager

**Official Name:** Git Credential Manager (GCM)  
It is a cross-platform credentials manager that uses OS-native secure storage:

- **Windows:** Windows Credential Manager
- **macOS:** macOS Keychain
- **Linux:** GNOME Keyring or `libsecret`

**Key Features:**
- Encrypts stored credentials.
- Automatically prompts for login (via browser or terminal) when needed.
- Works with:
    - GitHub (supports fine-grained PATs, OAuth, GPG tokens)
    - Azure DevOps
    - Bitbucket
    - GitLab (with some limitations)
- Avoids sending your password over the network or storing it in plain text.

---

## 4. IDEs and Git Authentication

No IDE-specific configuration (e.g., IntelliJ) is required for Git authentication.  
Authentication is handled entirely by **Git CLI**.

**Important for IntelliJ Users:**
- Leave the *"Use credential helper"* checkbox **unchecked** in IntelliJ’s Git settings.
    - Checking it makes IntelliJ use its own credential manager, which is **not recommended**.
- Let Git handle authentication using its configured credential manager.

---
