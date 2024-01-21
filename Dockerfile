FROM eclipse-temurin:21-alpine
MAINTAINER zlatanov.xyz
COPY target/subs-buddy.jar subs-buddy.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/subs-buddy.jar"]