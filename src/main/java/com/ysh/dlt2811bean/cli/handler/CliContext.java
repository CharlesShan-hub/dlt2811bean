package com.ysh.dlt2811bean.cli.handler;

import com.ysh.dlt2811bean.utils.CmsColor;
import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.cli.util.AutoTestHeartbeat;
import com.ysh.dlt2811bean.cli.handler.common.CommandHandler;
import com.ysh.dlt2811bean.datatypes.data.CmsDataDefinition;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsDataDefinitionEntry;
import com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory;
import com.ysh.dlt2811bean.service.svc.dataset.datatypes.CmsCreateDataSetEntry;
import com.ysh.dlt2811bean.transport.app.CmsClient;

import java.util.Map;
import java.util.Set;


public class CliContext {

    private final CmsConfig config;
    private final Map<String, CommandHandler> handlers;
    private final AutoTestHeartbeat autoTestHeartbeat = new AutoTestHeartbeat();

    private final Map<String, Map<String, Map<String, Map<String, Object>>>> cachedHierarchy;

    public CliContext(CmsConfig config, Map<String, CommandHandler> handlers,
                      Map<String, Map<String, Map<String, Map<String, Object>>>> cachedHierarchy) {
        this.config = config;
        this.handlers = handlers;
        this.cachedHierarchy = cachedHierarchy;
    }

    public CmsConfig getConfig() { return config; }
    public Map<String, CommandHandler> getHandlers() { return handlers; }
    public AutoTestHeartbeat getAutoTestHeartbeat() { return autoTestHeartbeat; }
    public Map<String, Map<String, Map<String, Map<String, Object>>>> getCachedHierarchy() { return cachedHierarchy; }

    /** Ensures an LD entry exists, returns its LN map. */
    public Map<String, Map<String, Map<String, Object>>> addLogicDevice(String ldName) {
        return cachedHierarchy.computeIfAbsent(ldName, k -> new java.util.LinkedHashMap<>());
    }

    /** Ensures an LN entry exists under the given LD, returns its ACSI-class map. */
    public Map<String, Map<String, Object>> addLogicNode(String ldName, String lnName) {
        return addLogicDevice(ldName).computeIfAbsent(lnName, k -> new java.util.LinkedHashMap<>());
    }

    /** Ensures an LN entry exists by full reference (LD/LN). */
    public void addLogicNode(String lnRef) {
        String[] parts = lnRef.split("/", 2);
        if (parts.length < 2) return;
        addLogicNode(parts[0], parts[1]);
    }

    /** Adds a member under LD/LN/ACSI-class if not already present. */
    public void addAcdMember(String lnRef, String acsiClass, String member) {
        String[] parts = lnRef.split("/", 2);
        if (parts.length < 2) return;
        Map<String, Map<String, Object>> acsiMap = addLogicNode(parts[0], parts[1]);
        acsiMap.putIfAbsent(acsiClass, new java.util.LinkedHashMap<>());
        acsiMap.get(acsiClass).putIfAbsent(member, null);
    }

    /** Ensures a DATA_OBJECT map exists under the given LN, returns it. */
    public Map<String, Object> addDataObjectGroup(String ldName, String lnName) {
        return addLogicNode(ldName, lnName).computeIfAbsent("DATA_OBJECT", k -> new java.util.LinkedHashMap<>());
    }

    public Map<String, Object> addDataObjectGroup(String target) {
        String[] parts = target.split("/", 2);
        return addDataObjectGroup(parts[0], parts[1]);
    }

    /** Ensures a DO entry exists under the given DATA_OBJECT group, returns its FC map. */
    @SuppressWarnings("unchecked")
    public Map<String, Object> addDataObject(Map<String, Object> dataObjectGroup, String doName) {
        return (Map<String, Object>) dataObjectGroup.computeIfAbsent(doName, k -> new java.util.LinkedHashMap<>());
    }

    /** Ensures an FC entry exists under DATA_OBJECT/DO, returns its DA map. */
    @SuppressWarnings("unchecked")
    public Map<String, Object> addDataObjectFc(Map<String, Object> doMap, String fc) {
        return (Map<String, Object>) doMap.computeIfAbsent(fc, k -> new java.util.LinkedHashMap<>());
    }

