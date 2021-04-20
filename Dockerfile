FROM adoptopenjdk/openjdk15:alpine-slim

COPY target/wezaam-withdrawal-service-*.jar /app/app.jar

CMD ["java", "-jar", "/app/app.jar"]