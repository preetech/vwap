# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the project JAR file into the container at /app
COPY target/vwap-calculation-stream.jar /app/vwap-calculation-stream.jar

# Make port 8080 available to the world outside this container
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "vwap-calculation-stream.jar"]