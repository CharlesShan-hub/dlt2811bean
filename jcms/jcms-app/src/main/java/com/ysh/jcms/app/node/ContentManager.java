package com.ysh.jcms.app.node;

import java.util.*;

public class ContentManager {

    private String sapRef;
    private final Set<String> ldNames = new LinkedHashSet<>();
    private final Set<String> lnNames = new LinkedHashSet<>();
    private final Set<String> dataRefs = new LinkedHashSet<>();
    private final Set<String> dataSetRefs = new LinkedHashSet<>();
    private final Map<Integer, Set<String>> lnRefsByAcsiClass = new HashMap<>();
    private List<AllDataEntry> allDataEntries = new ArrayList<>();
    private List<DataDefEntry> dataDefEntries = new ArrayList<>();

    // ── all-data entry ──

    public static class AllDataEntry {
        public final String reference;
        public final int choiceType;
        public final String valueString;

        public AllDataEntry(String reference, int choiceType, String valueString) {
            this.reference = reference;
            this.choiceType = choiceType;
            this.valueString = valueString;
        }
    }

    public void initAllData(List<AllDataEntry> entries) {
        this.allDataEntries = entries;
    }

    public List<AllDataEntry> getAllDataEntries() {
        return Collections.unmodifiableList(allDataEntries);
    }

    // ── all-def entry ──

    public static class DataDefEntry {
        public final String reference;
        public final String cdcType;
        public final int choiceType;

        public DataDefEntry(String reference, String cdcType, int choiceType) {
            this.reference = reference;
            this.cdcType = cdcType;
            this.choiceType = choiceType;
        }
    }

    public void initDataDef(List<DataDefEntry> entries) {
        this.dataDefEntries = entries;
    }

    public List<DataDefEntry> getDataDefEntries() {
        return Collections.unmodifiableList(dataDefEntries);
    }

    public void initServerDir(String sapRef, List<String> ldNames) {
        this.sapRef = sapRef;
        this.ldNames.clear();
        this.ldNames.addAll(ldNames);
    }

    public void initLdDir(List<String> lnNames) {
        this.lnNames.clear();
        this.lnNames.addAll(lnNames);
    }

    public void initDataRefs(List<String> refs) {
        this.dataRefs.clear();
        this.dataRefs.addAll(refs);
    }

    public void initDataSets(List<String> refs) {
        this.dataSetRefs.clear();
        this.dataSetRefs.addAll(refs);
    }

    public void initNodeDir(int acsiClass, List<String> refs) {
        Set<String> set = lnRefsByAcsiClass.computeIfAbsent(acsiClass, k -> new LinkedHashSet<>());
        set.clear();
        set.addAll(refs);
    }

    public String getSapRef() { return sapRef; }
    public Set<String> getLdNames() { return Collections.unmodifiableSet(ldNames); }
    public Set<String> getLnNames() { return Collections.unmodifiableSet(lnNames); }
    public Set<String> getDataRefs() { return Collections.unmodifiableSet(dataRefs); }
    public Set<String> getDataSetRefs() { return Collections.unmodifiableSet(dataSetRefs); }

    /**
     * Get references by ACSIClass (from GetLogicalNodeDirectory results).
     * Returns empty set if no results for the given class.
     */
    public Set<String> getNodeRefs(int acsiClass) {
        Set<String> set = lnRefsByAcsiClass.get(acsiClass);
        return set != null ? Collections.unmodifiableSet(set) : Collections.emptySet();
    }
}
