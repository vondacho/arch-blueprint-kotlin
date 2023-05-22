# arch-blueprint-kotlin
![build workflow](https://github.com/vondacho/arch-blueprint-kotlin/actions/workflows/build.yml/badge.svg)

A Kotlin project as template and pedagogical support for the teaching of Clean Architecture crafting practice.

## Features

- **Client management**: CRUD operations on client entities exposed by a REST API
- **Problem-solving** examples exposed by a Kotlin API

- Web request validation with [Swagger request validator](https://bitbucket.org/atlassian/swagger-request-validator/src/master/)
- Web security based on Basic Authentication
- Exception handling with [Zalando problem handling](https://github.com/zalando/problem-spring-web)
- Application management with Spring Actuator
- Acceptance testing with [Cucumber](https://cucumber.io/docs/cucumber/)
- Contract testing with [Pact](https://docs.pact.io/)
- Architecture testing with [ArchUnit](https://www.archunit.org/motivation)
- Reactive programming supported by Spring Webflux and [Reactor](https://projectreactor.io/)

## Getting started

- Build the project with `./gradlew clean build`.
- Start the containerized database with `docker-compose up`.
- Launch the application locally with `gradlew bootRun --args='--spring.profiles.active=local,r2dbc'`
- Play use cases with Postman using [this default collection](https://vondacho.github.io/arch-blueprint-kotlin/postman/postman_collection.json) or with [Swagger UI](https://vondacho.github.io/arch-blueprint-kotlin/api/).

## Documentation
Find full detailed documentation [here](https://vondacho.github.io/arch-blueprint-kotlin/) powered by [MkDocs](https://www.mkdocs.org/getting-started/)
