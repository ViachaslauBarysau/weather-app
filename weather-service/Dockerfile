FROM openjdk:8-jre-alpine
ARG JAR_FILE=target/weather-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} weather-service.jar
ENTRYPOINT ["java","-jar","weather-service.jar"]
EXPOSE 8082