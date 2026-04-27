FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy only needed files first (better caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

# Now copy source
COPY src src

# Build jar
RUN ./mvnw clean package -DskipTests

# Expose port (default)
EXPOSE 5000

# Run app with dynamic port
CMD ["sh", "-c", "java -jar target/*.jar --server.port=${PORT}"]