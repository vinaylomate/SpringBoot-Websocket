# SpringBoot-Websocket

> A small Spring Boot multi-module project demonstrating WebSocket communication with a simple microservice setup (API Gateway, Eureka service registry, and a WebSocket service).

---

## Table of Contents

- [Project Overview](#project-overview)
- [Repository Structure](#repository-structure)
- [Prerequisites](#prerequisites)
- [Running the Project (recommended)](#running-the-project-recommended)
- [Running Locally (manual)](#running-locally-manual)
- [WebSocket Usage](#websocket-usage)
- [Useful Commands](#useful-commands)

---

## Project Overview

This repository contains a small multi-module Spring Boot project that demonstrates WebSocket-based messaging within a microservice-style layout. It includes modules for an API Gateway (apigw), a Eureka service registry (eureka-server), and the WebSocket service (websocket). A `docker-compose.yml` is present to bring up the services together.


## Repository Structure

```
SpringBoot-Websocket/
├─ .mvn/
├─ apigw/            # API Gateway (Spring Cloud / Zuul or Gateway style)
├─ eureka-server/    # Eureka server for service discovery
├─ websocket/        # WebSocket service (Spring Boot application)
├─ docker-compose.yml
├─ pom.xml
└─ mvnw, mvnw.cmd
```

> Note: the repository layout above is based on the project tree in this repository.


## Prerequisites

- Java 11 or 17 (use the version configured in your local environment or check each module's `pom.xml` for compiler settings)
- Maven (you can use the provided `./mvnw` wrapper)
- Docker & Docker Compose (if you plan to use `docker-compose.yml` to run the whole stack)
- (Optional) A WebSocket-capable client like a simple web page, `wscat`, or a JS/TS client in the browser


## Running the Project (recommended using Docker Compose)

The repo contains a `docker-compose.yml` to start services together. This is the fastest way to get the whole stack running.

```bash
# from repository root
docker-compose up --build
```

This will (depending on the compose file configuration):
- build Docker images for modules (if Dockerfiles present)
- start Eureka server, API Gateway and the WebSocket service

Once started, check logs for the ports each service is listening on. Typical defaults used in small demos:
- Eureka: `http://localhost:8761`
- API Gateway: `http://localhost:8080`
- WebSocket service: `http://localhost:8081` (or proxied by the gateway)


## Running Locally (manual)

If you prefer to run modules locally with Maven:

```bash
# run Eureka server
cd eureka-server
./mvnw spring-boot:run

# in another terminal: run WebSocket service
cd ../websocket
./mvnw spring-boot:run

# optionally run API gateway
cd ../apigw
./mvnw spring-boot:run
```

> Use the specific `application.yml` / `application.properties` in each module to confirm ports and any credentials.


## WebSocket Usage

This project demonstrates how to register a WebSocket endpoint in a Spring Boot microservice and exchange messages with connected clients. Typical patterns used in such projects:

- **Simple WebSocket endpoint** (raw `WebSocket` or Spring's `@ServerEndpoint`) — clients open a `ws://` connection to the configured endpoint (for example `ws://localhost:8081/ws`)
- **Spring STOMP over WebSocket** — clients use SockJS + STOMP and send messages to app destinations like `/app/chat`, receiving broadcast messages from `/topic/messages`.

### Example: simple browser client (SockJS + STOMP)

```html
<!doctype html>
<html>
  <head>
    <script src="https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/stompjs@2.3.3/lib/stomp.min.js"></script>
  </head>
  <body>
    <script>
      const socket = new SockJS('http://localhost:8080/ws'); // or directly to websocket service
      const stompClient = Stomp.over(socket);

      stompClient.connect({}, function(frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/messages', function(msg){
          console.log('Received: ', msg.body);
        });

        stompClient.send('/app/chat', {}, JSON.stringify({from: 'Vinay', text: 'Hello from client'}));
      });
    </script>
  </body>
</html>
```

> Adjust endpoints based on actual paths in `websocket` module (check `WebSocketConfig` or controller classes).


## Useful Commands

- Build whole multi-module project:

```bash
./mvnw clean install -DskipTests
```

- Build & run a single module (example: websocket):

```bash
cd websocket
./mvnw spring-boot:run
```

- Run tests for a module:

```bash
./mvnw -pl websocket test
```


## Technologies

- Java + Spring Boot
- Spring WebSocket / STOMP (likely; see websocket module)
- Spring Cloud Gateway / Zuul (API gateway)
- Eureka (service discovery)
- Maven
- Docker / Docker Compose
