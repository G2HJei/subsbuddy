FROM eclipse-temurin:21-alpine
LABEL org.opencontainers.image.authors="zlatanov.xyz"
COPY subs-buddy-web/target/subs-buddy-web.jar subs-buddy-web.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/subs-buddy-web.jar"]