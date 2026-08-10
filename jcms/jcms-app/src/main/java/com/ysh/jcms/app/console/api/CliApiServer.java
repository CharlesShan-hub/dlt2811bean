package com.ysh.jcms.app.console.api;

import com.ysh.jcms.app.console.CmsClientConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.util.CmsFormatUtil;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Executors;

/**
 * Embedded HTTP API server for remote CLI execution.
 *
 * <p>
 * Provides endpoints:
 * <ul>
 * <li>{@code POST /api/execute} — execute a CLI command, returns text
 * output</li>
 * <li>{@code GET /api/status} — query connection status</li>
 * <li>{@code GET /ui/*} — serve Vue web UI static files</li>
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
        server.setExecutor(Executors.newSingleThreadExecutor());
        server.createContext("/api/execute", this::handleExecute);
        server.createContext("/api/status", this::handleStatus);
        server.createContext("/ui", this::handleStatic);
        server.createContext("/", this::handleStatic);
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
        String ap = console.associatedAp();
        String status = "{\"connected\": " + console.connected() + ", \"tcpConnected\": " + console.clientConnected() + ", \"associated\": "
                + console.connected() + ", \"ap\": " + (ap != null ? "\"" + CmsFormatUtil.escapeJson(ap) + "\"" : "null") + ", \"tls\": "
                + console.tlsConnected() + ", \"apSecure\": " + console.associatedSecure() + ", \"serverRunning\": true, \"port\": " + port
                + "}";
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        sendResponse(exchange, 200, status);
    }

    private void handleStatic(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        if (requestPath.equals("/")) {
            requestPath = "/index.html";
        }

        // Try webui/dist first (production build), fallback to index.html for SPA
        Path filePath = Paths.get("webui/dist", requestPath);
        if (!Files.exists(filePath)) {
            filePath = Paths.get("webui/dist/index.html");
        }
        if (!Files.exists(filePath)) {
            String html = "<!DOCTYPE html><html><head><meta charset='UTF-8'><title>CMS Console</title></head>"
                    + "<body style='background:#0f1117;color:#e1e3ec;display:flex;align-items:center;justify-content:center;height:100vh;font-family:sans-serif;'>"
                    + "<div style='text-align:center;'><h1>⚡ CMS Console</h1>"
                    + "<p style='color:#9196ab;'>请先构建前端: <code style='background:#1e2030;padding:4px 8px;border-radius:4px;'>cd webui && yarn build</code></p>"
                    + "</div></body></html>";
            sendResponse(exchange, 200, html);
            return;
        }

        String contentType = guessContentType(filePath.toString());
        exchange.getResponseHeaders().set("Content-Type", contentType);
        byte[] bytes = Files.readAllBytes(filePath);
        sendResponse(exchange, 200, new String(bytes, StandardCharsets.UTF_8));
    }

    private String readBody(HttpExchange exchange) throws IOException {
        try (InputStream is = exchange.getRequestBody(); ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toString("UTF-8");
        }
    }

    private void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        addCorsHeaders(exchange);
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

    private static String guessContentType(String path) {
        if (path.endsWith(".html"))
            return "text/html; charset=UTF-8";
        if (path.endsWith(".css"))
            return "text/css; charset=UTF-8";
        if (path.endsWith(".js"))
            return "application/javascript; charset=UTF-8";
        if (path.endsWith(".json"))
            return "application/json";
        if (path.endsWith(".svg"))
            return "image/svg+xml";
        if (path.endsWith(".png"))
            return "image/png";
        if (path.endsWith(".ico"))
            return "image/x-icon";
        if (path.endsWith(".woff2"))
            return "font/woff2";
        return "application/octet-stream";
    }
}
