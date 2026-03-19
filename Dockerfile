# Bước 1: Build ứng dụng bằng Maven
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Bước 2: Chạy ứng dụng bằng Java 21
FROM eclipse-temurin:21-jdk-jammy
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]