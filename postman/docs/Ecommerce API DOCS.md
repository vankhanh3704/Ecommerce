# Ecommerce API Documentation

**Collection:** Ecommerce API

This document provides an overview of the Ecommerce API collection, including authentication, core resources (users, roles, shops, locations, categories, products, variants, variant values), and how to run the collection in Postman.

## Overview

- **Purpose:** REST API for an Ecommerce Spring Boot application, covering authentication, user and role management, shop and location management, catalog (categories, products, variants), and inventory (variant values).
- **Base URL:** `{{baseUrl}}` (default: `http://localhost:8080`)
- **Auth:** `Authorization: Bearer {{api_key}}`
- **Version:** 1.0.0 (documentation version)

## Authentication

Most endpoints (except token issuance) require a valid Bearer token:

```http
Authorization: Bearer {{api_key}}
```

Use the **Auth** folder in the collection to obtain and manage tokens.

## Folders & Endpoints

The collection is structured by resource type:

- **Auth** – login and token lifecycle
- **Users** – CRUD for user accounts
- **Roles** – role definitions
- **Shops** – merchant shops
- **Shop Locations** – pickup/return addresses
- **Categories** – hierarchical product categories
- **Products** – product catalog
- **Variants** – variant metadata (e.g., size, color)
- **Variant Values** – specific variant values per product variant

Each request in the collection includes:

- HTTP method and path
- Required headers including `Authorization: Bearer {{api_key}}` and `Content-Type` where applicable
- Path / query / body parameters with types and requiredness (via request body JSON shape and URL variables)
- Example response (200 OK with JSON body)
- Code snippets for:
  - `curl`
  - JavaScript `fetch`

Open the collection in Postman to see the full per-endpoint Markdown descriptions.

## Environments

No dedicated Postman environments were found in this workspace. You can create an environment with:

- `baseUrl` – base URL of your API server
- `api_key` – Bearer token value

Then select that environment when running the collection.

## Run in Postman

Use the collection file from this workspace:

`postman/collections/Ecommerce API.postman_collection.json`

You can also use a "Run in Postman" button (for example, when publishing to a README or documentation site):

```markdown
[![Run in Postman](https://run.pstmn.io/button.svg)](postman/collections/Ecommerce%20API.postman_collection.json)
```

This wraps the Ecommerce API collection and documentation in a single, shareable entry point.
