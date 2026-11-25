# Dionysus

A home cocktail bar app for browsing recipes, tracking your inventory, and
finding out what you can make with what's on hand.

## Stack

- Kotlin 2.3
- Spring Boot 4 (Web MVC, Data JPA, Security, Thymeleaf)
- PostgreSQL 18 with Flyway migrations
- Spring Cloud AWS

## Running locally

Requires JDK 25 and Docker.

### 1. Start Postgres

Create an `init.sql` in the `./deploy/docker/` directory with the following:

```sql
create database <db-name>;
create user <db-user> with password '<db-password>';
alter database <db-name> owner to <db-user>;
```

Also create a `.env` file for Docker Compose with the same values in the `./deploy/docker/` directory:

```bash
POSTGRES_PASSWORD=<postgres-admin-password>
DB_NAME=<db-name>
DB_USER=<db-user>
DB_PASSWORD=<db-password>
```

Then start the database:

```bash
docker compose -f deploy/docker/compose.yaml up -d db
```

Flyway later applies the migrations in `src/main/resources/db/migration` on application startup.

Hibernate is set to `ddl-auto: validate` as an application startup safety-check that fails if the entities and schema don't match

### 2. Configure the app

Create `application-local.yaml` in the repo root, using the same database name,
user and password you put in `init.sql`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/<db-name>
    username: <db-user>
    password: <db-password>
  cloud:
    aws:
      parameter-store:
        enabled: false
```

`application-local.yaml` is gitignored, so your credentials stay out of the
repo. It also turns off AWS Parameter Store, which is only needed in deployed
environments.

### 3. Run the application

#### Using Gradle

Run with the `local` profile so the config file above is loaded:

```bash
SPRING_PROFILES_ACTIVE=local ./gradlew bootRun
```

#### Using Docker Compose

To run the application as a container instead of using bootRun, build the jar first and then run it with Docker Compose.
The app talks to the database over the Compose network, so `application-local.yaml` isn't used:

```bash
./gradlew bootJar
docker compose -f deploy/docker/compose.yaml up --build
```

The app starts on <http://localhost:8080>. 

## Tests

```bash
./gradlew test
```

Docker must be running for the integration tests to pass. The persistence tests use 
Testcontainers to start a embedded Postgres, so they start fresh each time.

## License

MIT — see [LICENSE](LICENSE).
