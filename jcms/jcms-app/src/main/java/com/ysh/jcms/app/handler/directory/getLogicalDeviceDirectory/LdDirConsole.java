package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.console.CmsConsole;
import com.ysh.jcms.app.console.CommandHandler;
import com.ysh.jcms.app.console.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LdDirConsole implements CommandHandler {

    @Override
    public String name() {
        return "ld-dir";
    }

    @Override
    public String description() {
        return "获取逻辑节点目录 (GetLogicalDeviceDirectory)。用法: ld-dir [--ld LD] [--after REF] [--json]";
    }

    @Override
    public List<Param> params() {
        return Arrays.asList(new Param("ld", "逻辑设备名（不传则返回所有逻辑设备的完整引用）", null), new Param("after", "起始引用（分页截取）", ""),
                new Param("json", "JSON 格式输出", ""));
    }

    @Override
    public void execute(CmsConsole console, Map<String, String> args) throws Exception {
        if (!console.requireConnected(args))
            return;
        LdDirDao dao = new LdDirDao();
        String ld = args.get("ld");
        // 未传 --ld（空串）→ 不设置 ldName，走"所有逻辑设备"模式（标准 8.3.2.2 a)）
        if (ld != null && !ld.isEmpty()) {
            dao.ldName(ld);
        }
        String after = args.get("after");
        if (after != null && !after.isEmpty()) {
            dao.referenceAfter(after);
        }
        console.getClient(LdDirClient.class).execute(dao);
        List<String> items = new ArrayList<>(console.getContentManager().getLnNames());
        CmsConsole.outputList("Logical Nodes", items, s -> s, args);
    }
}
