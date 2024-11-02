FROM openjdk:21-jdk-slim
ARG JAR_FILE=target/videojuegos-0.0.1.jar
COPY ${JAR_FILE} videojuegos.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","videojuegos.jar"]