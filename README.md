
# 🚀 PromptChain Project Setup Guide

## ✅ Prerequisites

1. A GitHub account
2. Software: Docker Desktop 4+, Jdk 21+, IntelliJ Community 2025+

---

## 🛠️ Define OS-Level Environment Variables

Set the following environment variables in `.bashrc` (similarly in Windows if using Windows):

```bash
export GITHUB_USERNAME=<your GitHub username>
export GITHUB_PASSWORD=<your GitHub password OR a classic PAT with write:packages privilege>
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

You can run the following maven phases from command line or via IntelliJ maven plugin:

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

### ▶️ Running the application

1. Select the IntelliJ profile `DevPromptChain` and click Run ▶️ or Debug 🐞

---

## ✅ Running Integration Tests

1. Run ▶️ or Debug 🐞 the IntelliJ **profile** 'DevPromptChainIntegrationTests.testGetAppUser' to run the sample test.
2. Modify or copy the sample profile as needed.
3. If you modify the `build.properties` file then change the env-var defined in the profile accordingly.

---

## 🧠 Running in locally in 'prod-like' mode

In a non-dev environment (i.e. a prod env), PromptChain is executed by deploying it as a stack in Docker Swarm.
To run in this mode, execute the `dev-deploy-app-stack.bat` script from the command shell (Windows), or
`dev-deploy-app-stack.sh` from the bash shell terminal (Linux/Mac).

Even this can be started in debug mode. To enable, set the `RUN_IN_DEBUG_MODE` environment variable to `true`.

This will enable remote debugging at the port defined by the environment variable `DEBUG_PORT`. Setting this
env var is optional, and, if it is not set, then the default value is `5005`.

When you want to stop the app, execute the corresponding `dev-remove-app-stack` script.
