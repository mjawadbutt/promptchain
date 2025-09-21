# WSL2 + Ansible Setup Guide

This guide explains how to set up one WSL instance (**ubuntu-1**) as an Ansible master and another (**ubuntu-2**) as an inventory host, with SSH connectivity between them.

---

## 1. On `ubuntu-1` (Ansible Master)

**Update instance:**

```bash
sudo apt update && sudo apt upgrade -y
```

**Install Ansible:**

```bash
sudo apt install -y software-properties-common
sudo add-apt-repository --yes --update ppa:ansible/ansible
sudo apt install -y ansible
ansible --version
```

**Test Ansible locally:**

Create `inventory.ini`:

```ini
[local]
localhost ansible_connection=local
```

Create `test.yml`:

```yaml
- hosts: local
  tasks:
    - name: Ping localhost
      ansible.builtin.ping:
```

Run:

```bash
ansible-playbook -i inventory.ini test.yml
```

✅ Works without SSH because `ansible_connection=local`.

---

## 2. First login to `ubuntu-2`

After importing the new instance, log in:

```powershell
wsl -d ubuntu-2
```

⚠️ **Important**  
If this is your first login after enabling `systemd`, you may see:

```text
wsl: Failed to start the systemd user session for 'root'. See journalctl for more details.
```

This happens because WSL tries to launch a **systemd user session** for the default user (`root` unless changed).  
But `root` does not have a valid `systemd --user` environment, so startup fails.

We’ll fix this in the next step.

---

## 3. Configure `wsl.conf` on `ubuntu-2`

Update `/etc/wsl.conf` to set a proper default user:

```ini
[boot]
systemd=true

[network]
hostname = ubuntu-2
generateResolvConf = true

[user]
default=ansibleuser
```

Then restart the instance:

```powershell
wsl --terminate ubuntu-2
wsl -d ubuntu-2
```

✅ Now the error is gone. Even if you explicitly log in as `root` later (with `-u root`), systemd still initializes correctly because the default user is no longer `root`.

---

## 4. Keep `ubuntu-2` running with systemd

By default, WSL2 stops instances when no interactive session is open.  
To keep services (like SSH) alive after restart:

```powershell
wsl --terminate ubuntu-2
wsl -d ubuntu-2 --exec dbus-launch true
```

👉 This initializes the **systemd user session** non-interactively, preventing WSL from idling the instance.

---

## 5. Configure `ubuntu-1` (Master) for SSH resolution

`/etc/wsl.conf`:

```ini
[boot]
systemd=true

[network]
generateHosts=false
generateResolvConf=true
```

Add `/etc/profile.d/fix_hosts.sh`:

```bash
#!/bin/bash
# fix_hosts.sh — keep ubuntu-2 resolvable

# Skip if non-interactive and not SSH (cron, system jobs excluded)
[[ $- != *i* && -z "$SSH_CONNECTION" ]] && exit 0

# Skip if root in interactive shell (to avoid login annoyance)
if [ "$(id -u)" -eq 0 ] && [[ $- == *i* ]]; then exit 0; fi

POWERSHELL_EXE="/mnt/c/Windows/System32/WindowsPowerShell/v1.0/powershell.exe"
UBUNTU2_IP=$($POWERSHELL_EXE -Command "wsl -d ubuntu-2 hostname -I"              2>/dev/null | tr -d '
' | awk '{print $1}')

if [ -n "$UBUNTU2_IP" ]; then
    sudo sed -i "/ubuntu-2/d" /etc/hosts
    echo "$UBUNTU2_IP ubuntu-2" | sudo tee -a /etc/hosts >/dev/null
fi
```

Restart:

```powershell
wsl --terminate ubuntu
wsl -d ubuntu
```

---

## 6. Setup SSH Keys (`ubuntu-1` → `ubuntu-2`)

**Generate keys on `ubuntu-1`:**

```bash
ssh-keygen -t rsa -b 4096
```

**Copy to `ubuntu-2`:**

```bash
ssh-copy-id ansibleuser@ubuntu-2
```

If `ssh-copy-id` is missing:

```bash
sudo apt install -y ssh-client
```

Or copy manually:

```bash
cat ~/.ssh/id_rsa.pub | ssh ansibleuser@ubuntu-2     "mkdir -p ~/.ssh && cat >> ~/.ssh/authorized_keys"
```

---

## 7. Test SSH from Master

```bash
ssh ansibleuser@ubuntu-2
```

✅ Should connect without a password.

---

## Final Setup

- **ubuntu-1** runs Ansible and can SSH into **ubuntu-2**.  
- **ubuntu-2** stays alive with `systemd + SSH` after boot.

---
