# Build Phase
FROM eclipse-temurin:17-jdk as builder
WORKDIR /app

# 1. First copy only necesary files
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw .

# 2. Execution permissions to mvnw
RUN chmod +x mvnw

# 3. Download dependencies (create cache in this layer)
RUN ./mvnw dependency:go-offline

# 4. Copy all code
COPY src/ src/

# 5. Build app
RUN ./mvnw clean package -DskipTests

# Execution Phase
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]