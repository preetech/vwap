[![License](https://img.shields.io/badge/license-ANZ-blue)](https://img.shields.io/badge/license-ANZ-blue)
[![Release](https://img.shields.io/badge/release-0.0.1-orange)](https://img.shields.io/badge/release-0.0.1-orange)

# VWAP Calculation Stream

### Overview
This project is a VWAP (Volume Weighted Average Price) calculation stream using Apache Flink and Spring Boot. The application processes a stream of price data, calculates the VWAP for different currency pairs, and outputs the results.

#### Java 17 is particularly selected for the build run time as it includes advanced Just-In-Time (JIT) compiler optimizations and enhanced garbage collection, which are useful for low-latency calculations required in VWAP and avoid JVM crashes.
### Prerequisites
- Java 17 or higher
- Maven 3.6.0 or higher
- Docker (optional, for running dependencies like Kafka)

## Running the Application

### Using Maven

1. **Build the project:**

   ```sh
   mvn clean install
   ```

2. **Run the application:**

   ```sh
   mvn spring-boot:run
   ```

### Using Docker

1. **Build the Docker image:**

   ```sh
   docker build -t vwap-calculation-stream .
   ```

2. **Run the Docker container:**

   ```sh
   docker run -p 8080:8080 vwap-calculation-stream
   ```

## Key Components

### VWAPFlinkJobService

This class is responsible for setting up and starting the Flink job. It reads configuration properties from `application.properties` and configures the Flink execution environment accordingly.

### VWAPCalculatorService

This class contains the logic for calculating the VWAP. It processes the incoming price data and computes the VWAP for each currency pair.

### PriceDataSource

This class simulates a source of price data for the Flink job. In a production environment, this would be replaced with a real data source such as Kafka.

## Configuration

The application configuration is managed through the `application.properties` file located in `src/main/resources`. Key configuration properties include:

```ini
flink.streamTimeCharacteristic=EventTime
flink.restartAttempts=3
flink.restartDelay=10000
flink.checkpointInterval=60000
flink.parallelism=4
flink.bufferTimeout=100
vwap.window.time=3600
```
- flink.streamTimeCharacteristic=EventTime: This setting ensures that the application processes events based on their timestamps, which is crucial for accurate time-based operations like windowing.
- flink.restartAttempts=3: This setting configures the application to attempt to restart up to 3 times in case of failures, improving fault tolerance.  
- flink.restartDelay=10000: This setting specifies a 10-second delay between restart attempts, allowing time for transient issues to resolve.  
- flink.checkpointInterval=60000: This setting enables checkpointing every 60 seconds, ensuring that the application can recover from failures by resuming from the last checkpoint.  
- flink.parallelism=4: This setting configures the application to use 4 parallel instances for processing, distributing the load and improving performance.  
- flink.bufferTimeout=100: This setting specifies a buffer timeout of 100 milliseconds, balancing latency and throughput for efficient data processing.
- vwap.window.time=3600: This lets you set the time window for calculating VWAP. Can be changes to 5 sec for testing the app.

#### These configuration values make the application more resilient and production-ready by enhancing fault tolerance, ensuring accurate event-time processing, and optimizing resource utilization. This setup helps in maintaining high availability and reliability, which are critical for production environments.

## Unit Tests

Unit tests are provided for key components to ensure correctness. The tests are located in `src/test/java/au/com/anz/vwap/service`.

To run the tests:
```sh
   mvn test
   ```

## Monitoring and Logging
The application uses SLF4J for logging. Logs are configured in src/main/resources/logback-spring.xml. Monitoring can be set up using tools like Prometheus and Grafana to track resource usage and application performance.

## Conclusion
This project demonstrates a production-ready setup for a VWAP calculation stream using Apache Flink and Spring Boot. The configuration is flexible and can be adjusted through the application.properties file to suit different environments and requirements.
