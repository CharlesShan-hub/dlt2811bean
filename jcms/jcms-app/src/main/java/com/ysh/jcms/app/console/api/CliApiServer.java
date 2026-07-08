package com.ysh.jcms.app.console.api;

import com.ysh.jcms.app.console.CmsClientConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

/**
 * Embedded HTTP API server for remote CLI execution.
 *
 * <p>Provides endpoints:
 * <ul>
 *   <li>{@code POST /api/execute} — execute a CLI command, returns text output</li>
 *   <li>{@code GET /api/status} — query connection status</li>
 * </ul>
 */
public class CliApiServer {

    private final int port;
    private final CmsClientConsole console;
    private HttpServer server;

    public CliApiServer(int port, CmsClientConsole console) {
        this.port = port;
        this.console = console;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.createContext("/api/execute", this::handleExecute);
        server.createContext("/api/status", this::handleStatus);
        server.start();
        ConsolePrinter.gray("CliApiServer started on port " + port);
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    private void handleExecute(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            sendResponse(exchange, 405, "Method not allowed (use POST)");
            return;
        }

        String body = readBody(exchange);
        if (body.isEmpty()) {
            sendResponse(exchange, 400, "Empty request body");
            return;
        }

        String cmdLine = body.trim();
        if (cmdLine.startsWith("cmd=")) {
            cmdLine = URLDecoder.decode(cmdLine.substring(4), "UTF-8");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream capturePs = new PrintStream(baos, true, "UTF-8");

        ConsolePrinter.setCaptureStream(capturePs);

        try {
            console.executeLine(cmdLine);
        } catch (Exception e) {
            baos.write(("ERR " + e.getMessage() + "\n").getBytes("UTF-8"));
        } finally {
            ConsolePrinter.setCaptureStream(null);
            capturePs.close();
        }

        String responseText = baos.toString("UTF-8");
        sendResponse(exchange, 200, responseText);
    }

    private void handleStatus(HttpExchange exchange) throws IOException {
        String status = "{\"connected\": " + console.isConnected()
                + ", \"serverRunning\": true, \"port\": " + port + "}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, 200, status);
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toString("UTF-8");
        }
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes("UTF-8");
        String trimmed = body.trim();
        String contentType = (trimmed.startsWith("{") || trimmed.startsWith("["))
            ? "application/json; charset=UTF-8"
            : "text/plain; charset=UTF-8";
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
