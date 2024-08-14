# Distributed_System_AS2

**Distributed_System_AS2** is a project designed to explore the concepts of distributed systems, including client-server architecture, Lamport clocks, XML formatting, and content aggregation. This repository contains various Java classes and test files that implement and demonstrate these distributed system principles.

## Project Structure

The project is organized into several key components:

### Java Classes

- **AggregationServer.java**: Manages the aggregation of content from multiple content servers.
- **client.java**: Represents the client that interacts with the content servers and the aggregation server.
- **Content.java**: Defines the structure and properties of the content to be shared and aggregated across servers.
- **ContentServer.java**: Hosts content and responds to client requests, acting as a content provider in the system.
- **FormatXML.java**: Provides functionality for formatting data into XML format.
- **GETClient.java**: A specialized client for handling HTTP GET requests to interact with servers.
- **lamportClock.java**: Implements the Lamport clock algorithm, used for ordering events in a distributed system.
- **node.java**: Represents a node in the distributed system, possibly functioning as a client or server.
- **toXml.java**: Converts objects and data structures into XML format for transmission or storage.

### Test Files

- **lamportTest.java**: Tests the functionality of the Lamport clock implementation.
- **multiClientTest.java**: Tests the system's behavior with multiple clients interacting concurrently.
- **singleGetTest.java**: Tests the system's behavior with a single GET request.
- **timeoutTest.java**: Tests the system's response to timeouts and delays in client-server communication.
- **returnStateTest.java**: Verifies the state return functionality of the system components.

### Data and Results Files

- **feed1.txt, feed2.txt, feed3.txt**: Text files containing sample content feeds used for testing content aggregation.
- **lamportResult.txt**: Stores the results of the Lamport clock test.
- **multiClientResult.txt**: Stores the results of the multi-client interaction test.
- **singleGetResult.txt**: Stores the results of the single GET request test.
- **timeoutResult.txt**: Stores the results of the timeout test.
- **returnStateResult.txt**: Stores the results of the state return test.

## How to Run

1. **Compile the Java files**:

```bash
javac *.java
```

2. **Run the tests**:

Each test file can be executed to verify the respective functionality. For example, to run the Lamport clock test:

```bash
java lamportTest
```

Similarly, you can run other test files as needed to observe the behavior of different components in the system.

3. **View Results**:

After running the tests, check the respective result files (e.g., lamportResult.txt, multiClientResult.txt) to view the output and verify the correctness of the system's behavior.

## Contributing
Contributions are welcome! If you have any improvements, bug fixes, or new test cases, feel free to open an issue or submit a pull request.
