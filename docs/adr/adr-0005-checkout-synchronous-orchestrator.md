# ADR-0005: Checkout via Synchronous Orchestrator, with Compensation on Failure

## Status

Accepted

## Context

Checkout must coordinate cart validation, stock reservation, payment authorization, order creation, and
shipment initiation as one customer-facing request, and needs a consistent way to unwind partial work
(e.g. stock reserved but payment declined) without leaving the system in an inconsistent state

## Decision

Implement checkout as a synchronous orchestrator that calls each module in sequence - validate cart,
reserve stock, authorize payment, create order, initiate shipment - within a single request/response
cycle. If a step fails, the orchestrator runs compensating actions for the steps already completed
(e.g. release reserved stock, void the payment authorization)

## Consequences

- Customer gets an immediate synchronous result (success or failure)
- Each step needs an explicit compensating action, written and maintained alongside it - failure
  handling is application code, not automatic