# ADR-0006: Expose a Controller Only Where a Real External Caller Exists

## Status

Accepted

## Context

Some modules are only ever invoked in-process by other modules' services (e.g. Inventory is called by
Catalog and the Checkout Orchestrator, never directly by a customer or external system), while others
are genuinely reached from outside the application (customer-facing endpoints, AI Assistant tool calls,
webhooks from payment/shipping providers). Giving every module a REST Controller by default adds API
surface and endpoints that nothing outside the application ever calls

## Decision

Add a Controller to a module only when a real external caller exists for it - a customer-facing
endpoint, an MCP tool the AI Assistant invokes, or a webhook from an external system. A module consumed
only in-process by other modules exposes its functionality solely through its Service class; it gets no
Controller

## Consequences

- Inventory has no Controller for now - it's reached only through its Service by Catalog and the
  Checkout Orchestrator
- API surface reflects actual external needs rather than one controller per module by convention
