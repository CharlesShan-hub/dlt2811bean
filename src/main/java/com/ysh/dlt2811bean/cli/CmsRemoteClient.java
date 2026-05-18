package com.ysh.dlt2811bean.cli;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class CmsRemoteClient {

    private static final String DEFAULT_HOST = "http://127.0.0.1";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: cms <command> [args...]");
            System.out.println("       cms --status");
            System.out.println("       cms --port <port> <command> [args...]");
            System.exit(1);
        }

        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        int argIndex = 0;

        if (args[argIndex].equals("--port") && args.length > argIndex + 1) {
            port = Integer.parseInt(args[argIndex + 1]);
            argIndex += 2;
        }

        if (args[argIndex].equals("--status")) {
            fetchStatus(host, port);
            return;
        }

        StringBuilder cmdBuilder = new StringBuilder();
        for (int i = argIndex; i < args.length; i++) {
            if (cmdBuilder.length() > 0) cmdBuilder.append(" ");
            String arg = args[i];
            if (arg.contains(" ") || arg.contains("\"")) {
                cmdBuilder.append("\"").append(arg.replace("\"", "\\\"")).append("\"");
            } else {
                cmdBuilder.append(arg);
            }
        }
        String cmdLine = cmdBuilder.toString();
        executeCommand(host, port, cmdLine);
    }

    private static void executeCommand(String host, int port, String cmdLine) {
        try {
            URI uri = URI.create(host + ":" + port + "/api/execute");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);

            String body = "cmd=" + URLEncoder.encode(cmdLine, StandardCharsets.UTF_8);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            try (java.io.InputStream is = (responseCode >= 200 && responseCode < 300)
                    ? conn.getInputStream() : conn.getErrorStream();
                 java.util.Scanner scanner = new java.util.Scanner(is, StandardCharsets.UTF_8)) {
                String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                System.out.print(response);
            }
        } catch (java.net.ConnectException e) {
            System.err.println("Error: Cannot connect to CMS CLI API server at " + host + ":" + port);
            System.err.println("Make sure the CMS CLI is running with API server enabled.");
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void fetchStatus(String host, int port) {
        try {
            URI uri = URI.create(host + ":" + port + "/api/status");
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (java.io.InputStream is = conn.getInputStream();
                 java.util.Scanner scanner = new java.util.Scanner(is, StandardCharsets.UTF_8)) {
                String response = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                System.out.println(response);
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