    /** Sets the type for a DA under DATA_OBJECT/DO/FC. Creates the {type, value} map if absent. */
    @SuppressWarnings("unchecked")
    public void addDataObjectType(Map<String, Object> dataObjectGroup, String doName, String fc, String daName, String typeName) {
        Map<String, Object> doMap = addDataObject(dataObjectGroup, doName);
        Map<String, Object> fcMap = addDataObjectFc(doMap, fc);
        Map<String, Object> daMap = (Map<String, Object>) fcMap.computeIfAbsent(daName, k -> {
            Map<String, Object> m = new java.util.LinkedHashMap<>();
            m.put("type", typeName);
            m.put("value", null);
            return m;
        });
        if (daMap.get("type") == null || "?".equals(daMap.get("type"))) {
            daMap.put("type", typeName);
        }
    }

    /** Adds a data definition entry into the cache under the given LN reference. */
    public void addDataDefinition(String lnRef, CmsDataDefinitionEntry entry) {
        String[] parts = lnRef.split("/", 2);
        if (parts.length < 2) return;
        Map<String, Object> dataObjectGroup = addDataObjectGroup(parts[0], parts[1]);
        String doName = entry.reference().get();
        CmsDataDefinition def = entry.definition();
        if (def != null && def.getStructureEntries() != null) {
            def.getStructureEntries().forEach(se -> {
                String fc = se.fc.get();
                String daName = se.name.get();
                String typeName = se.type != null ? se.type.typeName() : "?";
                addDataObjectType(dataObjectGroup, doName, fc, daName, typeName);
            });
        }
    }

    /** Sets the value for a DA under DATA_OBJECT/DO/FC. Creates the {type, value} map with type=? if absent. */
    @SuppressWarnings("unchecked")
    public void addDataObjectValue(Map<String, Object> dataObjectGroup, String doName, String fc, String daName, String value) {
        Map<String, Object> doMap = addDataObject(dataObjectGroup, doName);
        Map<String, Object> fcMap = addDataObjectFc(doMap, fc);
        Map<String, Object> daMap = (Map<String, Object>) fcMap.computeIfAbsent(daName, k -> {
            Map<String, Object> m = new java.util.LinkedHashMap<>();
            m.put("type", "?");
            m.put("value", null);
            return m;
        });
        daMap.put("value", value);
    }

    /** Sets the value for a DA under DATA_OBJECT/DO, auto-resolving the FC
     * by searching existing DO/FC/DA entries. If no FC is found, uses "". */
    @SuppressWarnings("unchecked")
    public void addDataObjectValue(Map<String, Object> dataObjectGroup, String doName, String daName, String value) {
        Map<String, Object> doMap = addDataObject(dataObjectGroup, doName);
        String fc = resolveFc(doMap, daName);
        Map<String, Object> fcMap = addDataObjectFc(doMap, fc);
        Map<String, Object> daMap = (Map<String, Object>) fcMap.computeIfAbsent(daName, k -> {
            Map<String, Object> m = new java.util.LinkedHashMap<>();
            m.put("type", "?");
            m.put("value", null);
            return m;
        });
        daMap.put("value", value);
    }

    /** Sets the value for a DA by full reference like "C1/MMXU1.Volts.mag.f".
     * Parses the reference and delegates to addDataObjectValue(Map, String, String, String). */
    public void addDataObjectValue(String ref, String value) {
        if (!ref.contains("/") || !ref.contains(".")) return;
        String[] parts = ref.split("\\.");
        if (parts.length < 2) return;
        String[] ldLn = parts[0].split("/", 2);
        if (ldLn.length < 2) return;
        String doDa = ref.substring(ref.indexOf('.') + 1);
        int dotIdx = doDa.indexOf('.');
        String doName = dotIdx > 0 ? doDa.substring(0, dotIdx) : doDa;
        String daName = dotIdx > 0 ? doDa.substring(dotIdx + 1) : "";
        addDataObjectValue(addDataObjectGroup(ldLn[0], ldLn[1]), doName, daName, value);
    }

