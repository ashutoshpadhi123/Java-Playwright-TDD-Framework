# Framework Overview

## Design

- **PlaywrightFactory**: Central place for initializing and closing Playwright,
  Browser, and Page instances.
- **BaseTest**: Handles setup/teardown for all tests.
- **Page Objects**:
  - `LoginPage` – login interactions
  - `DashboardPage` – minimal dashboard verification & search stub
- **Tests**:
  - `ValidLoginTest` – valid credentials scenario
  - `InvalidLoginTest` – invalid credentials scenario

## Flow

1. TestNG starts a test from `testng.xml`.
2. `BaseTest` creates a new Playwright `Page`.
3. Tests navigate to the OrangeHRM URL, use page objects to perform actions.
4. Assertions validate the outcome (URL/content/selectors).
5. `BaseTest` closes Playwright in teardown.
