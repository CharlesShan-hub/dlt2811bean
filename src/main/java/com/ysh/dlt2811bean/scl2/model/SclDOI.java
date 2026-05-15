package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDOI {

    private String name;
    private String desc;
    private final List<SclDAI> dais = new ArrayList<>();
    private final List<SclSDI> sdis = new ArrayList<>();

    public void addDai(SclDAI dai) { this.dais.add(dai); }

    public void addSdi(SclSDI sdi) { this.sdis.add(sdi); }

    public SclDAI findDaiByName(String name) {
        for (SclDAI dai : dais) {
            if (dai.getName().equals(name)) return dai;
        }
        return null;
    }

    public SclSDI findSdiByName(String name) {
        for (SclSDI sdi : sdis) {
            if (sdi.getName().equals(name)) return sdi;
        }
        return null;
    }

    // -------------------------------------------------------------------------
    // Data directory collection (for GetDataDirectory service)
    // -------------------------------------------------------------------------

    /**
     * Collects data directory entries at the DO level: lists all DA names and SDI sub-entries.
     * <p>Merges instance DAIs/SDIs with type template DAs/SDOs, instance takes priority.
     *
     * @param templates the data type templates for type-based DA/SDO listing
     * @param ln        the parent LN (for resolving the DO type)
     * @return list of directory entries (ref = DA/SDI name, fc = functional constraint)
     */
    public List<SclDataDirectoryEntry> collectDataDirectory(SclDataTypeTemplates templates, SclLN ln) {
        java.util.Set<String> seen = new java.util.HashSet<>();
        List<SclDataDirectoryEntry> entries = new ArrayList<>();

        for (SclDAI dai : dais) {
            String daName = dai.getName();
            seen.add(daName);
            String fc = resolveDaFc(templates, ln, name, daName);
            entries.add(new SclDataDirectoryEntry(daName, fc));
        }

        for (SclSDI sdi : sdis) {
            String sdiName = sdi.getName();
            seen.add(sdiName);
            entries.add(new SclDataDirectoryEntry(sdiName, null));
        }

        // Add type template DAs/SDOs not present in instance
        if (templates != null && ln != null && ln.getLnType() != null && !ln.getLnType().isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
            if (lnt != null) {
                SclDO doDef = lnt.findDoByName(name);
                if (doDef != null && doDef.getType() != null) {
                    SclDOType doType = templates.findDoTypeById(doDef.getType());
                    if (doType != null) {
                        for (SclDA da : doType.getDas()) {
                            if (!seen.contains(da.getName())) {
                                seen.add(da.getName());
                                entries.add(new SclDataDirectoryEntry(da.getName(), da.getFc()));
                            }
                        }
                        for (SclSDO sdo : doType.getSdos()) {
                            if (!seen.contains(sdo.getName())) {
                                seen.add(sdo.getName());
                                entries.add(new SclDataDirectoryEntry(sdo.getName(), null));
                            }
                        }
                    }
                }
            }
        }

        return entries;
    }

    private static String resolveDaFc(SclDataTypeTemplates templates, SclLN ln, String doName, String daName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return null;
        SclDA da = doType.findDaByName(daName);
        return da != null ? da.getFc() : null;
    }
}
