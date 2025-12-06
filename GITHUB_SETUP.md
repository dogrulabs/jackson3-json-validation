# GitHub Repository Setup Guide

To make `jackson3-json-validation` a professional open-source library and enable automated releases, please follow these configuration steps on your GitHub repository: [https://github.com/dogrulabs/jackson3-json-validation](https://github.com/dogrulabs/jackson3-json-validation)

## 1. General Settings
Go to **Settings > General**:
*   **Description**: "A lightweight Jackson 3 + Jakarta Bean Validation library for Java."
*   **Website**: Link to your Maven Central page (once published) or just the GitHub repo URL.
*   **Topics**: Add tags like: `java`, `json`, `validation`, `jackson`, `bean-validation`, `library`.
*   **Features**: Enable **Issues**, **Discussions** (optional but recommended for community), **Projects** (can disable if unused).

## 2. Secrets (Critical for Releases)
For the automated release workflow (`.github/workflows/release.yml`) to work, you must add secrets that allow GitHub Actions to sign and publish your code.

Go to **Settings > Secrets and variables > Actions > New repository secret**.

Add the following (you will need to have these from your local setup or Sonatype account):

| Secret Name | Value Description |
| :--- | :--- |
| `CENTRAL_USERNAME` | The **username** from your generated User Token (starts with `token-`). |
| `CENTRAL_TOKEN` | The **password** from your generated User Token. |
| `GPG_PRIVATE_KEY` | Your **ASCII-armored** GPG Private Key. Run `gpg --export-secret-keys --armor <key-id>` to get this. |
| `GPG_PASSPHRASE` | The password you used to protect your GPG key. |

> **Note:** If you haven't set up GPG or Sonatype yet, refer to the `RELEASE.md` file in the codebase for prerequisites.

## 3. Branch Protection
Protect your code integrity by preventing direct pushes to `main`.

Go to **Settings > Branches > Add branch protection rule**:
*   **Branch name pattern**: `main`
*   **Protect matching branches**: Check this.
*   **Require a pull request before merging**: Check this.
    *   *Require approvals*: Optional (useful if you have a team).
*   **Require status checks to pass before merging**: Check this.
    *   Search for and select your CI job name (e.g., `build` from your `ci.yml`). This ensures tests pass before merging!

## 4. Community Standards (Optional but Recommended)
Go to **Insights > Community Standards**.
GitHub will suggest adding:
*   **CONTRIBUTING.md**: Guidelines for how people can contribute.
*   **CODE_OF_CONDUCT.md**: Behavioral expectations.
*   **Issue Templates**: To standardise bug reports.

## 5. Publishing
Once you have configured the Secrets (Step 2), you can perform a release by simply pushing a tag:
```bash
git tag v1.0.0
git push origin v1.0.0
```
This will trigger the action to build, sign, and publish to Maven Central.
