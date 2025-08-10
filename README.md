[![Maven Build and Deploy](https://github.com/mjawadbutt/promptchain/actions/workflows/prompthub-github-workflow.yml/badge.svg)](https://github.com/mjawadbutt/promptchain/actions/workflows/prompthub-github-workflow.yml)
# 🚀 PromptChain Project Setup Guide

## ✅ Prerequisites

1. A **GitHub account**
2. Software:
    - **Docker Desktop** 4+
    - **JDK** 21+
    - **IntelliJ Community** 2025+

---

## 📥 Clone the Repository

```bash
cd ~/projects/personal
git clone https://github.com/mjawadbutt/promptchain.git
cd promptchain
```

---

## 🔧 Maven Build Options

Make sure **Docker Desktop** (or the Docker Daemon) is running.

You can run the following Maven phases from the **command line** or via the **IntelliJ Maven plugin**:

### 💡 Command Line

| Command               | Description                                                                 |
|-----------------------|-----------------------------------------------------------------------------|
| `mvn compile`         | Compiles source files (no JAR created)                                      |
| `mvn package`         | Compiles and packages a JAR artifact                                        |
| `mvn install`         | Packages the JAR, installs it in local `.m2`, and builds a Docker image     |
| `mvn deploy`          | Publishes the JAR to GitHub Packages and the Docker image to `ghcr.io`      |
| `mvn clean package`   | Cleans target dir before packaging (optional)                               |

---

## 🧠 Running from IntelliJ IDEA

### ▶️ Running the Application

1. Select the IntelliJ **profile** `DevPromptChain`.
2. Click **Run ▶️** or **Debug 🐞**.

---

## ✅ Running Integration Tests

1. Run ▶️ or Debug 🐞 the IntelliJ **profile** `DevPromptChainIntegrationTests.testGetAppUser` to run the sample test.
2. Modify or copy the sample profile as needed.
3. If you modify the `build.properties` file, update the environment variable in the profile accordingly.

---

## 🧠 Running Locally in 'Prod-like' Mode

In a **non-dev environment** (i.e., production-like), PromptChain runs as a **stack in Docker Swarm**.

- On **Windows**: run `dev-deploy-app-stack.bat`
- On **Linux/Mac**: run `dev-deploy-app-stack.sh`

**Debug Mode:**
- Enable by setting the environment variable:
  ```bash
  export RUN_IN_DEBUG_MODE=true
  ```
- Default debug port: **5005** (change by setting `DEBUG_PORT`)

**Stopping the App:**
- Run the corresponding `dev-remove-app-stack` script.

---

## 📦 Publishing Artifacts Manually

The Maven build creates both:
- A **JAR artifact**
- A **Docker Image**

Although CI (GitHub Actions) publishes these automatically, you can manually publish them by running:

```bash
mvn deploy
```

**Authentication Setup:**

### 1️⃣ Create a Personal Access Token (PAT)
- Type: **Classic**
- Scope: `write:packages`

### 2️⃣ Set Environment Variables
For Linux/Mac (add to `.bashrc`):
```bash
export GITHUB_USERNAME=<your GitHub username>
export GITHUB_PAT=<your classic PAT>
```

### 3️⃣ Configure Maven Credentials (`~/.m2/settings.xml`)
```xml
<settings xmlns="http://maven.apache.org/SETTINGS/1.2.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.2.0 https://maven.apache.org/xsd/settings-1.2.0.xsd">
  <servers>
    <server>
      <id>github-promptchain</id>
      <username>${GITHUB_USERNAME}</username>
      <password>${GITHUB_PAT}</password>
    </server>
  </servers>
</settings>
```
> 📝 **Note:** This config is for **publishing JAR artifacts** to GitHub Packages.  
> It does **not** handle Docker image registries.

### 4️⃣ Setup GHCR Authentication
For Docker images to `ghcr.io`, the `exec-maven-plugin` runs shell scripts that reuse the same variables:
- `GITHUB_USERNAME`
- `GITHUB_PAT`

No extra configuration is needed.

---

## Running the Client (Next.js)

The frontend for this project is built with **Next.js** and lives inside the Spring Boot project at:

```
src/main/resources/static/client/
```

### 1. Navigate to the client folder
```bash
cd src/main/resources/static/client
```

### 2. Install dependencies
Make sure you have **Node.js 18+** and **npm** installed.  
Then run:
```bash
npm install
```

### 3. Run in development mode
This will start the Next.js development server on port **3000**:
```bash
npm run dev
```
The app will be available at:
```
http://localhost:3000
```
> In development, the client runs separately from the Spring Boot backend.

### 4. Build for production
To generate the production build:
```bash
npm run build
```
Then start it locally with:
```bash
npm start
```

### 5. Serving with Spring Boot
When deployed, the built client files (`.next` or `out`) are served by Spring Boot from:
```
src/main/resources/static/client/
```