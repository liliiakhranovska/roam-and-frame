# Roam & Frame

## Prerequisites (one-time)

- **Docker runtime: Colima**
- **Compose plugin**
- **First Colima start** - CPU/memory are remembered, later a plain `colima start` is enough
  ```sh
  colima start --cpu 2 --memory 3
  ```

## IntelliJ run configuration (`CoreApiApplication`)

- **Working directory: `$PROJECT_DIR$`** - Spring looks for `compose.yaml` in the working directory,
  and it lives in the repo root
- **VM options: `-Duser.timezone=UTC`**

## Local start

1. `colima start` - starts the Docker engine (needed once per session / after reboot)
2. Run `CoreApiApplication` in IntelliJ - Spring Boot Docker Compose support runs `docker compose up`
   for `compose.yaml`, waits for Postgres, and configures the datasource automatically
3. Check: `curl localhost:8080/actuator/health` → `{"status":"UP"}` (includes a DB check)

Stopping the app also stops the Postgres container (default `start-and-stop` lifecycle).
Data survives in the `pgdata` volume.

## Local database

| Setting  | Value          |
|----------|----------------|
| Host     | localhost:5432 |
| Database | roamandframe   |
| User     | roamandframe   |
| Password | roamandframe   |

Useful commands (run from the repo root):
- `docker compose ps` - is the container running?
- `docker compose logs postgres` - Postgres logs, first place to look when the app can't connect
- `docker compose down` - stop the container (data kept); `down -v` also deletes the data volume
