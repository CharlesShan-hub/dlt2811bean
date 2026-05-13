package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.command.HelpHandler;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import com.ysh.dlt2811bean.service.info.FcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.cli.handler.*;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

import java.nio.file.Paths;
import java.util.*;

public class CmsClientCli {

    private final CmsClient client = new CmsClient();
    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final LineReader reader;
    private final CliContext ctx;
    private CmsConfig config = CmsConfigLoader.load();
    private boolean running = true;
    private final java.util.Map<String, java.util.Map<String, java.util.Map<String, java.util.Map<String, Object>>>> cachedHierarchy
            = new java.util.LinkedHashMap<>();

    public CmsClientCli() {
        ctx = new CliContext(config, handlers, cachedHierarchy);
        reader = LineReaderBuilder.builder()
                .highlighter(new org.jline.reader.Highlighter() {
                    public org.jline.utils.AttributedString highlight(org.jline.reader.LineReader rdr, String buffer) {
                        String[] parts = buffer.split("\\s+", 2);
                        String cmd = parts[0].toLowerCase();
                        CommandHandler h = handlers.get(cmd);
                        if (h == null || h.updateConfigAndGetParams().isEmpty()) {
                            return new org.jline.utils.AttributedStringBuilder().append(buffer).toAttributedString();
                        }
                        int argsCount = buffer.trim().isEmpty() ? 0 : buffer.trim().split("\\s+").length - 1;
                        if (argsCount >= h.updateConfigAndGetParams().size()) {
                            return new org.jline.utils.AttributedStringBuilder().append(buffer).toAttributedString();
                        }
                        org.jline.utils.AttributedStringBuilder asb = new org.jline.utils.AttributedStringBuilder();
                        asb.append(buffer);
                        for (int i = argsCount; i < h.updateConfigAndGetParams().size(); i++) {
                            asb.style(org.jline.utils.AttributedStyle.DEFAULT.foreground(org.jline.utils.AttributedStyle.BRIGHT));
                            asb.append(" <" + h.updateConfigAndGetParams().get(i).getName() + ">");
                        }
                        return asb.toAttributedString();
                    }
                    public void setErrorPattern(java.util.regex.Pattern errorPattern) {}
                    public void setErrorIndex(int errorIndex) {}
                })
                .completer(new CmsCliCompleter(handlers, ctx))
                .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_cli_history"))
                .build();
        register(new RpcHandler(ctx));
        register(new IfaceDirHandler(ctx));
        register(new IfaceDefHandler(ctx));
        register(new MethodDirHandler(ctx));
        register(new MethodDefHandler(ctx));
        register(new FileGetHandler(ctx));
        register(new FileSetHandler(ctx));
        register(new FileDeleteHandler(ctx));
        register(new FileAttrHandler(ctx));
        register(new FileDirHandler(ctx));
        register(new SelectHandler(ctx));
        register(new SelectWithValueHandler(ctx));
        register(new OperateHandler(ctx));
        register(new CancelHandler(ctx));
        register(new TimeActOperateHandler(ctx));
        register(new MsvcbValHandler(ctx));
        register(new SetMsvcbHandler(ctx));

        HandlerRegistry.autoRegister(ctx, handlers);
    }

    private void register(CommandHandler handler) {
        handlers.put(handler.getName(), handler);
    }

    public void run() {
        java.util.logging.Logger.getLogger("org.bouncycastle").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("CMS CLI v1.0 — Type 'help' for commands, 'exit' to quit");
        java.util.List<String> batchBuffer = new java.util.ArrayList<>();
        while (running) {
            System.out.println();
            String raw;
            try {
                raw = reader.readLine("cms> ").trim();
            } catch (Exception e) {
                if (running) {
                    System.out.println(CmsColor.red("\n  Connection lost. Type 'connect' to reconnect."));
                }
                continue;
            }

            if (raw.isEmpty()) continue;

            if (raw.startsWith("#") || raw.startsWith("//")) continue;

            boolean isEndOfBatch = raw.endsWith(";");
            if (isEndOfBatch) {
                raw = raw.substring(0, raw.length() - 1).trim();
            }

            if (!raw.isEmpty()) {
                batchBuffer.add(raw);
            }

            if (isEndOfBatch) {
                java.util.List<String> batch = new java.util.ArrayList<>(batchBuffer);
                batchBuffer.clear();
                boolean stop = false;
                for (String cmdLine : batch) {
                    if (stop || !running) break;
                    if (!executeLine(cmdLine, false)) {
                        stop = true;
                    }
                }
            } else if (batchBuffer.size() == 1) {
                String singleLine = batchBuffer.remove(0);
                executeLine(singleLine);
            }
        }
        System.exit(0);
    }

    private boolean executeLine(String raw) {
        return executeLine(raw, true);
    }

