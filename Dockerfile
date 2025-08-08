FROM eclipse-temurin:21-alpine
LABEL org.opencontainers.image.authors="zlatanov.xyz"
COPY target/subs-buddy.jar subs-buddy.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/subs-buddy.jar"]