    /** Searches a DO map for which FC contains the given DA name. Returns "" if not found. */
    private static String resolveFc(Map<String, Object> doMap, String daName) {
        for (Map.Entry<String, Object> entry : doMap.entrySet()) {
            Object val = entry.getValue();
            if (val instanceof Map) {
                Map<?, ?> fcMap = (Map<?, ?>) val;
                if (fcMap.containsKey(daName)) {
                    return entry.getKey();
                }
            }
        }
        return "";
    }

    /** Ensures a DATA_SET map exists under the given LN, returns it. */
    public Map<String, Object> addDataSetGroup(String ldName, String lnName) {
        return addLogicNode(ldName, lnName).computeIfAbsent("DATA_SET", k -> new java.util.LinkedHashMap<>());
    }

    /**
     * Updates a SGCB attribute in the cache under the LN's SGCB group.
     * The sgcbRef format is "LD/LN.SGCB", attribute is stored as "attrName=value".
     */
    @SuppressWarnings("unchecked")
    public void updateSgcbAttribute(String sgcbRef, String attrName, String value) {
        String[] parts = sgcbRef.split("/", 2);
        if (parts.length < 2) return;
        String ldName = parts[0];
        String rest = parts[1];
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return;
        String lnName = rest.substring(0, dotIdx);
        String sgName = rest.substring(dotIdx + 1);
        if (sgName.isEmpty()) return;
        Map<String, Object> sgcbGroup = addLogicNode(ldName, lnName)
                .computeIfAbsent("SGCB", k -> new java.util.LinkedHashMap<>());
        Map<String, Object> attrMap = (Map<String, Object>) sgcbGroup.computeIfAbsent(sgName, k -> new java.util.LinkedHashMap<>());
        attrMap.put(attrName, value);
    }

    /** Updates a BRCB attribute in the cache under the LN's BRCB group. */
    @SuppressWarnings("unchecked")
    public void updateBrcbAttribute(String ref, String attrName, String value) {
        String[] parts = ref.split("/", 2);
        if (parts.length < 2) return;
        String[] lnParts = parts[1].split("\\.");
        if (lnParts.length < 2) return;
        Map<String, Object> brcbGroup = addLogicNode(parts[0], lnParts[0])
                .computeIfAbsent("BRCB", k -> new java.util.LinkedHashMap<>());
        Map<String, Object> attrMap = (Map<String, Object>) brcbGroup.computeIfAbsent(lnParts[1], k -> new java.util.LinkedHashMap<>());
        attrMap.put(attrName, value);
    }

