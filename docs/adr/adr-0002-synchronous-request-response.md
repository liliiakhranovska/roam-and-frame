# ADR-0002: Synchronous Request/Response for Phase 1; Event-Driven Evolution Deferred

## Status

Accepted

## Context

Modules need a default way to communicate, both in-process and with external systems

## Decision

Use synchronous request/response as the default communication style for Phase 1: in-process calls
between modules, and synchronous HTTP for outbound calls to external systems. Defer event-driven
messaging until a concrete need emerges

## Consequences

- Simpler to build, test
- Tighter coupling for now - a slow downstream call inside a synchronous chain directly slows the caller
- Introducing event-driven messaging later is a deliberate, scoped follow-up decision, not a redesign,
  since module boundaries are already established independently of how modules call each other
