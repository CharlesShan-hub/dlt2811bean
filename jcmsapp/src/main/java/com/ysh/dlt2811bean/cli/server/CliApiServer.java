package com.ysh.dlt2811bean.cli.server;

import com.ysh.dlt2811bean.cli.handler.common.CommandHandler;
import com.ysh.dlt2811bean.cli.client.CmsClientCli;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.transport.app.CmsClient;
import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.Executors;

public class CliApiServer {

    private final int port;
    private final CmsClient client;
    private final Map<String, CommandHandler> handlers;
    private final CliContext ctx;
    private final CmsClientCli cli;
    private HttpServer server;

    public CliApiServer(int port, CmsClient client, Map<String, CommandHandler> handlers, CliContext ctx) {
        this(port, client, handlers, ctx, null);
    }

    public CliApiServer(int port, CmsClient client, Map<String, CommandHandler> handlers, CliContext ctx, CmsClientCli cli) {
        this.port = port;
        this.client = client;
        this.handlers = handlers;
        this.ctx = ctx;
        this.cli = cli;
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(4));

        server.createContext("/api/execute", this::handleExecute);
        server.createContext("/api/status", this::handleStatus);

        server.start();
        System.out.println(CmsColor.gray("  CliApiServer started on port " + port));
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
            cmdLine = URLDecoder.decode(cmdLine.substring(4), StandardCharsets.UTF_8);
        }

        PrintStream originalOut = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream captureOut = new PrintStream(baos, true, StandardCharsets.UTF_8);
        System.setOut(captureOut);

        try {
            if (cli != null) {
                cli.executeLine(cmdLine, false);
            } else {
                System.out.println("  " + CmsColor.red("CLI instance not available"));
            }
        } catch (Exception e) {
            System.out.println("  " + CmsColor.red("Error: " + e.getMessage()));
        } finally {
            System.setOut(originalOut);
            captureOut.close();
        }

        String responseText = baos.toString(StandardCharsets.UTF_8);
        sendResponse(exchange, 200, responseText);
    }

    private void handleStatus(HttpExchange exchange) throws IOException {
        String status = "{\"connected\": " + client.isConnected()
                + ", \"handlers\": " + handlers.size()
                + ", \"port\": " + port + "}";
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
            return baos.toString(StandardCharsets.UTF_8);
        }
    }

    private void sendResponse(HttpExchange exchange, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(code, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
