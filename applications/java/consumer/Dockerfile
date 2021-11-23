FROM openjdk:11
ARG JAR_FILE=build/libs/consumer-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5050
ENTRYPOINT [ "java", "-jar", "/app.jar" ]