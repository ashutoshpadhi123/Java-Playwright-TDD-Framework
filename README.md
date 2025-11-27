# UI Test Automation Framework – Java + Playwright

This project automates the login workflow for **OrangeHRM demo** using
**Java + Playwright + TestNG**, with parallel execution, screenshots on failure,
environment-based configuration, and Jenkins CI support.

## Tech Stack
- Java 17
- Playwright for Java
- TestNG
- Maven
- Jenkins (example pipeline via `Jenkinsfile`)

## Repository Layout (per assignment)

- `tests/` – logical folder for test cases (Java tests live in `src/test/java/tests`)
- `framework/` – reusable framework utilities & page objects (Java code in `src/test/java/framework`)
- `ci/` – CI pipeline configuration (initially GitHub Actions example)
- `docs/` – basic documentation
- `Jenkinsfile` – Declarative pipeline for Jenkins
- `.env`, `.env.jenkins` – config/env files

## Configuration via Env Loader

The `framework.utils.ConfigLoader` resolves config keys in this order:

1. Java system properties: `-DKEY=value`
2. Environment variables: `KEY=value`
3. `.env` file values (and optional `.env.<ENV>` overlay)

Example `.env` (already included):

```env
BASE_URL=https://opensource-demo.orangehrmlive.com/
USERNAME=Admin
PASSWORD=admin123
HEADLESS=true
```

In Jenkins, we use:

```groovy
environment {
  ENV = "jenkins"
  HEADLESS = "true"
  BASE_URL = "https://opensource-demo.orangehrmlive.com/"
  USERNAME = "Admin"
  PASSWORD = "admin123"
}
```

## Screenshots on Failure

- Implemented in `framework.listeners.TestListener`.
- On any TestNG test failure, a **full-page screenshot** is saved to `./screenshots/`.
- Listener is wired via `testng.xml`.

## Parallel Execution

- Enabled at TestNG suite level in `testng.xml`:

  ```xml
  <suite name="Playwright UI Suite" parallel="methods" thread-count="4">
  ```

- `PlaywrightFactory` and `BaseTest` use `ThreadLocal` to ensure each test
  method has its own isolated Playwright `Page` instance.

## How to Run Locally

1. Ensure Java 17 and Maven are installed.
2. Optionally adjust `.env` values.
3. Run:

   ```bash
   mvn test
   ```

## Running in Jenkins

1. Create a new **Pipeline** job.
2. Point it to this Git repository.
3. Jenkins will automatically use the `Jenkinsfile`:
   - Checkout
   - `mvn clean test`
   - Archive screenshots (if any)
   - Publish TestNG JUnit reports

## Implemented Test Cases

- ✅ Valid login → verifies dashboard visibility
- ✅ Invalid login → verifies "Invalid credentials" error
