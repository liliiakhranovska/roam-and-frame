# ADR-0009: Hybrid Local-LLM-First with Escalation to Claude API

## Status

Accepted

## Context

The AI Assistant handles everything from trivial requests (e.g. "where's my order") to requests that
need real reasoning (comparing products, ambiguous questions). Sending every request to the Claude API
adds cost, latency, and an external dependency the trivial majority doesn't need, but a self-hosted
model alone can't match Claude on genuinely complex requests

## Decision

Route each request through a Model Router. Trivial/simple requests go to a small, locally-run model
first. The router escalates to the Claude API when the local model is low-confidence or can't handle the
request

## Consequences

- Most trivial/simple traffic is handled locally, without Claude API cost, latency, or availability
  dependency
- Genuinely complex requests still get Claude's reasoning, either directly or after local escalation, so
  response quality doesn't degrade for the requests that need it
