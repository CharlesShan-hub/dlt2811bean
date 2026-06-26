package com.ysh.jcms.app.node;

import java.util.*;

public class ContentManager {

    private String sapRef;
    private final Set<String> ldNames = new HashSet<>();
    private final Set<String> lnNames = new HashSet<>();
    private final Set<String> dataRefs = new HashSet<>();
    private final Set<String> dataSetRefs = new HashSet<>();
    private final Map<Integer, Set<String>> lnRefsByAcsiClass = new HashMap<>();

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
        Set<String> set = lnRefsByAcsiClass.computeIfAbsent(acsiClass, k -> new HashSet<>());
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