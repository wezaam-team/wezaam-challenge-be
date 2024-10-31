FROM openjdk:21-jdk-slim
ARG version=1.0.0
COPY target/wezaam-challenge-be-${version}.jar /app.jar
ENV JAVA_OPTS ""
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -jar /app.jar