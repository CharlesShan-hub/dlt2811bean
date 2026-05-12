package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDA;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDO;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDOType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclLNodeType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclDAType;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.SclBDA;
import com.ysh.dlt2811bean.scl.model.SclIED;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLN;
import com.ysh.dlt2811bean.scl.model.SclIED.SclLDevice;
import com.ysh.dlt2811bean.scl.model.SclIED.SclServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility for resolving SCL type template information.
 *
 * <p>When instance data ({@code <DOI>}/{@code <DAI>}) is incomplete, this class
 * traverses the type template chain ({@code LNodeType → DO → DOType → DA → DAType → BDA})
 * to find DOs, DAs, and their bTypes.
 */
public class SclTypeResolver {

    private SclTypeResolver() {
        // utility class
    }

    // ==================== LN lookup ====================

    /**
     * Finds an LN (SclLN or SclLN0) in a device by its combined name (lnClass + inst).
     */
    public static SclIED.SclLN findLnInDevice(SclLDevice device, String lnName) {
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnName)) {
                // Wrap SclLN0 as SclLN-compatible — SclLN0 has lnType too
                return new SclLNWrapper(device.getLn0());
            }
        }
        for (SclLN ln : device.getLns()) {
            String curLnName = ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                return ln;
            }
        }
        return null;
    }

    /**
     * Gets the lnType from an LN (handles both SclLN and SclLN0).
     */
    public static String getLnType(SclIED.SclLN ln) {
        return ln.getLnType();
    }

    // ==================== DO lookup ====================

    /**
     * Finds a DO in the type templates for a given LN.
     *
     * @param server    the SCL server model
     * @param templates the data type templates
     * @param ldName    logical device name (e.g. "C1")
     * @param lnName    logical node name (e.g. "CSWI1")
     * @param doName    data object name (e.g. "Pos")
     * @return the SclDO if found, null otherwise
     */
    public static SclDO findDoInType(SclServer server, SclDataTypeTemplates templates,
                                     String ldName, String lnName, String doName) {
        SclLDevice device = findLDevice(server, ldName);
        if (device == null) return null;

        SclIED.SclLN ln = findLnInDevice(device, lnName);
        if (ln == null) return null;

        String lnType = getLnType(ln);
        if (lnType == null || lnType.isEmpty()) return null;

        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        if (lnt == null) return null;

        for (SclDO doObj : lnt.getDos()) {
            if (doObj.getName().equals(doName)) {
                return doObj;
            }
        }
        return null;
    }

    /**
     * Lists all DO names for an LN from type templates.
     */
    public static List<String> listDoNamesFromType(SclServer server, SclDataTypeTemplates templates,
                                                   String ldName, String lnName) {
        SclLDevice device = findLDevice(server, ldName);
        if (device == null) return List.of();

        SclIED.SclLN ln = findLnInDevice(device, lnName);
        if (ln == null) return List.of();

        String lnType = getLnType(ln);
        if (lnType == null || lnType.isEmpty()) return List.of();

        SclLNodeType lnt = templates.findLNodeTypeById(lnType);
        if (lnt == null) return List.of();

        List<String> names = new ArrayList<>();
        for (SclDO doObj : lnt.getDos()) {
            names.add(doObj.getName());
        }
        return names;
    }

    // ==================== DA lookup ====================

    /**
     * Finds a DA in the type templates for a given DO.
     *
     * @param server    the SCL server model
     * @param templates the data type templates
     * @param ldName    logical device name
     * @param lnName    logical node name
     * @param doName    data object name
     * @param daName    data attribute name (may be in a DAType for structs)
     * @return the SclDA or SclBDA if found, null otherwise
     */
    public static Object findDaInType(SclServer server, SclDataTypeTemplates templates,
                                      String ldName, String lnName, String doName, String daName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return null;

        // First, search in direct DAs
        for (SclDA da : dot.getDas()) {
            if (da.getName().equals(daName)) {
                return da;
            }
        }

        // Then, search in SDOs (sub-data objects)
        for (SclDataTypeTemplates.SclSDO sdo : dot.getSdos()) {
            if (sdo.getName().equals(daName)) {
                return sdo;
            }
        }

        return null;
    }

    /**
     * Lists all DAs for a DO from type templates.
     */
    public static List<SclDA> listDasFromType(SclServer server, SclDataTypeTemplates templates,
                                              String ldName, String lnName, String doName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return List.of();

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return List.of();

        return new ArrayList<>(dot.getDas());
    }

    /**
     * Lists all DA names for a DO from type templates (including SDOs).
     */
    public static List<String> listDaNamesFromType(SclServer server, SclDataTypeTemplates templates,
                                                   String ldName, String lnName, String doName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return List.of();

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return List.of();

        List<String> names = new ArrayList<>();
        for (SclDA da : dot.getDas()) {
            names.add(da.getName());
        }
        for (SclDataTypeTemplates.SclSDO sdo : dot.getSdos()) {
            names.add(sdo.getName());
        }
        return names;
    }

    // ==================== bType resolution ====================

    /**
     * Resolves the bType for a DA reference by traversing the type template chain.
     *
     * <p>Supports both direct DAs and struct DAs (via DAType/BDA).
     *
     * @param server    the SCL server model
     * @param templates the data type templates
     * @param ldName    logical device name
     * @param lnName    logical node name
     * @param doName    data object name
     * @param daName    data attribute name
     * @return the bType string (e.g. "BOOLEAN", "INT32", "VisString255"), or null if not found
     */
    public static String resolveBType(SclServer server, SclDataTypeTemplates templates,
                                      String ldName, String lnName, String doName, String daName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return null;

        // Search in direct DAs
        for (SclDA da : dot.getDas()) {
            if (da.getName().equals(daName)) {
                if (da.getBType() != null && !da.getBType().isEmpty()) {
                    return da.getBType();
                }
                // If bType is a struct (e.g. "Quality"), it's actually a type reference
                // Try to resolve as DAType
                if (da.getType() != null && !da.getType().isEmpty()) {
                    SclDAType dat = templates.findDaTypeById(da.getType());
                    if (dat != null) {
                        return "STRUCT"; // It's a structured type
                    }
                }
                return da.getBType();
            }
        }

        // Search in SDOs (sub-data objects) — these are struct types
        for (SclDataTypeTemplates.SclSDO sdo : dot.getSdos()) {
            if (sdo.getName().equals(daName)) {
                // SDO type points to another DOType
                SclDOType sdoDot = templates.findDoTypeById(sdo.getType());
                if (sdoDot != null) {
                    return "STRUCT";
                }
                return null;
            }
        }

        return null;
    }

    /**
     * Resolves bType for a DA inside an SDI (sub-structure).
     * e.g. for "sVC.offset", first find DA "sVC" → get its DAType → find BDA "offset".
     */
    public static String resolveSdiBType(SclServer server, SclDataTypeTemplates templates,
                                         String ldName, String lnName, String doName,
                                         String sdiName, String bdaName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return null;

        // Find the DA that has a DAType (struct)
        for (SclDA da : dot.getDas()) {
            if (da.getName().equals(sdiName)) {
                String typeRef = da.getType();
                if (typeRef == null || typeRef.isEmpty()) {
                    // Maybe bType itself is the struct name
                    typeRef = da.getBType();
                }
                SclDAType dat = templates.findDaTypeById(typeRef);
                if (dat != null) {
                    for (SclBDA bda : dat.getBdas()) {
                        if (bda.getName().equals(bdaName)) {
                            return bda.getBType();
                        }
                    }
                }
                return null;
            }
        }

        // Also check SDOs
        for (SclDataTypeTemplates.SclSDO sdo : dot.getSdos()) {
            if (sdo.getName().equals(sdiName)) {
                SclDOType sdoDot = templates.findDoTypeById(sdo.getType());
                if (sdoDot != null) {
                    for (SclDA da : sdoDot.getDas()) {
                        if (da.getName().equals(bdaName)) {
                            return da.getBType();
                        }
                    }
                }
                return null;
            }
        }

        return null;
    }

    // ==================== CDC resolution ====================

    /**
     * Resolves the CDC (Common Data Class) for a DO.
     */
    public static String resolveCdc(SclServer server, SclDataTypeTemplates templates,
                                    String ldName, String lnName, String doName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return null;

        return dot.getCdc();
    }

    // ==================== FC resolution ====================

    /**
     * Resolves the FC (Functional Constraint) for a DA.
     */
    public static String resolveFc(SclServer server, SclDataTypeTemplates templates,
                                   String ldName, String lnName, String doName, String daName) {
        SclDO doObj = findDoInType(server, templates, ldName, lnName, doName);
        if (doObj == null) return null;

        SclDOType dot = templates.findDoTypeById(doObj.getType());
        if (dot == null) return null;

        for (SclDA da : dot.getDas()) {
            if (da.getName().equals(daName)) {
                return da.getFc();
            }
        }
        return null;
    }

    // ==================== Helper ====================

    private static SclLDevice findLDevice(SclServer server, String ldName) {
        for (SclLDevice ld : server.getLDevices()) {
            if (ld.getInst().equals(ldName)) {
                return ld;
            }
        }
        return null;
    }

    /**
     * Wraps SclLN0 as SclLN for uniform lnType access.
     */
    private static class SclLNWrapper extends SclIED.SclLN {
        private final SclIED.SclLN0 ln0;

        public SclLNWrapper(SclIED.SclLN0 ln0) {
            this.ln0 = ln0;
        }

        @Override
        public String getLnType() {
            return ln0.getLnType();
        }

        @Override
        public String getLnClass() {
            return ln0.getLnClass();
        }

        @Override
        public String getInst() {
            return ln0.getInst();
        }

        @Override
        public String getPrefix() {
            return "";
        }

        @Override
        public String getDesc() {
            return ln0.getDesc();
        }

        @Override
        public List<SclIED.SclDOI> getDois() {
            return ln0.getDois();
        }

        @Override
        public List<SclIED.SclInputs> getInputs() {
            return null;
        }
    }
}