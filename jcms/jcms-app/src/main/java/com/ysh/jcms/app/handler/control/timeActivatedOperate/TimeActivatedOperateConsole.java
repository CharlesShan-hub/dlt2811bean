package com.ysh.jcms.app.handler.control.timeActivatedOperate;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.ConsolePrinter;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.CommandInfo;
import com.ysh.jcms.app.console.Param;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TimeActivatedOperateConsole extends CommandHandler {

    public TimeActivatedOperateConsole() {
        super(CommandInfo.TIME_ACT_OPE);
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ref", "控制对象引用", null), new Param("oper-tm", "执行时间（UTC 秒级时间戳）", null), new Param("value", "控制值", ""),
                new Param("origin", "操作源 (0=本地, 1=远程)", ""), new Param("ctlNum", "控制序号", ""), new Param("test", "测试标志", ""),
                new Param("check", "校验码", ""), new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireAssociated(args))
            return;
        if (!CmsConsole.requireParam(args, "ref", "Usage: time-act-ope --ref <reference> --oper-tm <epochSeconds>"))
            return;
        if (!CmsConsole.requireParam(args, "oper-tm", "Usage: time-act-ope --ref <reference> --oper-tm <epochSeconds>"))
            return;

        String ref = args.get("ref").trim();
        long operTm = Long.parseLong(args.get("oper-tm"));
        ConsolePrinter.info("TimeActivatedOperate: " + ref + " at " + operTm);
        TimeActivatedOperateDao dao = new TimeActivatedOperateDao().ref(ref).operTmEpochSeconds(operTm).args(args);
        console.getClient(TimeActivatedOperateClient.class).execute(dao);
        ConsolePrinter.success("TimeActivatedOperate scheduled for " + ref);
    }
}
