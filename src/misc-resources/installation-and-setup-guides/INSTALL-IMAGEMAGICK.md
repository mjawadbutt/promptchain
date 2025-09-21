# Install ImageMagick from Source

## Step 1: Update System and Install Dependencies

```bash
sudo su
sudo dnf update -y
sudo dnf install -y gcc gcc-c++ make tar libjpeg-devel libpng-devel libtiff-devel freetype-devel ghostscript-devel libX11-devel libXext-devel libXt-devel
```

## Step 2: Download and Extract ImageMagick Source

```bash
cd /usr/local/bin
wget https://imagemagick.org/archive/ImageMagick.tar.gz
tar xvzf ImageMagick.tar.gz
cd ImageMagick-7.*
```

## Step 3: Build and Install

```bash
./configure
make
sudo make install
sudo ldconfig
```

## Step 4: Verify Installation

```bash
magick -version
```

You should see the installed version and enabled delegates listed.
