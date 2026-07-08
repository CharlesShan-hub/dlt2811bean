package com.ysh.jcms.utils.scl.model.ied;

import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.lnBuilder.SclLNControlBlockCollector;
import com.ysh.jcms.utils.scl.model.lnBuilder.SclLNDataCollector;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.data.SclCBEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataDefinitionEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataDirectoryEntry;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclLN extends SclLNBase {

    public SclDOI findDoiByName(String name) {
        for (SclDOI doi : dois) {
            if (doi.getName().equals(name)) return doi;
        }
        return null;
    }

    public SclDataSet findDataSetByName(String name) {
        for (SclDataSet ds : dataSets) {
            if (ds.getName().equals(name)) return ds;
        }
        return null;
    }

    public SclReportControl findReportControlByName(String name) {
        for (SclReportControl rc : reportControls) {
            if (rc.getName().equals(name)) return rc;
        }
        return null;
    }

    public SclGSEControl findGseControlByName(String name) {
        for (SclGSEControl gc : gseControls) {
            if (gc.getName().equals(name)) return gc;
        }
        return null;
    }

    public SclSampledValueControl findSmvControlByName(String name) {
        for (SclSampledValueControl svc : svControls) {
            if (svc.getName().equals(name)) return svc;
        }
        return null;
    }

    public List<String> getDataObjectNames(SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || lnType == null || lnType.isEmpty()) return names;
        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        if (lnt == null) return names;
        // Cache DOType lookups to avoid repeated searches
        java.util.HashMap<String, SclDOType> doTypeCache = new java.util.HashMap<>();
        for (SclDO doDef : lnt.getDos()) {
            names.add(doDef.getName());
            collectSdoNames(templates, doDef.getType(), doDef.getName(), names, doTypeCache);
        }
        return names;
    }

    private void collectSdoNames(SclDataTypeTemplates templates, String doTypeId, String prefix,
                                  List<String> names, java.util.HashMap<String, SclDOType> cache) {
        if (doTypeId == null) return;
        SclDOType doType = cache.get(doTypeId);
        if (doType == null) {
            doType = templates.findDoTypeById(doTypeId);
            if (doType != null) cache.put(doTypeId, doType);
        }
        if (doType == null) return;
        for (SclSDO sdo : doType.getSdos()) {
            names.add(prefix + "." + sdo.getName());
        }
    }

    // -------------------------------------------------------------------------
    // Data Value collection (for GetAllValuesHander service)
    // -------------------------------------------------------------------------

    public List<SclDataValue> collectDataValues(SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        return SclLNDataCollector.collectDataValues(this, templates, fcFilter, relative);
    }

    // -------------------------------------------------------------------------
    // Data definition collection (for GetAllDataDefinition service)
    // -------------------------------------------------------------------------

    public List<SclDataDefinitionEntry> collectDataDefinitions(SclDataTypeTemplates templates, String fcFilter, boolean relative) {
        return SclLNDataCollector.collectDataDefinitions(this, templates, fcFilter, relative);
    }

    // -------------------------------------------------------------------------
    // Control block value collection (for GetAllCBValues service)
    // -------------------------------------------------------------------------

    public List<SclCBEntry> collectCBValues(int acsiClass) {
        return SclLNControlBlockCollector.collectCBValues(this, acsiClass);
    }

    // -------------------------------------------------------------------------
    // Data directory collection (for GetDataDirectory service)
    // -------------------------------------------------------------------------

    /**
     * Collects data directory entries at the LN level: lists all DO names.
     * <p>Merges instance DOIs with type template DOs, instance takes priority.
     *
     * @param templates the data type templates for type-based DO listing
     * @return list of directory entries (ref = DO name, fc = null)
     */
    public List<SclDataDirectoryEntry> collectDataDirectory(SclDataTypeTemplates templates) {
        java.util.Set<String> seen = new java.util.HashSet<>();
        List<SclDataDirectoryEntry> entries = new ArrayList<>();

        for (SclDOI doi : dois) {
            String name = doi.getName();
            seen.add(name);
            entries.add(new SclDataDirectoryEntry(name, null));
        }

        if (templates != null && lnType != null && !lnType.isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(lnType);
            if (lnt != null) {
                for (SclDO doDef : lnt.getDos()) {
                    if (!seen.contains(doDef.getName())) {
                        entries.add(new SclDataDirectoryEntry(doDef.getName(), null));
                        seen.add(doDef.getName());
                    }
                }
            }
        }

        return entries;
    }
}
