# arch-blueprint-kotlin

[![Build Status](https://travis-ci.com/vondacho/arch-blueprint-kotlin.svg?branch=master)](https://travis-ci.com/vondacho/arch-blueprint-kotlin)

A Kotlin project as template and pedagogical support for the teaching of Clean Architecture crafting practice.

## Features

- **Client management**: CRUD operations on client entities exposed by a REST API
- **Problem-solving** examples exposed by a Kotlin API

## Getting started

- To build the project with `gradlew clean build`
- To build the project reports with `gradlew clean appmap test aggregate allureReport`
- To run a containerized database with `docker-compose run postgres`
- To execute the main application (REST API) locally with `gradlew bootRun --args='--spring.profiles.active=local'`

## Technical specifications

- JDK 11
- Kotlin 1.5.0
- SpringBoot 2.5.0
- Webflux
- Hibernate
- Zalando problem handling https://github.com/zalando/problem-spring-web
- Atlassian OpenApi validator https://bitbucket.org/atlassian/swagger-request-validator
- Cucumber 6 https://cucumber.io/docs/cucumber/
- PostgreSQL 12
- Gradle 7 https://docs.gradle.org/current/userguide/userguide.html
- Serenity-BDD https://serenity-bdd.github.io/theserenitybook/latest/
- Allure https://docs.qameta.io/allure/
- Appmap https://appland.com/docs/appmap-overview.html
- Structurizr https://github.com/structurizr/

## Clean architecture

The logical layers are organized like an onion.

- **domain** (Domain model composed of core entities and core logic)
- **appl** (application logic, orchestration of the use cases, transactional): `- uses ->` _domain_
- **web** (API model and controllers) `- calls ->` _appl_
- **data** (Database model and adapters) `- imports ->` _domain_, _appl_
- **external** (External systems models and adapters) `- imports ->` _appl_

Every layer has its own configuration class.

The main application is stored inside the boot package; Voluntarily, this latter is not at the root of the project. 

## Testing

- **Acceptance** testing: acceptance scenarios written with `Gherkin` syntax
- **xUnit** testing: component integration tests and component unit tests
- **Reporting** with `Serenity-BDD` and `Allure`

## Acceptance testing

- Testing of **Client management** scenarios at **web API** level
- Testing of **problem-solving** logic at **domain** level
- With containerized database

### BDD

- Definition of `Gherkin` scenarios for Client management
- Definition of `Gherkin` scenarios for problem-solving
- Setup of `Cucumber` BDD framework
- Definition of reusable and specific BDD glue code
- Reporting with `Serenity-BDD`
- Instrumentation for documentation with `AppMap` recorder

## Reporting

### Living documentation

The **Acceptance** tests execution report produced by `Serenity-BDD` is available at `./build/reports/tests/serenity`

- To run `gradlew clean test aggregate`

### Test reporting

The **Test** execution report produced by `Allure` is available at `./build/reports/tests/allure`

- To run `gradlew clean test allureReport`, then `gradlew allureServer`
- Test execution report is visible on CI.

### Static code analysis

The Kotlin test and production codes get analysed with `Detekt`.

- To run `gradlew clean build` or `gradlew detekt`
- Static code analysis report is visible on Jenkins-CI with `Warnings Next Generation Jenkins` plugin.

## Documentation

### Functional behaviour

- Living documentation by `Serenity-BDD` during acceptance tests execution is available at `./build/reports/tests/serenity`.
- Report of acceptance scenario status is available at `./build/reports/tests/cucumber`; it is visible on Jenkins-CI with `cucumber-jenkins` plugin.

### Testing evidence

- Report of acceptance tests execution by `Serenity-BDD` is available at `./build/reports/tests/serenity`.
- Report of acceptance and xUnit tests execution by `Allure` is available at `./build/reports/tests/allure`; it can be visible on Jenkins-CI with `allure-jenkins` plugin.

### C4 architecture model

A C4 architecture model is specified by a set of Structurizr DSL files stored in `./doc/c4/dsl/`.

#### Publication to Structurizr cloud

- To have a free account on https://structurizr.com/help/getting-started
- To run:
  ```
  java \
  -cp ./build/libs/arch-blueprint-kotlin-1.0.0.jar \
  -Dloader.main=edu.software.craftsmanship.blueprint.doc.c4.C4WithDslKt \
  org.springframework.boot.loader.PropertiesLauncher \
  <workspace-id> <api-key> <api-secret> ./doc/c4/dsl/blueprint.dsl
  ```
- To visualize your architecture model at https://structurizr.com/

### AppMaps

The **Components flows** are recorded by `Appmap` https://appland.com/docs/appmap-overview.html.

#### Recording when testing

- To run `gradlew clean appmap test`
- The acceptance scenario records are stored at `./build/appmap`
- To visualize them inside IntelliJ with https://appland.com/docs/quickstart/intellij/step-1.html

#### Live recording

- To install https://www.app.land/setup/browserextension
- To run:
  ```
  java -Dappmap.debug \
  -javaagent:./libs/appmap-1.1.0.jar \
  -Dappmap.config.file=./appmap.yml \
  -Dspring.profiles.active=local \
  -jar ./build/libs/arch-blueprint-kotlin-1.0.0.jar
  ```
- To set the target http://localhost:8080 in AppLand extension popup, and press _Record_
- To play some acceptance scenario
- To press _Stop_ in AppLand extension popup
- Application scenario records are then published to https://www.app.land/ for visualization

#### Publication to AppLand portal

- To publish application scenario records with https://www.app.land/setup/cli
- To visualize your publications at https://www.app.land/
