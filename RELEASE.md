# Releasing to Maven Central

This guide describes how to release a new version of `jackson3-json-validation` to Maven Central.

## Prerequisites

1.  **Central Portal Account**: Sign in to [central.sonatype.com](https://central.sonatype.com) (use "Sign in with GitHub").
2.  **Namespace Verification**: In the portal, verify you own `io.github.dogrulabs` (the portal will guide you to create a temporary repo or DNS record).
3.  **User Token**: Generate a User Token from your Account settings (top right menu > View Account > Generate User Token).
4.  **GPG Keys**: You need a GPG key pair to sign artifacts.
5.  **GitHub Secrets**:
    *   `CENTRAL_USERNAME`: The username from your generated token.
    *   `CENTRAL_TOKEN`: The password from your generated token.
    *   `GPG_PRIVATE_KEY`: Your ASCII-armored private key.
    *   `GPG_PASSPHRASE`: Passphrase for your GPG key.

## Release Process

### 1. Update Version

Update the version in `pom.xml` to the release version (remove `-SNAPSHOT`):

```bash
mvn versions:set -DnewVersion=1.0.0
```

Commit the change:

```bash
git add pom.xml
git commit -m "Release v1.0.0"
```

### 2. Tag the Release

Create a git tag starting with `v`:

```bash
git tag v1.0.0
git push origin v1.0.0
```

### 3. Automated Release

Pushing the tag triggers the **Release to Maven Central** GitHub Action, which will:
1.  Build and test the project.
2.  Sign artifacts with GPG.
3.  Deploy to the Sonatype OSSRH staging repository.
4.  Automatically close and release the repository (if configured in pom.xml).

### 4. Next Development Iteration

Update `pom.xml` to the next snapshot version:

```bash
mvn versions:set -DnewVersion=1.0.1-SNAPSHOT
```

Commit and push:

```bash
git add pom.xml
git commit -m "Prepare for next development iteration"
git push origin main
```

## Manual Release (Fallback)

If the CI fails, you can release locally:

```bash
export GPG_TTY=$(tty)
mvn clean deploy -P release
```

(Ensure you have `settings.xml` configured with your credentials).
