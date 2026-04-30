# Merge Report — login + db → develop → main
Date: 2026-04-30

## What Was Merged
- db branch: Database layer logic, DAO, DatabaseManager, PasswordUtils, and schema file (`DDL.sql`).
- login branch: Authentication UI (LoginForm, SignupForm, RightPanel) and existing POM dependencies.

## Conflicts Found and How They Were Resolved
| File | Conflict Description | Resolution |
|------|---------------------|------------|
| `Main.java` | Both branches modified startup sequence | Combined: DB init test runs first, login scene loads second |
| `User.java` | Differing fields and getters/setters (DB used capitalized methods, Login used non-capitalized) | Merged all fields and methods from both branches. Kept empty constructor (for DB) and parameterized constructor (for Login). |
| `UserDao.java` | Differing `createUser` arguments; DB branch had additional methods (`GetUser`, `VerifyUserPassword`) | Merged both `createUser` method variants to ensure full compatibility. Preserved `GetUser` and `VerifyUserPassword`. |
| `RightPanel.java` | UI relied on a mock HTTP API call | Rewrote `doLogin` and `doSignup` to use the merged `UserDao` logic in background tasks. |

## Files Added to develop (not in main before)
The manual merge was done by pulling all files from the `University-Management-System-DB-Util` directory into the main workspace. 
All files were fully integrated into `src/`.

## Dependencies Added
All required dependencies (`postgresql`, `dotenv-java`, `jbcrypt`, `javafx-controls`, `javafx-fxml`) were already merged correctly in the workspace `pom.xml`.

## Known Issues After Merge
No known issues. Note: `SessionManager` was not present in the workspace, so it was not created as per explicit instruction. Authentication logic in `RightPanel.java` directly interacts with `UserDao`.

## What Each Team Should Verify
- DB team: Verify the integration of `UserDao` inside the UI components correctly passes data and does not block the UI thread.
- Frontend/Login team: Test the Registration and Login UI to ensure successful DB transactions.

## Next Steps
- New feature branches should be created from: develop
- Do NOT branch from main going forward
