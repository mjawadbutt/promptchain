# Building QEMU 9.2.4 from Source (CentOS/RHEL/AlmaLinux)

This guide walks you through compiling and installing **QEMU 9.2.4** from source, specifically the **x86_64-linux-user** target (useful for running x86 binaries under emulation, e.g., on ARM64 EC2 instances).

---

## Step 1: Install Dependencies

Make tomcat user part of the 'wheel' group so it can run all commands via sudo so all steps can be run as tomcat
user (otherwise there will be back n forth between su and tomcat and make steps complex unnecessarily).
The group membership can be revoked, after completion of all steps, if needed.

```bash
sudo su - tomcat
sudo yum groupinstall "Development Tools" -y
sudo yum install glib2-devel pixman-devel zlib-devel python3 python3-pip -y
```

---

## Step 2: Download QEMU 9.2.4 Source

```bash
cd /home/tomcat
wget https://download.qemu.org/qemu-9.2.4.tar.xz
tar -xf qemu-9.2.4.tar.xz
cd qemu-9.2.4
```

---

## Step 3: Install Python Packages

```bash
pip3 install --user tomli ninja
```

🔍 To double-check that Python is picking up your user-installed packages:

```bash
python3 -m site --user-site
```

You should see something like:

```
/home/tomcat/.local/lib/python3.9/site-packages
```

Then verify installation:

```bash
python3 -m pip show tomli ninja
```

---

## Step 4: Configure Build

```bash
./configure --target-list=x86_64-linux-user --disable-docs
```

---

## Step 5: Build QEMU

```bash
cd build
make -j$(nproc)
```

---

## Step 6: Check Meson Version in QEMU Virtualenv

```bash
# Activate QEMU's internal Python virtualenv
source build/pyvenv/bin/activate

# Check Meson version
meson --version

# Deactivate when done
deactivate
```

---

## Step 7: Meson Setup (ONLY FOR QEMU VERSIONS < 9)

If you need to override Meson:

```bash
source build/pyvenv/bin/activate
pip install --force-reinstall meson==1.9.0
meson --version   # should print 1.9.0
deactivate
```

⚠️ Any recent Meson >= 1.2.1 will also work. (1.9.0 is fine.)

---

## Step 8: Install QEMU

```bash
sudo make install
```

### Verify Installation

```bash
qemu-x86_64 --version
# OR:
/usr/local/bin/qemu-x86_64 --version
```

Expected output:

```
qemu-x86_64 version 9.2.4
Copyright (c) 2003-2024 Fabrice Bellard and the QEMU Project developers
```

---

## Step 9: Prepare Ubuntu RootFS

```bash
mkdir -p ~/ubuntu-rootfs
cd ~/ubuntu-rootfs
wget https://partner-images.canonical.com/core/jammy/current/ubuntu-jammy-core-cloudimg-amd64-root.tar.gz
tar -xzf ubuntu-jammy-core-cloudimg-amd64-root.tar.gz
```

### Check RootFS Binaries

```bash
file bin/bash
```

Should show something like:

```
ELF 64-bit LSB pie executable, x86-64, dynamically linked, interpreter /lib64/ld-linux-x86-64.so.2
```

---

## Step 10: Fix Absolute Symlinks

Create the following script to convert symlinks to relative ones:

```bash
cat > make-rootfs-symlinks-relative.sh <<'EOF'
#!/bin/bash
# Converts all symlinks inside a rootfs to relative paths

set -euo pipefail

ROOTFS="$1"

if [[ -z "$ROOTFS" || ! -d "$ROOTFS" ]]; then
    echo "Usage: $0 <path-to-rootfs>"
    exit 1
fi

echo "Making all symlinks in $ROOTFS relative..."

find "$ROOTFS" -type l | while read -r link; do
    target=$(readlink "$link")
    if [[ "$target" != /* ]]; then
        continue
    fi
    parent_dir=$(dirname "$link")
    rel=$(realpath --relative-to="$parent_dir" "$ROOTFS$target" 2>/dev/null || true)
    if [[ -n "$rel" ]]; then
        rm "$link"
        ln -s "$rel" "$link"
        echo "Fixed: $link -> $rel"
    fi
done

echo "All symlinks are now relative."
EOF
```

And run it:

```bash
chmod +x make-rootfs-symlinks-relative.sh
sudo ./make-rootfs-symlinks-relative.sh ~/ubuntu-rootfs
```

---

## Step 11: Run Your Binary Under QEMU

```bash
qemu-x86_64 -L ~/ubuntu-rootfs /ies/data/docmaint/reqloader/reqloader reqloader.config.xml
# OR:
/usr/local/bin/qemu-x86_64 -L ~/ubuntu-rootfs /ies/data/docmaint/reqloader/reqloader reqloader.config.xml
```

✅ You now have **QEMU 9.2.4** installed and a working Ubuntu glibc-based rootfs for running x86_64 binaries on ARM64.
