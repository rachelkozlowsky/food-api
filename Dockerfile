
FROM openjdk:17-alpine AS final
ADD target/food-app.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]





