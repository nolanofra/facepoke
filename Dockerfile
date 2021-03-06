FROM  eed3si9n/sbt:jdk11-alpine AS builder

WORKDIR /code
COPY . /code
RUN sbt "project core" assembly
FROM openjdk:11-slim
COPY --from=builder /code/core/target/scala-**/facepoke.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]