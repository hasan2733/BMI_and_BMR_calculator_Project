# 🤝 Contribution Guide

Thank you for your interest in contributing to **BMI_and_BMR_calculator_Project**! 🎉
This guide outlines the standard workflow to ensure all contributions are integrated **smoothly, safely, and consistently**.

---

## 🧰 1. Setup and Preparation

### 1.1 Fork the Repository

First, create your own copy of the project:

1. Go to the main repository on GitHub:
   **BMI_and_BMR_calculator_Project**
2. Click the **Fork** button (top-right corner).
3. This creates a personal copy under your GitHub account.

---

### 1.2 Clone Your Fork

Clone your forked repository to your local machine:

```bash
git clone https://github.com/YourUsername/BMI_and_BMR_calculator_Project.git
```

Navigate into the project directory:

```bash
cd BMI_and_BMR_calculator_Project
```

---

### 1.3 Set Up the Upstream Remote

The **upstream** remote points to the original repository and allows you to stay updated.

Add the upstream repository (replace `OriginalProjectOwner` with the actual owner name):

```bash
git remote add upstream https://github.com/OriginalProjectOwner/BMI_and_BMR_calculator_Project.git
```

Verify your remotes:

```bash
git remote -v
```

You should see:

* `origin` → your fork
* `upstream` → main project repository

---

## 🔄 2. Contribution Workflow

Follow these steps every time you work on a new feature or bug fix.

---

### 2.1 Sync with Upstream

Before starting, always sync your local repository with the latest changes:

```bash
# Switch to main branch
git checkout main

# Fetch updates from upstream
git fetch upstream

# Merge upstream changes into local main
git merge upstream/main
```

---

### 2.2 Create a New Feature Branch

⚠️ **Never work directly on the `main` branch.**
Create a descriptive branch for your task:

```bash
# Example: Adding BMR unit tests
git checkout -b feature/bmr-unit-tests
```

Branch naming examples:

* `feature/diet-chart-ui`
* `bugfix/bmi-calculation-error`
* `docs/update-readme`

---

### 2.3 Make Your Changes

* Write clean, readable code
* Follow existing coding style
* Ensure the app builds and runs correctly in **Android Studio**
* Test your changes thoroughly

---

### 2.4 Commit Your Changes

Use meaningful and descriptive commit messages:

```bash
git add .
git commit -m "feat: Add comprehensive unit tests for BMR calculation"
```

**Commit Message Guidelines:**

* `feat:` for new features
* `fix:` for bug fixes
* `docs:` for documentation updates
* `refactor:` for code restructuring

---

## 🚀 3. Submitting the Contribution

### 3.1 Push to Your Fork

Push your feature branch to your GitHub fork:

```bash
git push origin feature/bmr-unit-tests
```

---

### 3.2 Create a Pull Request (PR)

1. Go to your fork on GitHub
2. Click **Compare & pull request**
3. Verify details:

   * **Base Repository:** OriginalProjectOwner/BMI_and_BMR_calculator_Project
   * **Head Repository:** YourUsername/BMI_and_BMR_calculator_Project
4. Write a clear title and description:

   * What you changed
   * Why the change is needed
5. Click **Create pull request**

---

## ✅ 4. Review and Merge

* The project maintainer will review your PR
* If approved, it will be merged into the `main` branch
* If changes are requested:

  * Update your code
  * Commit the fixes
  * Push to the same branch
  * The PR will update automatically

---

## 📌 Notes

* Keep PRs focused and small
* Follow Android & Java best practices
* Be respectful and constructive in discussions

---

🎉 **Thank you for contributing to BMI_and_BMR_calculator_Project!**
Your efforts help make this project better for everyone.
