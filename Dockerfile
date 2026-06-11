# Stage 1: Build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos el archivo pom y el código fuente
COPY pom.xml .
COPY src ./src

# Compilamos el proyecto omitiendo los tests (ya pasaron localmente)
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copiamos el JAR generado desde la fase de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de la aplicación
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
