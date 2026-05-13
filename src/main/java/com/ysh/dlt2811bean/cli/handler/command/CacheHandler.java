package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CacheHandler implements CommandHandler {

    private final CliContext ctx;

    public CacheHandler(CliContext ctx) { this.ctx = ctx; }

    public String getName() { return "cache"; }
    public String getDescription() { return "查看缓存结构 (cache / cache.LD / cache.LD.LN / cache.LD.LN.ACSI)"; }
    public List<Param> getParams() {
        return List.of(
            new Param("path", "缓存路径 (留空=全部)", "")
        );
    }

    @SuppressWarnings("unchecked")
    public void execute(CmsClient client, Map<String, String> values) {
        String path = values.get("path");
        Map<String, Map<String, Map<String, Map<String, Object>>>> h = ctx.getCachedHierarchy();
        if (h.isEmpty()) {
            System.out.println("  (empty) — connect first or run server-dir");
            return;
        }
        if (path == null || path.isEmpty()) {
            for (String ld : h.keySet()) {
                System.out.println("  " + ld);
            }
            return;
        }
        String[] parts = path.split("\\.");
        Map<?, ?> current = h;
        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (current.containsKey(part)) {
                Object val = current.get(part);
                if (i == parts.length - 1) {
                    printKeys(val);
                } else if (val instanceof Map) {
                    current = (Map<?, ?>) val;
                } else {
                    System.out.println("  " + CmsColor.red("Cannot drill into: " + part));
                    return;
                }
            } else {
                System.out.println("  " + CmsColor.red("Path not found: " + part));
                return;
            }
        }
    }

    private void printKeys(Object value) {
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            if (map.isEmpty()) {
                System.out.println("  (empty)");
                return;
            }
            for (Object key : map.keySet()) {
                System.out.println("  " + key);
            }
        } else {
            System.out.println("  " + value);
        }
    }
}
