package com.ysh.dlt2811bean.cli;

import com.ysh.dlt2811bean.cli.handler.association.AbortHandler;
import com.ysh.dlt2811bean.cli.handler.association.AssociateHandler;
import com.ysh.dlt2811bean.cli.handler.association.ReleaseHandler;
import com.ysh.dlt2811bean.cli.handler.data.GetDataDefinitionHandler;
import com.ysh.dlt2811bean.cli.handler.data.GetDataDirectoryHandler;
import com.ysh.dlt2811bean.cli.handler.data.GetDataValuesHandler;
import com.ysh.dlt2811bean.cli.handler.data.SetDataValuesHandler;
import com.ysh.dlt2811bean.cli.handler.dataset.GetDataSetDirectoryHandler;
import com.ysh.dlt2811bean.cli.handler.dataset.GetDataSetValuesHandler;
import com.ysh.dlt2811bean.cli.handler.dataset.SetDataSetValuesHandler;
import com.ysh.dlt2811bean.cli.handler.dataset.CreateDataSetHandler;
import com.ysh.dlt2811bean.cli.handler.dataset.DeleteDataSetHandler;
import com.ysh.dlt2811bean.cli.handler.setting.SelectActiveSGHandler;
import com.ysh.dlt2811bean.cli.handler.setting.SelectEditSGHandler;
import com.ysh.dlt2811bean.cli.handler.setting.SetEditSGValueHandler;
import com.ysh.dlt2811bean.cli.handler.setting.ConfirmEditSGValuesHandler;
import com.ysh.dlt2811bean.cli.handler.setting.GetEditSGValueHandler;
import com.ysh.dlt2811bean.cli.handler.setting.GetSGCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.report.GetBRCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.report.SetBRCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.report.GetURCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.report.SetURCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.log.GetLCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.log.SetLCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.log.QueryLogByTimeHandler;
import com.ysh.dlt2811bean.cli.handler.log.QueryLogAfterHandler;
import com.ysh.dlt2811bean.cli.handler.log.GetLogStatusValuesHandler;
import com.ysh.dlt2811bean.cli.handler.goose.GetGoCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.goose.SetGoCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.sv.GetMSVCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.sv.SetMSVCBValuesHandler;
import com.ysh.dlt2811bean.cli.handler.directory.*;
import com.ysh.dlt2811bean.cli.handler.negotiation.NegotiateHandler;
import com.ysh.dlt2811bean.cli.handler.test.TestHandler;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.service.info.CdcInfo;
import com.ysh.dlt2811bean.service.info.DataTypeInfo;
import com.ysh.dlt2811bean.service.info.FcInfo;
import com.ysh.dlt2811bean.service.info.ServiceInfo;
import com.ysh.dlt2811bean.cli.handler.*;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.ParsedLine;

import java.nio.file.Paths;
import java.util.*;

public class CmsClientCli {

    private final CmsClient client = new CmsClient();
    private final Map<String, CommandHandler> handlers = new LinkedHashMap<>();
    private final LineReader reader;
    private final CliContext ctx;
    private CmsConfig config = CmsConfigLoader.load();
    private boolean running = true;
    private final java.util.Set<String> cachedRefs = new java.util.HashSet<>();
    private final java.util.Set<String> cachedLds = new java.util.HashSet<>();
    private final java.util.Set<String> cachedValues = new java.util.HashSet<>();

