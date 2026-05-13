package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.cli.CommandHandler;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;
import java.util.Set;

public class CliContext {

    private final CmsConfig config;
    private final Map<String, CommandHandler> handlers;

    private final Map<String, Map<String, Map<String, Map<String, Object>>>> cachedHierarchy;

    public CliContext(CmsConfig config, Map<String, CommandHandler> handlers,
                      Map<String, Map<String, Map<String, Map<String, Object>>>> cachedHierarchy) {
        this.config = config;
        this.handlers = handlers;
        this.cachedHierarchy = cachedHierarchy;
    }

    public CmsConfig getConfig() { return config; }
    public Map<String, CommandHandler> getHandlers() { return handlers; }
    public Map<String, Map<String, Map<String, Map<String, Object>>>> getCachedHierarchy() { return cachedHierarchy; }

    /** Ensures an LD entry exists, returns its LN map. */
    public Map<String, Map<String, Map<String, Object>>> ldEntry(String ldName) {
        return cachedHierarchy.computeIfAbsent(ldName, k -> new java.util.LinkedHashMap<>());
    }

    /** Ensures an LN entry exists under the given LD, returns its ACSI-class map. */
    public Map<String, Map<String, Object>> lnEntry(String ldName, String lnName) {
        return ldEntry(ldName).computeIfAbsent(lnName, k -> new java.util.LinkedHashMap<>());
    }

    /** Puts member names under LD/LN/ACSI-class; does nothing if names is empty. */
    public void putAcdEntry(String ldName, String lnName, String acsiClass, Map<String, Object> members) {
        if (members == null || members.isEmpty()) return;
        lnEntry(ldName, lnName).put(acsiClass, members);
    }

    /** Gets cached LN references (LD/LN). */
    public Set<String> getCachedLnRefs() {
        Set<String> result = new java.util.HashSet<>();
        for (Map.Entry<String, Map<String, Map<String, Map<String, Object>>>> ldEntry : cachedHierarchy.entrySet()) {
            for (String lnName : ldEntry.getValue().keySet()) {
                result.add(ldEntry.getKey() + "/" + lnName);
            }
        }
        return result;
    }

    /** Gets cached DA references (LD/LN.DO.DA). */
    public Set<String> getCachedDaRefs() {
        Set<String> result = new java.util.HashSet<>();
        for (Map.Entry<String, Map<String, Map<String, Map<String, Object>>>> ldEntry : cachedHierarchy.entrySet()) {
            for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : ldEntry.getValue().entrySet()) {
                Map<String, Object> das = lnEntry.getValue().get("DATA_OBJECT");
                if (das != null) {
                    for (String da : das.keySet()) {
                        result.add(ldEntry.getKey() + "/" + lnEntry.getKey() + "." + da);
                    }
                }
            }
        }
        return result;
    }

    public CmsApdu sendAndPrint(CmsClient client, CmsAsdu<?> asdu) throws Exception {
        if (config.getCli().isTracePdu()) {
            printGray("  >> Request PDU:\n" + asdu.toString().indent(4).stripTrailing());
        }
        CmsApdu response = client.send(asdu);
        if (config.getCli().isTracePdu()) {
            printGray("  << Response PDU:\n" + response.toString().indent(4).stripTrailing());
        }
        return response;
    }

    private void printGray(String text) {
        for (String line : text.split("\n")) {
            System.out.println(CmsColor.gray(line));
        }
    }

    public void printGrayPdu(String label, Object pdu) {
        if (config.getCli().isTracePdu()) {
            printGray(label + "\n" + pdu.toString().indent(4).stripTrailing());
        }
    }

    public String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    public String bytesToHex(byte[] bytes, int len) {
        if (bytes == null) return "null";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(len, bytes.length); i++) {
            sb.append(String.format("%02X", bytes[i]));
        }
        return sb.toString();
    }

    public void discoverAfterConnect(CmsClient client) {
        CommandHandler serverDir = handlers.get("server-dir");
        if (serverDir == null) return;
        try {
            serverDir.execute(client, Map.of("referenceAfter", ""));
        } catch (Exception e) {
            System.out.println(CmsColor.red("  Auto-discovery failed at server-dir: " + e.getMessage()));
            return;
        }
        for (String ldName : cachedHierarchy.keySet()) {
            CommandHandler ldDir = handlers.get("ld-dir");
            if (ldDir == null) continue;
            try {
                ldDir.execute(client, Map.of("ldName", ldName, "referenceAfter", ""));
            } catch (Exception e) {
                System.out.println(CmsColor.red("  Auto-discovery failed at ld-dir " + ldName + ": " + e.getMessage()));
                continue;
            }
            Map<String, Map<String, Map<String, Object>>> lnMap = cachedHierarchy.get(ldName);
            if (lnMap == null) continue;
            for (String lnName : lnMap.keySet()) {
                CommandHandler getAllDef = handlers.get("get-all-def");
                if (getAllDef == null) continue;
                try {
                    getAllDef.execute(client, Map.of("target", ldName + "/" + lnName, "fc", "XX"));
                } catch (Exception e) {
                    System.out.println(CmsColor.red("  Auto-discovery failed at get-all-def " + ldName + "/" + lnName + ": " + e.getMessage()));
                }
            }
        }
    }
}
