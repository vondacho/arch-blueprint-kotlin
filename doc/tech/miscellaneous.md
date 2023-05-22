# Miscellaneous

## This documentation
Build technical documentation with `./gradlew allureAggregateReport plantumlAll generateSwaggerUI mkdocsBuild`.

## Publication to Structurizr cloud
- To have a free account on https://structurizr.com/help/getting-started
- To run:
  ```
  java \
  -cp ./build/libs/arch-blueprint-kotlin.jar \
  -Dloader.main=edu.obya.blueprint.client.c4.C4WithDsl \
  org.springframework.boot.loader.PropertiesLauncher \
  ./src/c4/blueprint.dsl \
  <workspace-id>
  ```
- To visualize your architecture model at https://structurizr.com/

## Map components flows
Record components flows with `./gradlew appmap test`.

Then, view your local AppMap JSON resources following [this tutorial](https://github.com/vondacho/appmap-viewer#getting-started).

## Static code analysis
Run test and production codes with `Detekt`, e.g. `gradlew clean build` or `gradlew detekt`

## Release
Draft new release of the application from GitHub [release panel](https://github.com/vondacho/arch-blueprint-java/releases).

## Test users

- `test / test` gives access to almost all endpoints but the delete and shutdown ones.
- `admin / admin` gives access to all functional and configured application management endpoints.

## Spring profiles

- `local` execution on local Postgres databases on port 5432.
- `test` defines credentials and access control for **anonymous**, **test** and **admin** users.
- `r2dbc` enables persistence with relational database using R2DBC reactive driver.
