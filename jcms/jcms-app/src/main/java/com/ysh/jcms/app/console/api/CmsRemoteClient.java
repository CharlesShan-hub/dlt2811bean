package com.ysh.jcms.app.console.api;

import com.ysh.jcms.utils.config.CmsConfigLoader;

import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLEncoder;

/**
 * Remote CMS CLI client.
 *
 * <p>Sends a command to the local CMS CLI API server for execution and prints the response.
 * Usage:
 * <pre>
 * java com.ysh.jcms.app.console.api.CmsRemoteClient [--port N] &lt;command&gt;
 * </pre>
 */
public class CmsRemoteClient {

    public static void main(String[] args) {
        String host = CmsConfigLoader.load().getClient().getConsole().getApiHost();
        int port = CmsConfigLoader.load().getClient().getConsole().getApiPort();

        // Parse --port option
        int i = 0;
        while (i < args.length - 1 && args[i].startsWith("--")) {
            switch (args[i]) {
                case "--port":
                    try {
                        port = Integer.parseInt(args[++i]);
                    } catch (NumberFormatException e) {
                        System.err.println("Invalid port: " + args[i]);
                        System.exit(1);
                    }
                    break;
                default:
                    System.err.println("Unknown option: " + args[i]);
                    System.exit(1);
            }
            i++;
        }

        // Remaining args form the command line
        StringBuilder cmdBuilder = new StringBuilder();
        while (i < args.length) {
            if (cmdBuilder.length() > 0) cmdBuilder.append(" ");
            String arg = args[i];
            if (arg.contains(" ") || arg.contains("\"")) {
                cmdBuilder.append("\"").append(arg.replace("\"", "\\\"")).append("\"");
            } else {
                cmdBuilder.append(arg);
            }
            i++;
        }

        String cmdLine = cmdBuilder.toString();
        if (cmdLine.isEmpty()) {
            System.err.println("Usage: java CmsRemoteClient [--port N] <command>");
            System.err.println("  e.g. java CmsRemoteClient connect --ap C_B5041X/S1");
            System.exit(1);
        }

        try {
            String response = executeCommand(host, port, cmdLine);
            System.out.print(response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }

    static String executeCommand(String host, int port, String cmdLine) throws Exception {
        String url = host + ":" + port + "/api/execute";
        URI uri = new URI(url);
        HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);

        String body = "cmd=" + URLEncoder.encode(cmdLine, "UTF-8");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        try (OutputStream os = conn.getOutputStream()) {
            os.write(body.getBytes("UTF-8"));
        }

        int responseCode = conn.getResponseCode();
        try (InputStream is = (responseCode >= 200 && responseCode < 300)
                ? conn.getInputStream() : conn.getErrorStream();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[4096];
            int len;
            while ((len = is.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            return baos.toString("UTF-8");
        }
    }
}
