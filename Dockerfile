FROM  eed3si9n/sbt:jdk11-alpine AS builder

WORKDIR /code
COPY . /code
RUN sbt assembly

FROM openjdk:11-slim
COPY --from=builder /code/target/scala-**/facepoke.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]