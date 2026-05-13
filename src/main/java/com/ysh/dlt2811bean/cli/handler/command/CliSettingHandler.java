package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

public class CliSettingHandler implements CommandHandler {

    private final CliContext ctx;

    public CliSettingHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "cli-setting"; }
    public String getDescription() { return "查看/修改 CLI 运行时配置"; }
    public List<Param> getParams() {
        return List.of(
            new Param("key", "配置项 (trace-pdu)", "trace-pdu"),
            new Param("value", "值 (true/false)", "")
        );
    }

    public void execute(CmsClient client, Map<String, String> values) {
        String key = values.get("key");
        String value = values.get("value");

        if (key.isEmpty()) {
            printCurrentSettings();
            return;
        }

        if (value.isEmpty()) {
            printSetting(key);
            return;
        }

        switch (key) {
            case "trace-pdu" -> {
                boolean v = Boolean.parseBoolean(value);
                ctx.getConfig().getCli().setTracePdu(v);
                System.out.println(CmsColor.green("  trace-pdu = " + v));
            }
            default -> System.out.println(CmsColor.red("  Unknown setting: " + key));
        }
    }

    private void printCurrentSettings() {
        System.out.println("\n" + CmsColor.bold("CLI 运行时配置:"));
        System.out.println(CmsColor.gray("  ───────────────────────────────"));
        System.out.println("  trace-pdu    " + (ctx.getConfig().getCli().isTracePdu()
                ? CmsColor.green("true") : CmsColor.gray("false")));
        System.out.println(CmsColor.gray("\n  cli-setting <key> <value>  修改配置"));
        System.out.println(CmsColor.gray("  cli-setting <key>          查看单项"));
        System.out.println(CmsColor.gray("  cli-setting                查看全部"));
    }

    private void printSetting(String key) {
        switch (key) {
            case "trace-pdu" -> System.out.println("  trace-pdu = " + ctx.getConfig().getCli().isTracePdu());
            default -> System.out.println(CmsColor.red("  Unknown setting: " + key));
        }
    }
}
