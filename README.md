# Roam & Frame

## Prerequisites (one-time)

- **Docker runtime: Colima**
- **Compose plugin**
- **First Colima start** - CPU/memory are remembered, later a plain `colima start` is enough
  ```sh
  colima start --cpu 2 --memory 3
  ```

## IntelliJ run configuration (`Application`)

- **Working directory: `$PROJECT_DIR$`** - Spring looks for `compose.yaml` in the working directory,
  and it lives in the repo root
- **VM options: `-Duser.timezone=UTC`**
- **Env variables: specify JWT_SECRET** (Spring Boot configuration) and **DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD** (Script configuarion)

## Local start

1. `colima start` - starts the Docker engine (needed once per session / after reboot)
2. Run `Application` in IntelliJ - Spring Boot Docker Compose support runs `docker compose up`
   for `compose.yaml`, waits for Postgres, and configures the datasource automatically
3. Check: `curl localhost:8080/actuator/health` → `{"status":"UP"}` (includes a DB check)
4. API docs: http://localhost:8080/swagger-ui.html (raw OpenAPI JSON: http://localhost:8080/v3/api-docs)

Stopping the app also stops the Postgres container (default `start-and-stop` lifecycle).
Data survives in the `pgdata` volume.

## Local database

| Setting  | Value          |
|----------|----------------|
| Host     | localhost:5432 |
| Database | roamandframe   |
| User     | roamandframe   |
| Password | roamandframe   |

## Database schema and sample data

**Schema** is managed by Flyway. Migrations live in `src/main/resources/db/migration`
(`V<n>__description.sql`) and are applied automatically when the app starts. Never edit a migration
that has already run - add a new `V<n+1>__...sql` instead. Hibernate only validates the schema
(`ddl-auto=validate`), it never changes it.

Sample data:
- `database/seed/seed.sh` - loads the CSVs; safe to run repeatedly (existing rows are skipped)
- `database/cleanup/cleanup.sh` - deletes **all** application data, keeps the schema

Full reset: `docker compose down -v`, then start the app (Flyway recreates the schema), then `seed.sh`.

When adding a table in a new migration, add it to `database/cleanup/cleanup.sql` as well.

Useful commands (run from the repo root):
- `docker compose ps` - is the container running?
- `docker compose logs postgres` - Postgres logs, first place to look when the app can't connect
- `docker compose down` - stop the container (data kept); `down -v` also deletes the data volume
- `docker compose exec postgres psql -U roamandframe -d roamandframe` - connect to the running container