    public CmsClientCli() {
        ctx = new CliContext(config, handlers, cachedRefs, cachedLds, cachedValues);
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
                .completer(new Completer() {
                    @Override
                    public void complete(LineReader rdr, ParsedLine parsedLine, java.util.List<Candidate> candidates) {
                        String buffer = parsedLine.line();
                        String word = parsedLine.word();
                        if (buffer.toLowerCase().startsWith("help datatype ")) {
                            for (DataTypeInfo dt : DataTypeInfo.values()) {
                                if (dt.getTypeName().toLowerCase().startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(dt.getTypeName()));
                                }
                            }
                        } else if (buffer.toLowerCase().startsWith("help cdc ")) {
                            for (CdcInfo cdc : CdcInfo.values()) {
                                if (cdc.getName().toLowerCase().startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(cdc.getName()));
                                }
                            }
                        } else if (buffer.toLowerCase().startsWith("help ")) {
                            for (String cmd : handlers.keySet()) {
                                if (cmd.startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(cmd));
                                }
                            }
                            if ("datatype".startsWith(word.toLowerCase())) {
                                candidates.add(new Candidate("datatype"));
                            }
                            if ("cdc".startsWith(word.toLowerCase())) {
                                candidates.add(new Candidate("cdc"));
                            }
                        } else if (buffer.contains(" ")) {
                            String[] parts = buffer.trim().split("\\s+");
                            String cmdName = parts[0].toLowerCase();
                            CommandHandler h = handlers.get(cmdName);
                            if (h != null) {
                                int paramIdx = parts.length - 1;
                                if (paramIdx < h.updateConfigAndGetParams().size()) {
                                    Param param = h.updateConfigAndGetParams().get(paramIdx);
                                    if (!param.getEnumChoices().isEmpty()) {
                                        for (Param.EnumChoice ec : param.getEnumChoices()) {
                                            if (ec.value.toLowerCase().startsWith(word.toLowerCase())) {
                                                candidates.add(new Candidate(ec.value));
                                            }
                                        }
                                    } else if (isRefParam(cmdName, paramIdx)) {
                                        java.util.Set<String> pool;
                                        if ("ld-dir".equals(cmdName)) {
                                            pool = cachedLds;
                                        } else if ("ln-dir".equals(cmdName) || "get-all-def".equals(cmdName) || "get-all-values".equals(cmdName) || "get-all-cb".equals(cmdName) || "get-data-def".equals(cmdName)) {
                                            pool = ctx.getCachedLnRefs();
                                        } else if ("get-data-dir".equals(cmdName) || "get-data-values".equals(cmdName)) {
                                            pool = cachedValues;
                                        } else if ("set-data-values".equals(cmdName)) {
                                            pool = ctx.getCachedDaRefs();
                                        } else if ("get-dataset-values".equals(cmdName) || "get-dataset-dir".equals(cmdName)
                                                || "create-dataset".equals(cmdName) || "delete-dataset".equals(cmdName)
                                                || "set-dataset-values".equals(cmdName)
                                                || "select-active-sg".equals(cmdName) || "select-edit-sg".equals(cmdName)
                                                || "set-edit-sg-value".equals(cmdName) || "confirm-edit-sg".equals(cmdName)
                                                || "get-edit-sg-value".equals(cmdName) || "get-sgcb-values".equals(cmdName)
                                                 || "get-brcb-values".equals(cmdName) || "set-brcb-values".equals(cmdName)
                                                 || "get-urcb-values".equals(cmdName) || "set-urcb-values".equals(cmdName)
                                                  || "get-lcb-values".equals(cmdName) || "set-lcb-values".equals(cmdName)
                                                   || "query-log-by-time".equals(cmdName) || "query-log-after".equals(cmdName)
                                                   || "get-log-status".equals(cmdName)
                                                   || "get-gocb-values".equals(cmdName) || "set-gocb-values".equals(cmdName)
                                                    || "msvcb-val".equals(cmdName) || "set-msvcb".equals(cmdName)) {
                                            pool = cachedRefs;
                                        } else {
                                            pool = cachedRefs;
                                        }
                                        for (String ref : pool) {
                                            if (ref.toLowerCase().startsWith(word.toLowerCase())) {
                                                candidates.add(new Candidate(ref));
                                            }
                                        }
                                    } else if (param.getDefaultValue() != null && !param.getDefaultValue().isEmpty()) {
                                        candidates.add(new Candidate(param.getDefaultValue()));
                                    }
                                }
                            }
                        } else {
                            for (String cmd : handlers.keySet()) {
                                if (cmd.startsWith(word.toLowerCase())) {
                                    candidates.add(new Candidate(cmd));
                                }
                            }
                        }
                    }
                })
                .variable(LineReader.HISTORY_FILE, Paths.get(System.getProperty("user.home"), ".cms_cli_history"))
                .build();
        register(new HelpHandler(ctx));
        register(new ExitHandler(ctx));
        register(new ConnectHandler(ctx));
        register(new ConnectTlsHandler(ctx));
        register(new CloseHandler(ctx));
        register(new StatusHandler(ctx));
        register(new NegotiateHandler(ctx));
        register(new AssociateHandler(ctx));
        register(new ReleaseHandler(ctx));
        register(new AbortHandler(ctx));
        register(new TestHandler(ctx));
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
        register(new ServerDirHandler(ctx));
        register(new LdDirHandler(ctx));
        register(new LnDirHandler(ctx));
        register(new GetAllValuesHandler(ctx));
        register(new GetAllDefHandler(ctx));
        register(new GetAllCbHandler(ctx));
        register(new GetDataValuesHandler(ctx));
        register(new SetDataValuesHandler(ctx));
        register(new GetDataDirectoryHandler(ctx));
        register(new GetDataDefinitionHandler(ctx));
        register(new GetDataSetValuesHandler(ctx));
        register(new SetDataSetValuesHandler(ctx));
        register(new CreateDataSetHandler(ctx));
        register(new DeleteDataSetHandler(ctx));
        register(new GetDataSetDirectoryHandler(ctx));
        register(new SelectActiveSGHandler(ctx));
        register(new SelectEditSGHandler(ctx));
        register(new SetEditSGValueHandler(ctx));
        register(new ConfirmEditSGValuesHandler(ctx));
        register(new GetEditSGValueHandler(ctx));
        register(new GetSGCBValuesHandler(ctx));
        register(new GetBRCBValuesHandler(ctx));
        register(new SetBRCBValuesHandler(ctx));
        register(new GetURCBValuesHandler(ctx));
        register(new SetURCBValuesHandler(ctx));
        register(new GetLCBValuesHandler(ctx));
        register(new SetLCBValuesHandler(ctx));
        register(new QueryLogByTimeHandler(ctx));
        register(new QueryLogAfterHandler(ctx));
        register(new GetLogStatusValuesHandler(ctx));
        register(new GetGoCBValuesHandler(ctx));
        register(new SetGoCBValuesHandler(ctx));
        register(new GetMSVCBValuesHandler(ctx));
        register(new SetMSVCBValuesHandler(ctx));
        register(new CliSettingHandler(ctx));
        register(new ClearHandler(ctx));
    }

    private void register(CommandHandler handler) {
        handlers.put(handler.getName(), handler);
    }

    public void run() {
        java.util.logging.Logger.getLogger("org.bouncycastle").setLevel(java.util.logging.Level.SEVERE);
        System.out.println("CMS CLI v1.0 — Type 'help' for commands, 'exit' to quit");
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
            String line = raw.toLowerCase();

            if (line.isEmpty()) continue;

            if (line.startsWith("help ")) {
                String helpArg = line.substring(5).trim().toLowerCase();
                if (helpArg.equals("datatype")) {
                    ((HelpHandler) handlers.get("help")).printDatatypeList();
                    continue;
                }
                if (helpArg.startsWith("datatype ")) {
                    String dtName = raw.substring(5).trim().substring(9).trim();
                    DataTypeInfo dt = DataTypeInfo.byTypeName(dtName);
                    if (dt == null) {
                        System.out.println("  " + CmsColor.red("Unknown datatype: " + dtName));
                    } else {
                        ((HelpHandler) handlers.get("help")).printDatatypeHelp(dt);
                    }
                    continue;
                }
                if (helpArg.equals("cdc")) {
                    ((HelpHandler) handlers.get("help")).printCdcList();
                    continue;
                }
                if (helpArg.startsWith("cdc ")) {
                    String cdcName = raw.substring(5).trim().substring(4).trim().toUpperCase();
                    CdcInfo cdc = CdcInfo.byName(cdcName);
                    if (cdc == null) {
                        System.out.println("  " + CmsColor.red("Unknown CDC: " + cdcName));
                    } else {
                        ((HelpHandler) handlers.get("help")).printCdcHelp(cdc);
                    }
                    continue;
                }
                if (helpArg.equals("fc")) {
                    ((HelpHandler) handlers.get("help")).printFcList();
                    continue;
                }
                if (helpArg.startsWith("fc ")) {
                    String fcName = raw.substring(5).trim().substring(3).trim().toUpperCase();
                    FcInfo fc = FcInfo.byName(fcName);
                    if (fc == null) {
                        System.out.println("  " + CmsColor.red("Unknown FC: " + fcName));
                    } else {
                        ((HelpHandler) handlers.get("help")).printFcHelp(fc);
                    }
                    continue;
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
                    continue;
                }
                if (h == null) {
                    System.out.println("  " + CmsColor.red("Unknown command: " + helpArg));
                } else {
                    ((HelpHandler) handlers.get("help")).printCommandHelp(helpCmdName, h);
                }
                continue;
            }

            String[] inputParts = raw.split("\\s+", 2);
            String cmdName = inputParts[0].toLowerCase();
            String inlineArgs = inputParts.length > 1 ? inputParts[1].trim() : null;

            CommandHandler handler = handlers.get(cmdName);
            if (handler == null) {
                System.out.println(CmsColor.red("Unknown command: " + cmdName) + "  (type 'help' for available commands)");
                continue;
            }

            try {
                Map<String, String> values = new HashMap<>();
                java.util.List<String> inlineTokens = inlineArgs != null && !inlineArgs.isEmpty()
                    ? new java.util.ArrayList<>(java.util.Arrays.asList(inlineArgs.split("\\s+")))
                    : new java.util.ArrayList<>();

                if (inlineTokens.isEmpty() && !handler.updateConfigAndGetParams().isEmpty()) {
                    StringBuilder hint = new StringBuilder();
                    hint.append(CmsColor.gray("  Usage: " + cmdName));
                    for (Param p : handler.updateConfigAndGetParams()) {
                        hint.append(" <" + p.getName() + ">");
                    }
                    System.out.println(hint.toString());
                }
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
                handler.execute(client, values);
                ServiceInfo si = ServiceInfo.byCliName(cmdName);
                if (si != null && !client.isConnected()) {
                    System.out.println(CmsColor.red("  Connection lost. Type 'connect' to reconnect."));
                }
            } catch (Exception e) {
                System.out.println(CmsColor.red("  ERROR: " + e.getMessage()));
            }
        }
        System.exit(0);
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
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

    private static boolean isRefParam(String cmdName, int paramIdx) {
        return switch (cmdName) {
            case "ld-dir" -> paramIdx == 0;
            case "ln-dir", "get-all-values", "get-all-def", "get-all-cb", "get-data-dir", "get-data-def" -> paramIdx == 0;
            case "get-data-values", "set-data-values" -> paramIdx == 0;
            case "get-dataset-values", "set-dataset-values", "get-dataset-dir", "create-dataset", "delete-dataset" -> paramIdx == 0;
            case "select-active-sg", "select-edit-sg", "set-edit-sg-value", "confirm-edit-sg", "get-edit-sg-value", "get-sgcb-values" -> paramIdx == 0;
            case "get-brcb-values", "set-brcb-values", "get-urcb-values", "set-urcb-values" -> paramIdx == 0;
            case "get-lcb-values", "set-lcb-values", "query-log-by-time", "query-log-after", "get-log-status" -> paramIdx == 0;
            case "get-gocb-values", "set-gocb-values" -> paramIdx == 0;
            case "msvcb-val", "set-msvcb" -> paramIdx == 0;
            default -> false;
        };
    }

    public static void main(String[] args) {
        new CmsClientCli().run();
    }
}
