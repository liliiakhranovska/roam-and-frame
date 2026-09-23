# ADR-0004: One Schema per Module, No Cross-Schema Access

## Status

Accepted

## Context

The Core API Application needs a persistence-layer rule that keeps each module's data private to that
module. All modules share one database instance, so without an explicit boundary there, nothing stops
one module's code from querying another module's tables directly

## Decision

Each module owns its own Postgres schema and its own set of tables within that schema, accessed only
through its own repository classes. Cross-module data needs are served through the owning module's
application-layer interface, never through direct SQL joins or queries across schema boundaries

## Consequences

- Schema-per-module makes ownership explicit 
- Enforcement relies on code review and repository structure, not database-level grants
- If a module is later split into an independent service, its data is already isolated in its own
  schema
