# Contributing to BookSeva

Welcome to the BookSeva team! To ensure a smooth development process and avoid merge conflicts, please adhere to the following workflow rules.

## Branch Naming Convention

We use a **branch-per-module** workflow. All development must occur on the assigned branch for your specific module. Do not push directly to `main`.

Please use the following branches:
- `feature/auth-admin-user`
- `feature/catalog-book-search-cart`
- `feature/transactions-order-payment-invoice`
- `feature/engagement-dispute-review-report`
- `feature/core-common-config-infra`

## Commit Message Format

We follow a semantic commit message format:
`type(module): description`

**Types**: 
- `feat`: A new feature
- `fix`: A bug fix
- `docs`: Documentation only changes
- `style`: Changes that do not affect the meaning of the code (white-space, formatting, etc)
- `refactor`: A code change that neither fixes a bug nor adds a feature
- `test`: Adding missing tests or correcting existing tests

**Examples**:
- `feat(auth): add OTP verification endpoint`
- `fix(cart): resolve issue with overlapping cart totals`

## Merge Conflict Avoidance Rule

**STRICT RULE**: You are only allowed to modify files inside your assigned module's directory (e.g., `backend/src/main/java/com/bookseva/auth/` and `frontend/src/pages/Auth/`).
- If you need to make changes to shared files (like `application.yml`, `api.js`, or global CSS), you must coordinate with the team in Slack/Discord before committing to avoid overriding someone else's work.

## Pull Request (PR) Process

1. When your module feature is complete, push your branch to GitHub.
2. Open a Pull Request (PR) against the `main` branch.
3. **Requirement**: At least **one team member** must review and approve your PR before it can be merged.
4. Once approved, select "Squash and merge" to keep the `main` branch commit history clean.
