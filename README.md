# GameBuddy — Campus Sports Matchmaker

Quick local setup (macOS friendly):

Prereqs:
- Java 17
- Maven 3.6+
- Docker (to run PostgreSQL easily)

1) Start PostgreSQL with docker-compose (recommended)
   docker compose up -d

   This uses:
   - DB: jdbc:postgresql://localhost:5432/gamebuddy
   - username: gameuser
   - password: gamepass

2) Build & run the app
   mvn clean package
   mvn spring-boot:run

3) Open in the browser:
   http://localhost:8080

Default file uploads are saved to `uploads/` in the project root (created automatically).

Notes:
- Tables are auto-created/updated by Hibernate (spring.jpa.hibernate.ddl-auto=update). You will see tables update as users and groups are created.
- If you prefer to run with a local Postgres instance, update `src/main/resources/application.properties`.
- If you want a fresh DB, stop docker compose, remove the volume: `docker compose down -v` and restart.

Project features:
- Sign up (upload student ID)
- Login / session
- Create group (auto-generated code)
- Browse groups with filters
- Join by invite code
- Group details with participant list
- Logout

If you want, I can:
- Push this to a GitHub repo
- Add Dockerfile for the app itself
- Add tests or Flyway migrations

Enjoy!