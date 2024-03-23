## FIRST CONTAINER
FROM openjdk:21-jdk-bullseye AS builder

WORKDIR /app

COPY src src
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

RUN chmod a+x /app/mvnw
RUN ./mvnw package -Dmaven.test.skip=true

## SECOND CONTAINER (another server)
FROM openjdk:21-jdk-bullseye

WORKDIR /app_run

COPY --from=builder /app/target/day19-0.0.1-SNAPSHOT.jar login.jar

# this is for documentation -- so you rmb what env variables you have to pass in (during docker run, or in Railway server variables)
# ENV API_KEY=placeholder
ENV PORT=8080
EXPOSE ${PORT}

# HEALTHCHECK --interval=30s --timeout=5s --start-period=5s --retries=3 CMD curl http://127.0.0.1:${PORT}/healthz || exit 1

# ASSIGNING SERVER_PORT TO PORT VARIABLE ALLOWS SERVER (e.g. RAILWAY) TO OVERRIDE THE VARIABLE TO THEIR DESIGNATED PORT
ENTRYPOINT SERVER_PORT=${PORT} java -jar login.jar