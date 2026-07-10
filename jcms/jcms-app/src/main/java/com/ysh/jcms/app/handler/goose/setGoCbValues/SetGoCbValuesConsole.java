package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SetGoCbValuesConsole implements CommandHandler {

    @Override
    public String name() {
        return "set-gocb-vals";
    }

    @Override
    public String description() {
        return "设置 GOOSE 控制块值 (SetGoCBValues) [--json]。\n" + "  案例:\n"
                + "    set-gocb-vals --ref LD0/LLN0.gocb1                               # 仅引用（无字段修改）\n"
                + "    set-gocb-vals --ref LD0/LLN0.gocb1 --go-ena true                 # 启用GOOSE\n"
                + "    set-gocb-vals --ref LD0/LLN0.gocb1 --go-ena false                # 停用GOOSE\n"
                + "    set-gocb-vals --ref LD0/LLN0.gocb1 --go-id \"MyGoCB\"            # 改 GO ID\n"
                + "    set-gocb-vals --ref LD0/LLN0.gocb1 --dat-set \"dsGOOSE\"         # 改数据集";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "GoCB 引用，如 LD0/LLN0.gocb1", null), new Param("go-ena", "GOOSE 使能 (true/false)", null),
                new Param("go-id", "GOOSE ID (VisibleString129)", null), new Param("dat-set", "数据集引用 (ObjectReference)", null),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;

        if (!CmsConsole.requireParam(args, "ref", "Usage: set-gocb-vals --ref <gocbRef> [options]"))
            return;

        String ref = args.get("ref");
        SetGoCbValuesDao dao = new SetGoCbValuesDao().ref(ref.trim());
        String v;

        v = args.get("go-ena");
        if (v != null && !v.isEmpty())
            dao.goEna(Boolean.parseBoolean(v));
        v = args.get("go-id");
        if (v != null && !v.isEmpty())
            dao.goID(v);
        v = args.get("dat-set");
        if (v != null && !v.isEmpty())
            dao.datSet(v);

        if (!CmsConsole.isJsonMode(args)) {
            ConsolePrinter.info("Setting GoCB values: ref=" + ref);
        }
        console.getClient(SetGoCbValuesClient.class).execute(dao);
        CmsConsole.outputMessage("GoCB values set for " + ref, args);
    }
}
