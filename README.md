
# 🚀 PromptChain Project Setup Guide

## ✅ Prerequisites

1. A GitHub account
2. A **GitHub Classic Personal Access Token (PAT)** with the `write:packages` privilege

---

## 🛠️ Define OS-Level Environment Variables

Set the following environment variables in your terminal:

```bash
export GITHUB_USERNAME=mjawadbutt
export GITHUB_PASSWORD=<your GitHub classic PAT with write:packages privilege>
```

---

## 🧾 Configure Maven Credentials (`~/.m2/settings.xml`)

Create a file named `settings.xml` in your `~/.m2` directory with the following content:

```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <servers>
    <server>
      <id>github-promptchain</id>
      <username>${GITHUB_USERNAME}</username>
      <password>${GITHUB_PASSWORD}</password>
    </server>
  </servers>
</settings>
```

> 📝 **Note:**  
> This config is used for publishing JAR artifacts to GitHub Packages (a Maven-compatible repository).  
> It **does not** support Docker image registries.

---

## 🐳 Docker Registry Authentication

For publishing Docker images to `ghcr.io` (GitHub Container Registry), we use **shell scripts** executed via the `exec-maven-plugin`.

These scripts reuse the same environment variables (`GITHUB_USERNAME`, `GITHUB_PASSWORD`) for authenticating with GHCR.

---

## 📥 Clone the Repository

```bash
cd ~/projects/personal
git clone https://github.com/<your-username>/promptchain.git
cd promptchain
```

---

## 🔧 Maven Build Options

Make sure **Docker Desktop** (or the Docker Daemon) is running.

You can use the following commands:

### 💡 Command Line

| Command               | Description                                                                 |
|-----------------------|-----------------------------------------------------------------------------|
| `mvn compile`         | Compiles source files (no JAR created)                                      |
| `mvn package`         | Compiles and packages a JAR artifact                                        |
| `mvn install`         | Packages the JAR, installs it in local `.m2`, and builds a Docker image     |
| `mvn deploy`          | Also publishes JAR to GitHub Packages and Docker image to `ghcr.io`         |
| `mvn clean package`   | (Optional) Clean target dir before packaging                                |

---

## 🧠 Running from IntelliJ IDEA

### ▶️ Application Run Configuration

1. Go to **Run → Edit Configurations**
2. Create a new **Application** profile:
    - **Name**: `PromptChain`
    - **Before Launch**: Add Maven goal `clean compile`
    - **Main class**:
      ```
      com.promptwise.promptchain.PromptChainApplication
      ```
    - **Program arguments**:
        - Windows:
          ```
          --spring.profiles.active=localWin
          ```
        - Linux/macOS:
          ```
          --spring.profiles.active=localLinux
          ```

3. Save and run with ▶️ or debug with 🐞

---

## ✅ Running Integration Tests

1. Open any `*IntegrationTests.java` file
2. Right-click a method or class → **Run**
3. Stop execution (this creates a run profile)
4. Edit the JUnit profile:
    - **Program arguments**:
        - Windows:
          ```
          --spring.profiles.active=localWin
          ```
        - Linux/macOS:
          ```
          --spring.profiles.active=localLinux
          ```

5. Use dropdowns to select tests
6. Run ▶️ or Debug 🐞
