package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.Param;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
import com.ysh.dlt2811bean.datatypes.type.CmsScalar;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.List;
import java.util.Map;

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
            if (isDaValueMap(map)) {
                String type = (String) map.get("type");
                Object val = map.get("value");
                if (val == null) {
                    System.out.println("  [" + CmsColor.green(type) + ", (no value)]");
                } else {
                    System.out.println("  [" + CmsColor.green(type) + ", " + val + "]");
                }
                return;
            }
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v == null) {
                    System.out.println("  " + entry.getKey() + " = (no value)");
                } else if (v instanceof Map) {
                    Map<?, ?> subMap = (Map<?, ?>) v;
                    if (isDaValueMap(subMap)) {
                        String type = (String) subMap.get("type");
                        Object val = subMap.get("value");
                        if (val == null) {
                            System.out.println("  " + entry.getKey() + " = [" + CmsColor.green(type) + ", (no value)]");
                        } else {
                            System.out.println("  " + entry.getKey() + " = [" + CmsColor.green(type) + ", " + val + "]");
                        }
                    } else {
                        System.out.println("  " + entry.getKey() + "/");
                    }
                } else if (v instanceof CmsData) {
                    System.out.println("  " + entry.getKey() + " = " + formatCmsDataValue((CmsData<?>) v));
                } else {
                    System.out.println("  " + entry.getKey() + " = " + v);
                }
            }
        } else if (value instanceof CmsData) {
            System.out.println("  " + formatCmsDataValue((CmsData<?>) value));
        } else {
            System.out.println("  " + (value == null ? "(no value)" : value));
        }
    }

    private static boolean isDaValueMap(Map<?, ?> map) {
        return map.containsKey("type") && map.containsKey("value") && map.size() == 2;
    }

    private String formatCmsDataValue(CmsData<?> data) {
        CmsType<?> inner = data.getInnerValue();
        if (inner == null) return CmsColor.gray("(no value)");
        String simpleName = inner.getClass().getSimpleName();
        String typeName = simpleName.startsWith("Cms") ? simpleName.substring(3).toUpperCase() : simpleName.toUpperCase();
        String valStr;
        if (inner instanceof CmsScalar) {
            Object val = ((CmsScalar<?, ?>) inner).get();
            valStr = val != null ? val.toString() : "null";
        } else {
            valStr = inner.toString();
        }
        return CmsColor.green(typeName) + "(" + valStr + ")";
    }
}
