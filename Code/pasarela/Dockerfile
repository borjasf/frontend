# Construimos la imagen a partir de una imagen base de Maven para compilar el proyecto
FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
# Copiamos el pom y el código fuente
COPY pom.xml .
COPY src ./src
# Compilamos saltando los tests para ir más rápido
RUN mvn clean package -DskipTests

# Ejecutamos la aplicación con una imagen base de Java ligera
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
# Copiamos el .jar generado en la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# El puerto variará dependiendo del microservicio 
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "app.jar"]