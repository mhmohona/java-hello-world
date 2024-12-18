import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class HelloWorldServer {
    private static final Logger logger = Logger.getLogger(HelloWorldServer.class.getName());

    public static void main(String[] args) throws IOException {
        // Create an HTTP server listening on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Set a thread pool executor
        server.setExecutor(Executors.newFixedThreadPool(10)); // Adjust the pool size as needed

        // Create contexts for handling requests to different paths
        server.createContext("/", new HelloWorldHandler());
        server.createContext("/api", new ApiHandler());

        // Add a shutdown hook for graceful shutdown
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop(0); // Adjust delay as needed
        }));

        // Start the server
        System.out.println("Server is running on http://localhost:8080/");
        server.start();
    }

    static class HelloWorldHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                String defaultMessage = "Hello, World!";
                String customMessage = "";

                // Check for query parameters
                String query = exchange.getRequestURI().getQuery();
                if (query != null) {
                    String[] params = query.split("&");
                    for (String param : params) {
                        String[] keyValue = param.split("=");
                        if (keyValue.length == 2 && "message".equals(keyValue[0])) {
                            customMessage = keyValue[1];
                        }
                    }
                }

                // Construct the response
                String response = defaultMessage;
                if (!customMessage.isEmpty()) {
                    response += " Message: " + customMessage;
                }

                // Set response headers
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, response.length());

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                String errorResponse = "Internal Server Error";
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(500, errorResponse.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorResponse.getBytes());
                }
                logger.severe("Error in HelloWorldHandler: " + e.getMessage());
            }
        }
    }

    static class ApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";

            try {
                if ("GET".equalsIgnoreCase(method)) {
                    response = "{\"message\": \"API Endpoint\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                } else if ("POST".equalsIgnoreCase(method)) {
                    try (InputStream inputStream = exchange.getRequestBody()) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuilder body = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            body.append(line);
                        }
                        String requestBody = body.toString();
                        response = "{\"message\": \"POST Request Body: " + requestBody + "\"}";
                    }
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(200, response.length());
                } else {
                    response = "{\"error\": \"Method Not Allowed\"}";
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(405, response.length());
                }

                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            } catch (Exception e) {
                String errorResponse = "Internal Server Error";
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(500, errorResponse.length());
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(errorResponse.getBytes());
                }
                logger.severe("Error in ApiHandler: " + e.getMessage());
            }
        }
    }

     
}
