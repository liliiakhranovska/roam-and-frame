# ADR-0003: PostgreSQL as the Primary Datastore

## Status

Accepted

## Context

The Core API Application needs a single, operationally simple datastore that supports relational
integrity and standard transactional guarantees

## Decision

Use PostgreSQL as the primary datastore for the Core API Application. As a widely adopted
industry-standard relational database, it fits the transactional nature of orders, inventory,
payments, and checkout, while providing ACID transactions, JSONB with GIN indexing for semi-structured
data, and read replicas for future read scalability, with first-class Spring Boot support

## Consequences

- Checkout and other cross-module work keep real ACID transactions in one database
- JSONB absorbs schema variation (e.g. product attributes) without constant migrations
- One relational database to provision, migrate, and operate, with mature tooling for backups and
  local development
