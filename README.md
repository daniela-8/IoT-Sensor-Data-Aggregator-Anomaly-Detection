# IoT Sensor Data Aggregator Anomaly Detection (Problem 2)

## Problem Definition
In industrial environments, hundreds of sensors emit data continuously. Identifying anomalies in real-time is crucial for preventing equipment failure. This application simulates a multi-threaded IoT environment where temperature sensors push real-time data to a shared queue. A concurrent aggregator service processes this stream, calculates rolling averages using a sliding window algorithm, and dispatches critical alerts if a reading deviates significantly from the historical baseline.

## Technical Highlights
* **Concurrency:** Utilizes `ScheduledExecutorService` for producers and a `LinkedBlockingQueue` to safely pass data to the consumer thread.
* **Modern Java:** Leverages Java Records and the Streams API.
* **Architecture:** Adheres to SOLID principles, utilizing Dependency Injection (e.g., injecting `AlertDispatcher` into `AggregatorService`) to ensure testability.
* **Testing:** Core business logic is fully covered by JUnit 5 and Mockito.

## How to Run
1. Ensure Java 17+ and Gradle are installed.
2. Clone the repository.
3. Build the project: `./gradlew build`
4. Run the application: `./gradlew run` (or run `Main.java` from your IDE).

## Sample Output
**Normal Operation:**
`10:15:01.123 [pool-1-thread-1] INFO  AggregatorService - Initializing baseline for: [TEMPERATURE] TEMP-ZONE-A: 21.45`
`10:15:05.123 [pool-2-thread-1] INFO  AggregatorService - Normal reading processed: [TEMPERATURE] TEMP-ZONE-B: 23.10`

**Anomaly Detection:**
`10:15:10.555 [pool-2-thread-1] WARN  AlertDispatcher - [CRITICAL ALERT] Anomaly detected for TEMP-ZONE-A! Reading: 41.20 (Avg: 22.15)`