# ADR-0007: AI Assistant Integrates via Dedicated MCP Tool Adapters, Not the Public REST API

## Status

Accepted

## Context

The AI Assistant needs to call into Core API Application capabilities - browsing products, checking
order status, and similar - to answer customers and take action on their behalf. It could reuse the
same REST Controllers the frontend calls, or get its own integration surface shaped for an LLM caller
instead of a browser

## Decision

Expose a dedicated MCP Tool Adapter per module for the AI Assistant to use, invoked over MCP, separate
from that module's REST Controller. Both sit on top of the same Service, but the AI Assistant never
calls a REST Controller directly - only the MCP tools deliberately exposed for it. A module gets an MCP
Tool Adapter only where the AI Assistant has a real, deliberate use for it - not automatically, and not
for every module

## Consequences

- Modules with AI Assistant capability carry a Controller and an MCP Tool Adapter over the same Service,
  each shaped for its own caller
- The AI Assistant's blast radius is limited to what's explicitly exposed as a tool, not the full REST
  surface
- Adding an AI Assistant capability means explicitly adding an MCP tool, not just pointing it at an
  existing endpoint
