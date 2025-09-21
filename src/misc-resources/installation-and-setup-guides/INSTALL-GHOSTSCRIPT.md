# Ghostscript Installation & Usage Guide

## 📥 Download Ghostscript
Download releases from the official repository:  
👉 [Ghostscript Releases (ArtifexSoftware)](https://github.com/ArtifexSoftware/ghostpdl-downloads/releases?page=4)

---

## 🔍 Check Installation

```bash
# Check if Ghostscript is installed
gs --version
```

---

## 📦 Manage Ghostscript with DNF (RHEL/CentOS/Fedora)

```bash
# List all available Ghostscript versions in repos
dnf list --available ghostscript

# Get details of each available version
dnf info ghostscript

# Install Ghostscript
sudo dnf install ghostscript
```

---

## 🎨 LZW Compression with Ghostscript

To specify **LZW compression** as the output method:

- Use a **TIFF-compatible output device** (`tiff24nc`, `tiffgray`, etc.)
- Set compression type with `-dCompression=LZW`

### ✅ Example Command

```bash
gs -sDEVICE=tiff24nc -sOutputFile=output.tiff -dCompression=LZW input.pdf
```

### 📌 Explanation
- `-sDEVICE=tiff24nc` → 24-bit color TIFF output device  
- `-dCompression=LZW` → Enables **LZW compression**  
- `-sOutputFile=output.tiff` → Sets output file name  
- `input.pdf` → Input file to process  

---

## 📋 Supported TIFF Devices (that work with `-dCompression`)
- `tiffgray` → Grayscale  
- `tiff24nc` → 24-bit RGB  
- `tiff32nc` → 32-bit RGBA  
- `tiffsep`, `tiffsep1` → Separation color  

⚠️ Note: `tiffg4` (monochrome CCITT Group 4) does **not** support LZW.