    /** Updates a URCB attribute in the cache under the LN's URCB group. */
    @SuppressWarnings("unchecked")
    public void updateUrcbAttribute(String ref, String attrName, String value) {
        String[] parts = ref.split("/", 2);
        if (parts.length < 2) return;
        String[] lnParts = parts[1].split("\\.");
        if (lnParts.length < 2) return;
        Map<String, Object> urcbGroup = addLogicNode(parts[0], lnParts[0])
                .computeIfAbsent("URCB", k -> new java.util.LinkedHashMap<>());
        Map<String, Object> attrMap = (Map<String, Object>) urcbGroup.computeIfAbsent(lnParts[1], k -> new java.util.LinkedHashMap<>());
        attrMap.put(attrName, value);
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
                String lnRef = ldName + "/" + lnName;
                CommandHandler getAllDef = handlers.get("get-all-def");
                if (getAllDef != null) {
                    try {
                        getAllDef.execute(client, Map.of("target", lnRef, "fc", "XX"));
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-all-def " + lnRef + ": " + e.getMessage()));
                    }
                }
                CommandHandler getAllValues = handlers.get("get-all-values");
                if (getAllValues != null) {
                    try {
                        getAllValues.execute(client, Map.of("target", lnRef, "fc", "", "referenceAfter", ""));
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-all-values " + lnRef + ": " + e.getMessage()));
                    }
                }
                CommandHandler lnDir = handlers.get("ln-dir");
                if (lnDir != null) { // no 1 and 9 !!
                    for (String acsi : new String[]{"DATA_SET", "BRCB", "URCB", "LCB", "LOG", "SGCB", "GO_CB", "MSV_CB"}) {
                        try {
                            lnDir.execute(client, Map.of("target", lnRef, "acsi", acsi, "referenceAfter", ""));
                        } catch (Exception e) {
                            System.out.println(CmsColor.red("  Auto-discovery failed at ln-dir " + acsi + " " + lnRef + ": " + e.getMessage()));
                        }
                    }
                }
                CommandHandler getAllCb = handlers.get("get-all-cb");
                if (getAllCb != null) {
                    try {
                        getAllCb.execute(client, Map.of("target", lnRef, "type", "SGCB"));
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-all-cb SGCB " + lnRef + ": " + e.getMessage()));
                    }
                    try {
                        getAllCb.execute(client, Map.of("target", lnRef, "type", "BRCB"));
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-all-cb BRCB " + lnRef + ": " + e.getMessage()));
                    }
                    try {
                        getAllCb.execute(client, Map.of("target", lnRef, "type", "URCB"));
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-all-cb URCB " + lnRef + ": " + e.getMessage()));
                    }
                }
            }
        }
        resolveDataSetMembers(client);
    }

    private void resolveDataSetMembers(CmsClient client) {
        for (Map.Entry<String, Map<String, Map<String, Map<String, Object>>>> ldEntry : cachedHierarchy.entrySet()) {
            String ldName = ldEntry.getKey();
            for (Map.Entry<String, Map<String, Map<String, Object>>> lnEntry : ldEntry.getValue().entrySet()) {
                String lnName = lnEntry.getKey();
                Map<String, Object> dataSetMap = lnEntry.getValue().get("DATA_SET");
                if (dataSetMap == null) continue;
                for (String dsName : dataSetMap.keySet()) {
                    String dsRef = ldName + "/" + lnName + "." + dsName;
                    try {
                        CmsGetDataSetDirectory dsDirAsdu = new CmsGetDataSetDirectory(MessageType.REQUEST).datasetReference(dsRef);
                        CmsApdu dsDirResp = client.send(dsDirAsdu);
                        if (dsDirResp.getMessageType() == MessageType.RESPONSE_POSITIVE) {
                            com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory dsDir =
                                (com.ysh.dlt2811bean.service.svc.dataset.CmsGetDataSetDirectory) dsDirResp.getAsdu();
                            java.util.List<CmsCreateDataSetEntry> members = dsDir.memberData.toList();
                            Map<String, Object> orderedMembers = new java.util.LinkedHashMap<>();
                            for (int i = 0; i < members.size(); i++) {
                                CmsCreateDataSetEntry member = members.get(i);
                                String memberRef = member.reference.get();
                                String memberFc = member.fc.get();
                                Map<String, Object> memberMap = new java.util.LinkedHashMap<>();
                                memberMap.put("FC", memberFc);
                                int slashIdx = memberRef.indexOf('/');
                                int dotIdx = memberRef.indexOf('.');
                                if (slashIdx >= 0 && dotIdx > slashIdx) {
                                    String targetLd = memberRef.substring(0, slashIdx);
                                    String targetLn = memberRef.substring(slashIdx + 1, dotIdx);
                                    String targetDo = memberRef.substring(dotIdx + 1);
                                    Map<String, Object> targetDoMap = addDataObjectGroup(targetLd, targetLn);
                                    Object ref = targetDoMap.get(targetDo);
                                    if (ref instanceof Map) {
                                        memberMap.put("DO", ref);
                                    }
                                }
                                orderedMembers.put(String.valueOf(i), memberMap);
                            }
                            dataSetMap.put(dsName, orderedMembers);
                        }
                    } catch (Exception e) {
                        System.out.println(CmsColor.red("  Auto-discovery failed at get-dataset-dir " + dsRef + ": " + e.getMessage()));
                    }
                }
            }
        }
    }
}
