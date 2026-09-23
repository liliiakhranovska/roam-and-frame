# ADR-0008: Sync Command + Async Webhook as the Standard Third-Party Integration Pattern

## Status

Accepted

## Context

Third-party providers (Payment Gateway, Shipping Provider) don't always resolve a request within the
call that started it - authorization can go to fraud review, a shipment can take time to process on the
carrier's side. The application needs one consistent way to both kick off an action with a provider and
receive its eventual outcome, instead of each integration inventing its own shape or resorting to polling

## Decision

Integrate each third-party provider as a synchronous outbound command plus an asynchronous inbound
webhook. The owning module's Service calls the provider synchronously to initiate the action (authorize/
capture/refund a payment, create a shipment) and gets back an immediate acknowledgment or reference id.
The eventual, authoritative result arrives later through a dedicated Webhook Controller (Payment Webhook
Controller, Shipping Webhook Controller) that the provider calls back, which updates the module's state
and triggers any downstream notification

## Consequences

- Every integrated module carries two code paths per provider - an outbound client for the sync command
  and an inbound Webhook Controller for the async result - instead of one
- Modules must treat "pending" as a valid state between the sync command and the webhook's result, not
  just success/failure
- Webhook Controllers are public endpoints invoked by external systems, not customers, so each one must
  verify the caller (signature/shared secret) and handle duplicate deliveries idempotently
