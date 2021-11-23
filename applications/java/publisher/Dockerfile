FROM openjdk:11
ARG JAR_FILE=build/libs/publisher-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
EXPOSE 5000
ENTRYPOINT [ "java", "-jar", "/app.jar" ]