# spring-comm-pattern

A reusable Spring Boot library that centralizes HTTP client abstractions to standardize inter-service API communication using Feign, WebClient, and RestTemplate.

## Features

- Feign integration
- WebClient support
- RestTemplate abstraction
- Request interceptors
- Response handlers
- Header propagation
- Correlation IDs
- Logging
- Timeout configuration

---

## Supported Clients

- OpenFeign
- Spring WebClient
- RestTemplate

---

## Flow

Application
↓
Communication Layer
↓
Feign/WebClient
↓
Remote Service
