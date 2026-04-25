# Build Maven avec Java 17
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copier d'abord le pom.xml pour optimiser le cache Docker
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Copier le reste du projet et compiler
COPY src ./src
RUN mvn -B clean package -DskipTests

# Image finale légère Java 17
FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Render injecte PORT automatiquement. Localement, 8080 reste utilisé.
ENV PORT=8080
EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
