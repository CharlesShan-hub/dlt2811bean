package com.ysh.jcms.app.node;

import java.util.*;

public class ContentManager {

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

    public synchronized void initAllData(List<AllDataEntry> entries) {
        this.allDataEntries = entries;
    }

    /** Append all-data entries (for auto-pull pagination). */
    public synchronized void addAllData(List<AllDataEntry> entries) {
        this.allDataEntries.addAll(entries);
    }

    public synchronized List<AllDataEntry> allDataEntries() {
        return Collections.unmodifiableList(new ArrayList<>(allDataEntries));
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

    public synchronized void initDataDef(List<DataDefEntry> entries) {
        this.dataDefEntries = entries;
    }

    /** Append data-def entries (for auto-pull pagination). */
    public synchronized void addDataDef(List<DataDefEntry> entries) {
        this.dataDefEntries.addAll(entries);
    }

    public synchronized List<DataDefEntry> dataDefEntries() {
        return Collections.unmodifiableList(new ArrayList<>(dataDefEntries));
    }

    public synchronized void initServerDir(List<String> ldNames) {
        this.ldNames.clear();
        this.ldNames.addAll(ldNames);
    }

    public synchronized void initLdDir(List<String> lnNames) {
        this.lnNames.clear();
        this.lnNames.addAll(lnNames);
    }

    public synchronized void initDataRefs(List<String> refs) {
        this.dataRefs.clear();
        this.dataRefs.addAll(refs);
    }

    public synchronized void initDataSets(List<String> refs) {
        this.dataSetRefs.clear();
        this.dataSetRefs.addAll(refs);
    }

    public synchronized void initNodeDir(int acsiClass, List<String> refs) {
        Set<String> set = lnRefsByAcsiClass.computeIfAbsent(acsiClass, k -> new LinkedHashSet<>());
        set.clear();
        set.addAll(refs);
    }

    public synchronized Set<String> ldNames() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(ldNames));
    }
    public synchronized Set<String> lnNames() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(lnNames));
    }
    public synchronized Set<String> dataRefs() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(dataRefs));
    }
    public synchronized Set<String> dataSetRefs() {
        return Collections.unmodifiableSet(new LinkedHashSet<>(dataSetRefs));
    }

    /**
     * Get references by ACSIClass (from GetLogicalNodeDirectory results). Returns
     * empty set if no results for the given class.
     */
    public synchronized Set<String> nodeRefs(int acsiClass) {
        Set<String> set = lnRefsByAcsiClass.get(acsiClass);
        return set != null ? Collections.unmodifiableSet(new LinkedHashSet<>(set)) : Collections.emptySet();
    }
}
