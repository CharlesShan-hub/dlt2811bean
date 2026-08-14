package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.scalar.CmsFC;
import com.ysh.jcms.core.data.sequence.data.CmsSubRefEntry;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.template.SclDA;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;
import com.ysh.jcms.utils.scl.navigate.TypeChain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Data directory service (8.4.3) —— lists DOs at LN level, DAs/including fc at
 * DO level, and DAs at SDO level.
 * <p>
 * At DO level, entries from the instance ({@code doi}) and the template are
 * merged to avoid duplicates.
 */
public final class SclDataDirectoryService {

    private SclDataDirectoryService() {
    }

    /**
     * Gets the data directory (lists DOs at LN level, DAs/including fc at DO level,
     * and DAs at SDO level).
     *
     * @param doc
     *            SCL document
     * @param ln
     *            current LN
     * @param doName
     *            DO name (null = LN level)
     * @param sdoName
     *            SDO name (null = DO level, non-null = SDO level)
     * @param doi
     *            DO instance (null = template only, used at DO level)
     * @return list of directory entries
     */
    public static List<CmsSubRefEntry> getDataDirectory(SclDocument doc, SclLN ln, String doName, String sdoName, SclDOI doi) {
        if (doName == null) {
            return collectLnDirectory(doc, ln);
        } else if (sdoName != null) {
            return collectSdoDirectory(doc, ln, doName, sdoName);
        } else if (doi != null) {
            return collectDoDirectory(doc, doi, ln);
        } else {
            return collectDoDirectoryFromTemplate(doc, ln, doName);
        }
    }

    /** LN level: lists DO names (merging instance + template). */
    private static List<CmsSubRefEntry> collectLnDirectory(SclDocument doc, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<CmsSubRefEntry> entries = new ArrayList<>();

        for (SclDOI doi : ln.dois()) {
            String name = doi.name();
            seen.add(name);
            entries.add(new CmsSubRefEntry().reference(name));
        }

        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates != null && ln.lnType() != null && !ln.lnType().isEmpty()) {
            SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
            if (lnt != null) {
                for (SclDO doDef : lnt.dos()) {
                    if (!seen.contains(doDef.name())) {
                        entries.add(new CmsSubRefEntry().reference(doDef.name()));
                        seen.add(doDef.name());
                    }
                }
            }
        }

        return entries;
    }

    /** DO level: lists DA/SDI names (merging instance + template), including FC. */
    private static List<CmsSubRefEntry> collectDoDirectory(SclDocument doc, SclDOI doi, SclLN ln) {
        Set<String> seen = new HashSet<>();
        List<CmsSubRefEntry> entries = new ArrayList<>();

        String doName = doi.name();

        // Instance DAIs
        for (SclDAI dai : doi.dais()) {
            String daName = dai.name();
            seen.add(daName);
            String fc = resolveDaFc(doc, ln, doName, daName);
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(daName);
            if (fc != null && !fc.isEmpty())
                entry.fc(CmsFC.fromCodeOr(fc, CmsFC.XX));
            entries.add(entry);
        }

        // Instance SDIs
        for (SclSDI sdi : doi.sdis()) {
            String sdiName = sdi.name();
            seen.add(sdiName);
            entries.add(new CmsSubRefEntry().reference(sdiName));
        }

        // Template DAs/SDOs not in instance
        addTemplateDirs(doc, ln, doName, seen, entries);

        return entries;
    }

    /** DO level (template fallback only). */
    private static List<CmsSubRefEntry> collectDoDirectoryFromTemplate(SclDocument doc, SclLN ln, String doName) {
        List<CmsSubRefEntry> entries = new ArrayList<>();
        addTemplateDirs(doc, ln, doName, new HashSet<>(), entries);
        return entries;
    }

    /**
     * Appends DA/SDO directory entries from the DOType template (skipping existing
     * ones).
     */
    private static void addTemplateDirs(SclDocument doc, SclLN ln, String doName, Set<String> seen, List<CmsSubRefEntry> entries) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return;
        SclDOType doType = TypeChain.of(templates).from(ln.lnType()).doDef(doName).doType();
        if (doType == null)
            return;
        for (SclDA da : doType.das()) {
            if (!seen.contains(da.name())) {
                seen.add(da.name());
                CmsSubRefEntry entry = new CmsSubRefEntry().reference(da.name());
                if (da.fc() != null && !da.fc().isEmpty())
                    entry.fc(CmsFC.fromCodeOr(da.fc(), CmsFC.XX));
                entries.add(entry);
            }
        }
        for (SclSDO sdo : doType.sdos()) {
            if (!seen.contains(sdo.name())) {
                seen.add(sdo.name());
                entries.add(new CmsSubRefEntry().reference(sdo.name()));
            }
        }
    }

    /** SDO level: lists the DAs in the SDO's DOType. */
    private static List<CmsSubRefEntry> collectSdoDirectory(SclDocument doc, SclLN ln, String doName, String sdoName) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return null;
        SclDOType doType = TypeChain.of(templates).from(ln.lnType()).doDef(doName).doType();
        if (doType == null)
            return null;
        SclSDO sdo = doType.findSdoByName(sdoName);
        if (sdo == null || sdo.type() == null)
            return null;
        SclDOType sdoType = templates.findDoTypeById(sdo.type());
        if (sdoType == null)
            return null;
        List<CmsSubRefEntry> entries = new ArrayList<>();
        for (SclDA da : sdoType.das()) {
            CmsSubRefEntry entry = new CmsSubRefEntry().reference(da.name());
            if (da.fc() != null && !da.fc().isEmpty())
                entry.fc(CmsFC.fromCodeOr(da.fc(), CmsFC.XX));
            entries.add(entry);
        }
        return entries;
    }

    /** Resolves the FC of a DA from the DOType. */
    private static String resolveDaFc(SclDocument doc, SclLN ln, String doName, String daName) {
        SclDataTypeTemplates templates = doc.dataTypeTemplates();
        if (templates == null || ln.lnType() == null)
            return null;
        TypeChain.DaStep daStep = TypeChain.of(templates).from(ln.lnType()).doDef(doName).daDef(daName);
        return daStep != null ? daStep.fc() : null;
    }
}
