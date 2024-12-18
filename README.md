# Sample Java HTTP Server

Sample Java HTTP server demonstrating basic web server functionality using `com.sun.net.httpserver.HttpServer`. This sample project showcases creating a lightweight HTTP server with multiple endpoint handlers.
---

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Basic understanding of Java and HTTP concepts

### Open Using Daytona 
1. **Install Daytona**: Follow the [Daytona installation guide](https://www.daytona.io/docs/installation/installation/). 
2. **Create the Workspace**: 
```bash
daytona create https://github.com/mhmohona/java-hello-world.git
```

### Running the Application
1. Compile the Java files:
```bash
javac HelloWorldServer.java
```

2. Run the server:
```bash
java HelloWorldServer
```

3. The server will start on `http://localhost:8080/`

---

## ✨ Features

- **Multiple Endpoint Handlers**
  - Root `/` handler with customizable message
  - `/api` endpoint supporting GET and POST requests
  - Graceful server shutdown handling

### Endpoints

1. **Root Endpoint (`/`)**
   - Supports optional query parameter `message`
   - Default response: "Hello, World!"
   - Example: `http://localhost:8080/?message=CustomGreeting`

2. **API Endpoint (`/api`)**
   - Supports GET and POST methods
   - GET returns a simple JSON message
   - POST echoes the request body in the response

---

## 🛠 Technical Details

- Uses `com.sun.net.httpserver.HttpServer` for HTTP handling
- Implements thread pooling with `Executors.newFixedThreadPool()`
- Includes error handling and logging
- Supports basic request routing

---

## 📦 Project Structure

- `HelloWorldServer.java`: Main server implementation
- Nested classes:
  - `HelloWorldHandler`: Handles root endpoint
  - `ApiHandler`: Handles API endpoint requests

---

## 🔒 License

This project is open-source and available under the MIT License.
