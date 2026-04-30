# University Management System (UMS) - JavaFX Implementation

## Project Overview
This repository contains a professional University Management System built with JavaFX. It follows a clean MVC-inspired architecture and supports both a direct PostgreSQL database connection and a **Mock Data Mode** for offline development.

## Architecture
- **Package Base**: `ASU.CAIE`
- **Frontend**: JavaFX (FXML + CSS)
- **Backend**: Java Service Layer + Data Access Objects (DAOs)
- **Database**: PostgreSQL (managed via `dotenv-java`)
- **Version Control**: Git (Branch-based workflow: `develop` for features, `main` for releases)

## Key Features
- **Role-Based Dashboards**: Customized views for Students, Instructors, Admins, and Parents.
- **Grading System**: Interactive grading interface for instructors with automatic letter grade calculation.
- **Academic Tracking**: Comprehensive grade viewing and cumulative GPA calculation for students.
- **Performance Analytics**: Visual trend charts (Line and Bar) for academic performance monitoring.
- **Session Management**: Centralized singleton-based user session handling.
- **Theme Support**: Unified aesthetic styling using a shared CSS system.

## Configuration & Development Mode

### Crucial Setup: The `.env` File
The application requires a `.env` file in the project root to connect to the database. 
Template:
```env
DB_URL=jdbc:postgresql://localhost:5432/ums_db
DB_USER=your_username
DB_PASSWORD=your_password
```

### 🔌 Mock Data Mode (Bypassing the DB)
To develop or test without an active database connection, you must use the `Mock Mode` toggle.
1. Locate `ASU.CAIE.config.AppConfig`.
2. Ensure `USE_MOCK_DATA` is set to `true`:
   ```java
   public static boolean USE_MOCK_DATA = true;
   ```
3. In the application UI, you can toggle this mode in real-time using the 🔌 button in the header of any dashboard.

## Development Workflow
- **Branching**: Always branch off `develop`. Never push directly to `main`.
- **Styling**: All global styles should be added to `src/main/resources/ASU/CAIE/css/styles.css`.
- **Scene Switching**: Use `ASU.CAIE.util.SceneManager.switchTo("ViewName.fxml")` for all transitions.

## Requirements
- Java 23+
- JavaFX SDK 17+
- Maven/Gradle (as configured in the project root)

---
*Built with precision for the University Management System team.*
