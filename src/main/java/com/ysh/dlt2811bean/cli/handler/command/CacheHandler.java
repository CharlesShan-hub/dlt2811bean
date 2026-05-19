package com.ysh.dlt2811bean.cli.handler.command;

import com.ysh.dlt2811bean.cli.handler.CliContext;
import com.ysh.dlt2811bean.cli.util.CliPrinter;
import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.cli.handler.common.Param;
import com.ysh.dlt2811bean.cli.handler.common.CommandHandler;
import com.ysh.dlt2811bean.datatypes.data.CmsData;
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
            CliPrinter.info("(empty) — connect first or run server-dir");
            return;
        }
        if (path == null || path.isEmpty()) {
            for (String ld : h.keySet()) {
                CliPrinter.info(ld);
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
                    CliPrinter.error("Cannot drill into: " + part);
                    return;
                }
            } else {
                CliPrinter.error("Path not found: " + part);
                return;
            }
        }
    }

    private void printKeys(Object value) {
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            if (map.isEmpty()) {
                CliPrinter.info("(empty)");
                return;
            }
            if (isDaValueMap(map)) {
                String type = (String) map.get("type");
                Object val = map.get("value");
                if (val == null) {
                    CliPrinter.info("[" + CmsColor.green(type) + ", (no value)]");
                } else {
                    CliPrinter.info("[" + CmsColor.green(type) + ", " + val + "]");
                }
                return;
            }
            for (Map.Entry<?, ?> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v == null) {
                    CliPrinter.info(entry.getKey() + " = (no value)");
                } else if (v instanceof Map) {
                    Map<?, ?> subMap = (Map<?, ?>) v;
                    if (isDaValueMap(subMap)) {
                        String type = (String) subMap.get("type");
                        Object val = subMap.get("value");
                        if (val == null) {
                            CliPrinter.info(entry.getKey() + " = [" + CmsColor.green(type) + ", (no value)]");
                        } else {
                            CliPrinter.info(entry.getKey() + " = [" + CmsColor.green(type) + ", " + val + "]");
                        }
                    } else if (isFcMap(subMap)) {
                        CliPrinter.info(entry.getKey() + "/");
                        for (Map.Entry<?, ?> fcEntry : subMap.entrySet()) {
                            CliPrinter.info("  " + fcEntry.getKey() + "/");
                            @SuppressWarnings("unchecked")
                            Map<String, Object> daMap = (Map<String, Object>) fcEntry.getValue();
                            for (Map.Entry<String, Object> daEntry : daMap.entrySet()) {
                                Object daVal = daEntry.getValue();
                                if (daVal instanceof Map && isDaValueMap((Map<?, ?>) daVal)) {
                                    Map<?, ?> daValueMap = (Map<?, ?>) daVal;
                                    String type = (String) daValueMap.get("type");
                                    Object val = daValueMap.get("value");
                                    if (val == null) {
                                        CliPrinter.info("    " + daEntry.getKey() + " = [" + CmsColor.green(type) + ", (no value)]");
                                    } else {
                                        CliPrinter.info("    " + daEntry.getKey() + " = [" + CmsColor.green(type) + ", " + val + "]");
                                    }
                                } else {
                                    CliPrinter.info("    " + daEntry.getKey());
                                }
                            }
                        }
                    } else if (isDataSetMemberMap(subMap)) {
                        CliPrinter.info(entry.getKey() + "/");
                        for (Map.Entry<?, ?> memberEntry : subMap.entrySet()) {
                            @SuppressWarnings("unchecked")
                            Map<String, Object> memberInfo = (Map<String, Object>) memberEntry.getValue();
                            String fc = (String) memberInfo.get("FC");
                            Object doRef = memberInfo.get("DO");
                            if (doRef instanceof Map) {
                                String doPath = resolveDoPath((Map<?, ?>) doRef);
                                CliPrinter.info("  [" + memberEntry.getKey() + "] fc=" + fc + " -> " + doPath);
                            } else {
                                CliPrinter.info("  [" + memberEntry.getKey() + "] fc=" + fc);
                            }
                        }
                    } else {
                        CliPrinter.info(entry.getKey() + "/");
                    }
                } else if (v instanceof CmsData) {
                    CliPrinter.info(entry.getKey() + " = " + CliPrinter.formatCmsDataValue((CmsData<?>) v));
                } else {
                    CliPrinter.info(entry.getKey() + " = " + v);
                }
            }
        } else if (value instanceof CmsData) {
            CliPrinter.info(CliPrinter.formatCmsDataValue((CmsData<?>) value));
        } else {
            CliPrinter.info(value == null ? "(no value)" : value.toString());
        }
    }

    private static boolean isFcMap(Map<?, ?> map) {
        if (map.isEmpty()) return false;
        for (Object key : map.keySet()) {
            if (!(key instanceof String) || ((String) key).length() != 2) return false;
        }
        for (Object val : map.values()) {
            if (!(val instanceof Map)) return false;
        }
        return true;
    }

    private static boolean isDaValueMap(Map<?, ?> map) {
        return map.containsKey("type") && map.containsKey("value") && map.size() == 2;
    }

    private static boolean isDataSetMemberMap(Map<?, ?> map) {
        if (map.isEmpty()) return false;
        for (Object val : map.values()) {
            if (!(val instanceof Map)) return false;
            Map<?, ?> m = (Map<?, ?>) val;
            if (!m.containsKey("FC")) return false;
        }
        return true;
    }

    /** Searches cachedHierarchy for the given DO map and returns its LD/LN.DO path. */
    private String resolveDoPath(Map<?, ?> targetDo) {
        Map<String, Map<String, Map<String, Map<String, Object>>>> h = ctx.getCachedHierarchy();
        for (Map.Entry<String, Map<String, Map<String, Map<String, Object>>>> ldEntry : h.entrySet()) {
            String ldName = ldEntry.getKey();
            for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : ldEntry.getValue().entrySet()) {
                String lnName = lnEntry.getKey();
                Map<String, Object> dataObject = lnEntry.getValue().get("DATA_OBJECT");
                if (dataObject == null) continue;
                for (Map.Entry<String, Object> doEntry : dataObject.entrySet()) {
                    if (doEntry.getValue() == targetDo) {
                        return ldName + "/" + lnName + "." + doEntry.getKey();
                    }
                }
            }
        }
        return "?";
    }
}
