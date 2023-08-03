FROM openjdk:17-jdk

ARG JAR_FILE=./build/libs/toquiz.jar

COPY ${JAR_FILE} app.jar

EXPOSE 3000

ENTRYPOINT ["java", "-jar", "/app.jar"]