    private boolean executeLine(String raw, boolean interactive) {
        String[] commands = raw.split(";");
        for (int i = 0; i < commands.length; i++) {
            String cmd = commands[i].trim();
            if (cmd.isEmpty() || cmd.startsWith("#") || cmd.startsWith("//")) continue;
            if (!executeSingle(cmd, interactive)) {
                return false;
            }
        }
        return true;
    }

    private boolean executeSingle(String raw, boolean interactive) {
        String line = raw.toLowerCase();

        if (line.isEmpty()) return true;

        if (line.startsWith("help ")) {
            String helpArg = line.substring(5).trim().toLowerCase();
            if (helpArg.equals("datatype")) {
                ((HelpHandler) handlers.get("help")).printDatatypeList();
                return true;
            }
            if (helpArg.startsWith("datatype ")) {
                String dtName = raw.substring(5).trim().substring(9).trim();
                DataTypeInfo dt = DataTypeInfo.byTypeName(dtName);
                if (dt == null) {
                    System.out.println("  " + CmsColor.red("Unknown datatype: " + dtName));
                } else {
                    ((HelpHandler) handlers.get("help")).printDatatypeHelp(dt);
                }
                return true;
            }
            if (helpArg.equals("cdc")) {
                ((HelpHandler) handlers.get("help")).printCdcList();
                return true;
            }
            if (helpArg.startsWith("cdc ")) {
                String cdcName = raw.substring(5).trim().substring(4).trim().toUpperCase();
                CdcInfo cdc = CdcInfo.byName(cdcName);
                if (cdc == null) {
                    System.out.println("  " + CmsColor.red("Unknown CDC: " + cdcName));
                } else {
                    ((HelpHandler) handlers.get("help")).printCdcHelp(cdc);
                }
                return true;
            }
            if (helpArg.equals("fc")) {
                ((HelpHandler) handlers.get("help")).printFcList();
                return true;
            }
            if (helpArg.startsWith("fc ")) {
                String fcName = raw.substring(5).trim().substring(3).trim().toUpperCase();
                FcInfo fc = FcInfo.byName(fcName);
                if (fc == null) {
                    System.out.println("  " + CmsColor.red("Unknown FC: " + fcName));
                } else {
                    ((HelpHandler) handlers.get("help")).printFcHelp(fc);
                }
                return true;
            }
            CommandHandler h = handlers.get(helpArg);
            String helpCmdName = helpArg;
            if (h == null) {
                ServiceInfo sectionInfo = ServiceInfo.bySection(helpArg);
                if (sectionInfo != null) {
                    helpCmdName = sectionInfo.getCliName();
                    h = handlers.get(helpCmdName);
                }
            }
            if (h == null && helpArg.matches("8\\.\\d+")) {
                ((HelpHandler) handlers.get("help")).printSectionCommands(helpArg);
                return true;
            }
            if (h == null) {
                System.out.println("  " + CmsColor.red("Unknown command: " + helpArg));
            } else {
                ((HelpHandler) handlers.get("help")).printCommandHelp(helpCmdName, h);
            }
            return true;
        }

        String[] inputParts = raw.split("\\s+", 2);
        String cmdName = inputParts[0].toLowerCase();
        String inlineArgs = inputParts.length > 1 ? inputParts[1].trim() : null;

        int dotIdx = inputParts[0].indexOf('.');
        if (dotIdx > 0 && inlineArgs == null) {
            inlineArgs = inputParts[0].substring(dotIdx + 1);
            cmdName = inputParts[0].substring(0, dotIdx).toLowerCase();
        }

        CommandHandler handler = handlers.get(cmdName);
        if (handler == null) {
            System.out.println(CmsColor.red("Unknown command: " + cmdName) + "  (type 'help' for available commands)");
            return true;
        }

        try {
            Map<String, String> values = new HashMap<>();
            java.util.List<String> inlineTokens = inlineArgs != null && !inlineArgs.isEmpty()
                ? tokenize(inlineArgs)
                : new java.util.ArrayList<>();

            if (inlineTokens.isEmpty() && !handler.updateConfigAndGetParams().isEmpty() && interactive) {
                StringBuilder hint = new StringBuilder();
                hint.append(CmsColor.gray("  Usage: " + cmdName));
                for (Param p : handler.updateConfigAndGetParams()) {
                    hint.append(" <" + p.getName() + ">");
                }
                System.out.println(hint.toString());
            }

            boolean namedMode = !inlineTokens.isEmpty() && inlineTokens.get(0).startsWith("--");

            if (namedMode) {
                java.util.Map<String, Param> paramMap = new java.util.LinkedHashMap<>();
                for (Param p : handler.updateConfigAndGetParams()) {
                    paramMap.put(p.getName(), p);
                }
                while (!inlineTokens.isEmpty()) {
                    String token = inlineTokens.remove(0);
                    if (!token.startsWith("--")) continue;
                    String paramName = token.substring(2);
                    Param param = paramMap.get(paramName);
                    if (param == null) {
                        System.out.println(CmsColor.red("  Unknown parameter: --" + paramName));
                        return false;
                    }
                    String value = !inlineTokens.isEmpty() && !inlineTokens.get(0).startsWith("--")
                        ? inlineTokens.remove(0)
                        : (param.getDefaultValue() != null ? param.getDefaultValue() : "");
                    if (!param.getEnumChoices().isEmpty()) {
                        String matched = matchEnum(value, param);
                        if (matched == null) {
                            System.out.println(CmsColor.red("  无效选项: " + value + ", 可选: " + param.getEnumChoices().stream().map(ec -> ec.value).collect(java.util.stream.Collectors.joining("/"))));
                            return false;
                        }
                        values.put(param.getName(), matched);
                    } else {
                        values.put(param.getName(), value);
                    }
                }
                for (Param param : handler.updateConfigAndGetParams()) {
                    if (!values.containsKey(param.getName())) {
                        values.put(param.getName(), param.getDefaultValue() != null ? param.getDefaultValue() : "");
                    }
                }
            } else {
                for (Param param : handler.updateConfigAndGetParams()) {
                if (!inlineTokens.isEmpty()) {
                    String token = inlineTokens.remove(0);
                    if (!param.getEnumChoices().isEmpty()) {
                        String matched = matchEnum(token, param);
                        if (matched == null) {
                            System.out.println(CmsColor.red("  无效选项: " + token + ", 可选: " + param.getEnumChoices().stream().map(ec -> ec.value).collect(java.util.stream.Collectors.joining("/"))));
                            continue;
                        }
                        values.put(param.getName(), matched);
                    } else {
                        values.put(param.getName(), token);
                    }
                    continue;
                }

                if (!interactive) {
                    if (param.getDefaultValue() != null) {
                        values.put(param.getName(), param.getDefaultValue());
                    } else {
                        values.put(param.getName(), "");
                    }
                    continue;
                }

                if (!param.getEnumChoices().isEmpty()) {
                    System.out.println("  " + param.getPrompt() + ":");
                    int maxLen = param.getEnumChoices().stream().mapToInt(ec -> ec.value.length()).max().orElse(0);
                    for (Param.EnumChoice ec : param.getEnumChoices()) {
                        System.out.println("    " + padRight(ec.value, maxLen) + "  " + ec.label);
                    }
                }
                String simplePrompt = param.getEnumChoices().isEmpty() ? param.getPrompt() : "  值";
                if (param.getDefaultValue() != null) {
                    simplePrompt += " [" + param.getDefaultValue() + "]";
                }
                String input = reader.readLine(simplePrompt + ": ").trim();

                if (input.isEmpty()) {
                    if (param.getDefaultValue() != null) {
                        values.put(param.getName(), param.getDefaultValue());
                    } else if (param.isRequired()) {
                        System.out.println(CmsColor.gray("  (skipped, using empty)"));
                        values.put(param.getName(), "");
                    } else {
                        values.put(param.getName(), "");
                    }
                } else {
                    if (!param.getEnumChoices().isEmpty()) {
                        String matched = matchEnum(input, param);
                        if (matched == null) {
                            System.out.println(CmsColor.red("  无效选项: " + input + ", 可选: " + param.getEnumChoices().stream().map(ec -> ec.value).collect(java.util.stream.Collectors.joining("/"))));
                            continue;
                        }
                        values.put(param.getName(), matched);
                    } else {
                        values.put(param.getName(), input);
                    }
                }
            }
            }
            handler.execute(client, values);
            ServiceInfo si = ServiceInfo.byCliName(cmdName);
            if (si != null && !client.isConnected()) {
                System.out.println(CmsColor.red("  Connection lost. Type 'connect' to reconnect."));
            }
        } catch (Exception e) {
            System.out.println(CmsColor.red("  ERROR: " + e.getMessage()));
            return false;
        }
        return true;
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static java.util.List<String> tokenize(String s) {
        java.util.List<String> tokens = new java.util.ArrayList<>();
        StringBuilder buf = new StringBuilder();
        boolean inQuote = false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') {
                inQuote = !inQuote;
            } else if (Character.isWhitespace(c) && !inQuote) {
                if (buf.length() > 0) {
                    tokens.add(buf.toString());
                    buf.setLength(0);
                }
            } else {
                buf.append(c);
            }
        }
        if (buf.length() > 0) {
            tokens.add(buf.toString());
        }
        return tokens;
    }

    private static String matchEnum(String input, Param param) {
        for (int i = 0; i < param.getEnumChoices().size(); i++) {
            Param.EnumChoice ec = param.getEnumChoices().get(i);
            if (ec.value.equals(input)) return ec.value;
            if (String.valueOf(i).equals(input)) return ec.value;
        }
        for (Param.EnumChoice ec : param.getEnumChoices()) {
            if (ec.label.equals(input)) return ec.value;
            if (ec.value.equalsIgnoreCase(input)) return ec.value;
        }
        return null;
    }

    public static void main(String[] args) {
        new CmsClientCli().run();
    }
}
