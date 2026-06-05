# Project Dependencies - Versions & Licenses

This document summarizes all frameworks and libraries used in this microservices project.

## Project Overview

- **Project Type:** Spring Boot Microservices (Java 25, Spring Boot 4.0.2)
- **Modules:** 8 modules (UserService, EstateService, VotingService, NotificationService, SecurityClient, Loggers, VotingEngine, Exceptions)
- **Database:** PostgreSQL

---

## Core Frameworks (Managed by Spring Boot Parent)

| Dependency | Version | License |
|------------|---------|---------|
| spring-boot-starter-parent | 4.0.2 | Apache 2.0 |
| spring-boot-starter-web | managed | Apache 2.0 |
| spring-boot-starter-actuator | managed | Apache 2.0 |
| spring-boot-starter-data-jpa | managed | Apache 2.0 |
| spring-boot-starter-validation | managed | Apache 2.0 |
| spring-boot-starter-liquibase | managed | Apache 2.0 |
| spring-boot-starter-websocket | managed | Apache 2.0 |
| spring-boot-starter-oauth2-resource-server | managed | Apache 2.0 |
| spring-boot-starter-aspectj | managed | Apache 2.0 |
| spring-security-core | managed | Apache 2.0 |
| spring-security-crypto | managed | Apache 2.0 |
| spring-security-oauth2-jose | managed | Apache 2.0 |
| spring-web | managed | Apache 2.0 |
| spring-context | managed | Apache 2.0 |
| spring-tx | managed | Apache 2.0 |

---

## Explicit Version Dependencies

| Dependency | Version | License |
|------------|---------|---------|
| springdoc-openapi-starter-webmvc-ui | 2.8.9 | Apache 2.0 |
| jjwt-api | 0.12.3 | Apache 2.0 |
| jjwt-impl | 0.12.3 (runtime) | Apache 2.0 |
| jjwt-jackson | 0.12.3 (runtime) | Apache 2.0 |
| testcontainers | 1.20.4 | MIT |
| testcontainers-junit-jupiter | 1.20.4 | MIT |
| testcontainers-postgresql | 1.20.4 | MIT |
| rest-assured | 5.5.1 | Apache 2.0 |
| postgresql | runtime | BSD-2-Clause |
| lombok | optional/provided | MIT |
| tomcat-embed-core | managed | Apache 2.0 |
| assertj-core | test | Apache 2.0 |
| junit-jupiter | test | EPL 2.0 |

---

## Internal Modules (Project)

| Module | Version | License |
|--------|---------|---------|
| Exceptions | 0.0.1-SNAPSHOT | (project) |
| Loggers | 0.0.1-SNAPSHOT | (project) |
| VotingEngine | 0.0.1-SNAPSHOT | (project) |
| SecurityClient | 0.0.1-SNAPSHOT | (project) |
| UserService | 0.0.1-SNAPSHOT | (project) |
| EstateService | 0.0.1-SNAPSHOT | (project) |
| VotingService | 0.0.1-SNAPSHOT | (project) |
| NotificationService | 0.0.1-SNAPSHOT | (project) |

---

## License Summary

| License | Count |
|---------|-------|
| Apache 2.0 | ~15 |
| MIT | 2 (Lombok, Testcontainers) |
| BSD-2-Clause | 1 (PostgreSQL) |
| EPL 2.0 | 1 (JUnit 5) |

---

## Usage Notes

All external dependencies are **permissive** (Apache 2.0, MIT, BSD). No copyleft (GPL) dependencies. This project can be used commercially.

### Key Libraries by Category

**Web:**
- Spring Boot Web
- Spring WebSocket

**Data:**
- Spring Data JPA
- PostgreSQL
- Liquibase

**Security:**
- Spring Security
- OAuth2 Resource Server
- JJWT (JWT)

**API Documentation:**
- SpringDoc OpenAPI 2.8.9

**Testing:**
- Testcontainers 1.20.4
- REST Assured 5.5.1

**Build Tools:**
- Lombok (code generation)
- Spring Boot Actuator