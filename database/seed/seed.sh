set -e
cd "$(dirname "$0")/../.."
for file in database/seed/0*.sql; do
  psql "postgresql://${DB_USER}:${DB_PASSWORD}@${DB_HOST}:${DB_PORT}/${DB_NAME}" -v ON_ERROR_STOP=1 -f "$file"
done