package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.core.data.enumerate.CmsAcsiClass;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.template.SclSDO;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Directory service —— server directory (8.3.1) / logical device directory
 * (8.3.2) / logical node directory (8.3.3).
 * <p>
 * Each method returns the complete result list; paging (referenceAfter /
 * pageSize) is handled by the handler layer. See {@link SclAllValuesService}
 * for all data values/definitions/control block values, and
 * {@link SclDataDirectoryService} for the data directory.
 */
public final class SclDirectoryService {

    private SclDirectoryService() {
    }

    // ==================== Server directory (8.3.1) ====================

    /**
     * Gets the names of all logical device instances under the given IED and within
     * the scope of the given AP.
     *
     * @param ap
     *            the currently associated access point (lookup limited to this AP)
     * @return list of logical device instance names
     */
    public static List<String> getServerDirectory(SclAccessPoint ap) {
        List<String> names = new ArrayList<>();
        SclServer srv = ap.server();
        if (srv != null) {
            for (SclLDevice ld : srv.lDevices()) {
                names.add(ld.inst());
            }
        }
        return names;
    }

    // ==================== Logical device directory (8.3.2) ====================

    /**
     * Gets the logical device directory (list of LN names).
     * <p>
     * When {@code ldName} is not empty, returns the short names of all LNs under
     * that LD (e.g. {@code LLN0}, {@code PIOC1}); when {@code ldName} is empty,
     * returns the full references of all LNs under all LDs of the given AP (e.g.
     * {@code LD0/LLN0}, {@code LD0/PIOC1}).
     *
     * @param ap
     *            the currently associated access point (lookup limited to this AP)
     * @param ldName
     *            logical device instance name; when empty, returns all LNs in the
     *            station
     * @return list of LN names, or null if the LD does not exist
     */
    public static List<String> getLogicalDeviceDirectory(SclAccessPoint ap, String ldName) {
        if (ldName != null) {
            SclLDevice device = findLdInAp(ap, ldName);
            if (device == null)
                return null;
            return getLnNames(device);
        }
        return getAllLnNames(ap);
    }

    private static SclLDevice findLdInAp(SclAccessPoint ap, String ldName) {
        SclServer srv = ap.server();
        if (srv != null) {
            return srv.findLDeviceByInst(ldName);
        }
        return null;
    }

    private static List<String> getLnNames(SclLDevice device) {
        List<String> names = new ArrayList<>();
        SclLN ln0 = null;
        for (SclLN ln : device.lns()) {
            if ("LLN0".equals(ln.lnClass())) {
                ln0 = ln;
            } else {
                names.add(ln.getFullName());
            }
        }
        if (ln0 != null)
            names.add(0, ln0.getFullName());
        return names;
    }

    private static List<String> getAllLnNames(SclAccessPoint ap) {
        List<String> names = new ArrayList<>();
        SclServer srv = ap.server();
        if (srv != null) {
            for (SclLDevice ld : srv.lDevices()) {
                for (String n : getLnNames(ld)) {
                    names.add(ld.inst() + "/" + n);
                }
            }
        }
        return names;
    }

    // ==================== Logical node directory (8.3.3) ====================

    /**
     * Gets the logical node directory (collects names by ACSI class).
     * <p>
     * Content returned per ACSI class:
     * <ul>
     * <li>{@code DATA_OBJECT} — full DO reference
     * ({@code ldName/lnFullName.doName}), including SDO recursion</li>
     * <li>{@code DATA_SET} — data set names</li>
     * <li>{@code BRCB} / {@code URCB} / {@code LCB} / {@code GOCB} / {@code MSVCB}
     * — control block names</li>
     * <li>{@code LOG} — log reference names</li>
     * </ul>
     *
     * @param doc
     *            SCL document (may be null)
     * @param lns
     *            list of resolved LNs
     * @param ldName
     *            logical device name (used as prefix for DO types)
     * @param acsiClass
     *            ACSI class (see {@link CmsAcsiClass})
     * @return list of names (returns an empty list rather than null when nothing is
     *         found)
     */
    public static List<String> getLogicalNodeDirectory(SclDocument doc, List<SclLN> lns, String ldName, int acsiClass) {
        SclDataTypeTemplates templates = doc != null ? doc.dataTypeTemplates() : null;
        List<String> all = new ArrayList<>();
        for (SclLN ln : lns) {
            switch (acsiClass) {
                case CmsAcsiClass.DATA_OBJECT :
                    if (templates != null) {
                        all.addAll(getDataObjectNames(ldName, ln, templates));
                    } else {
                        for (SclDOI doi : ln.dois()) {
                            all.add(ldName + "/" + ln.getFullName() + "." + doi.name());
                        }
                    }
                    break;
                case CmsAcsiClass.DATA_SET :
                    for (SclDataSet ds : ln.dataSets()) {
                        all.add(ds.name());
                    }
                    break;
                case CmsAcsiClass.BRCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if ("true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.URCB :
                    for (SclReportControl rc : ln.reportControls()) {
                        if (!"true".equals(rc.buffered()))
                            all.add(rc.name());
                    }
                    break;
                case CmsAcsiClass.LCB :
                    for (SclLogControl lc : ln.logControls()) {
                        all.add(lc.name());
                    }
                    break;
                case CmsAcsiClass.LOG :
                    for (SclLogControl lc : ln.logControls()) {
                        String logName = lc.logName();
                        if (logName != null && !logName.isEmpty()) {
                            all.add(logName);
                        }
                    }
                    break;
                case CmsAcsiClass.GOCB :
                    for (SclGSEControl gc : ln.gseControls()) {
                        all.add(gc.name());
                    }
                    break;
                case CmsAcsiClass.MSVCB :
                    for (SclSampledValueControl sv : ln.svControls()) {
                        all.add(sv.name());
                    }
                    break;
                case CmsAcsiClass.SGCB :
                    if ("LLN0".equals(ln.lnClass())) {
                        int numOfSG = CmsConfigLoader.load().protocol().setting().numOfSG();
                        for (int i = 1; i <= numOfSG; i++) {
                            all.add("SG" + i);
                        }
                    }
                    break;
                default :
                    break;
            }
        }
        return all;
    }

    private static List<String> getDataObjectNames(String ldName, SclLN ln, SclDataTypeTemplates templates) {
        List<String> names = new ArrayList<>();
        if (templates == null || ln.lnType() == null || ln.lnType().isEmpty())
            return names;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.lnType());
        if (lnt == null)
            return names;
        String lnPrefix = ldName + "/" + ln.getFullName() + ".";
        for (SclDO doDef : lnt.dos()) {
            names.add(lnPrefix + doDef.name());
            collectSdoNames(lnPrefix + doDef.name(), doDef.type(), templates, names, new HashSet<>());
        }
        return names;
    }

    private static void collectSdoNames(String parentRef, String doTypeId, SclDataTypeTemplates templates, List<String> names,
            HashSet<String> visited) {
        if (doTypeId == null || doTypeId.isEmpty() || !visited.add(doTypeId))
            return;
        SclDOType doType = templates.findDoTypeById(doTypeId);
        if (doType == null)
            return;
        for (SclSDO sdo : doType.sdos()) {
            String ref = parentRef + "." + sdo.name();
            names.add(ref);
            collectSdoNames(ref, sdo.type(), templates, names, visited);
        }
    }
}
