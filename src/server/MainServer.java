package server;

import com.sun.net.httpserver.HttpServer;
import server.Handlers.*;

import java.io.IOException;
import java.net.InetSocketAddress;

public class MainServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Register handlers
        server.createContext("/", new HomeHandler());
        server.createContext("/keyrings", new KeyringsHandler());
        server.createContext("/keychains", new KeychainsHandler());
        server.createContext("/cart", new CartHandler());
        server.createContext("/checkout", new CheckoutHandler());

        server.setExecutor(null); // Default executor
        System.out.println("Server is running on http://localhost:8080");
        server.start();
    }
}
