FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

COPY target/easyorder-*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
