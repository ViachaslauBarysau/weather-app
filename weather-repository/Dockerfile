FROM openjdk:8-jre-alpine
ARG JAR_FILE=target/weather-repository-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} weather-repository.jar
ENTRYPOINT ["java","-jar","weather-repository.jar"]
EXPOSE 8083