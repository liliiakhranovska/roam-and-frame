# ADR-0001: Modular Monolith Architecture

## Status

Accepted

## Context

Roam & Frame's bounded contexts (catalog, cart, customer, checkout, order, inventory, payment, shipping,
notification) are closely related and evolve together, but collapsing them into one undifferentiated
codebase risks tangled dependencies and shared persistence

## Decision

Adopt a modular monolith for the Core API Application. Each bounded context is its own module with its own
domain/application/api/infrastructure layers, built and deployed as a single Spring Boot application.
Modules communicate in-process through explicit application-layer interfaces - never by reaching into
another module's entities or repositories. The AI Assistant is a separate deployable that talks to the
Core API Application only over REST/MCP, not shared code

## Consequences

- One deployable to build, test, and operate for the Core API Application in Phase 1
- In-process, synchronous calls between modules (e.g. checkout orchestrating cart, inventory, payment)
  avoid distributed-transaction complexity
- Module boundaries are enforced at compile time (separate Gradle subprojects), so the codebase can still
  be split into independent services later without a rewrite
- Whole application scales and redeploys as one unit; boundary discipline (no reaching into another
  module's repository) isn't compiler-enforced beyond the Gradle subproject split, so it depends on review
