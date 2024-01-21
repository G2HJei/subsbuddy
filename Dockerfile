FROM eclipse-temurin:21-alpine
MAINTAINER zlatanov.xyz
COPY target/subsbuddy.jar subsbuddy.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/subsbuddy.jar"]