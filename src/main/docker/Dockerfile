FROM maven:3.6-jdk-11-openj9 as build-stage

WORKDIR app-build

ARG profile=dev

COPY ./src /app-build/src
COPY ./pom.xml /app-build
COPY ./checkstyle.xml /app-build
COPY ./src/main/resources/logback-spring-json.xml /app-build/src/main/resources/logback-spring.xml

RUN mvn package -P${profile}

FROM openjdk:11 as run-stage

WORKDIR app

COPY --from=build-stage /app-build/target/garwan-0.0.1-SNAPSHOT.jar /app

# RUN java -jar garwan-0.0.1-SNAPSHOT.jar
CMD ["java", "Main"]


