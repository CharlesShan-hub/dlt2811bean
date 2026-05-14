package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.datatypes.numeric.*;
import com.ysh.dlt2811bean.datatypes.string.CmsUtf8String;
import com.ysh.dlt2811bean.datatypes.string.CmsVisibleString;
import com.ysh.dlt2811bean.datatypes.type.CmsType;
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
     * Finds an LN (SclLN or SclLN0) in a device by its combined name (prefix + lnClass + inst).
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
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnName)) {
                return ln;
            }
        }
        return null;
    }

    /**
     * Parses a full reference string (e.g. "CTRL/ALMGGIO1.TotW") into an SclFCDA by
     * matching the LN part against actual LNs in the device.
     *
     * <p>This is more reliable than string parsing because LN names like "ALMGGIO1"
     * have ambiguous boundaries between prefix, lnClass, and lnInst.
     *
     * @param server the SCL server model
     * @param ref    the full reference string (e.g. "CTRL/ALMGGIO1.TotW" or "CTRL/ALMGGIO1.TotW.stVal")
     * @return the resolved SclFCDA, or null if the LN cannot be found
     */
    public static SclIED.SclFCDA parseRefToFcda(SclIED.SclServer server, String ref) {
        if (ref == null || ref.isEmpty()) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) return null;
        String lnPart = rest.substring(0, dotIdx);
        String doDaPart = rest.substring(dotIdx + 1);

        SclIED.SclLDevice device = findLDevice(server, ldName);
        if (device == null) return null;

        SclIED.SclFCDA fcda = new SclIED.SclFCDA();
        fcda.setLdInst(ldName);

        // Try LN0 first
        if (device.getLn0() != null) {
            String ln0Name = device.getLn0().getLnClass() + device.getLn0().getInst();
            if (ln0Name.equals(lnPart)) {
                fcda.setLnClass(device.getLn0().getLnClass());
                fcda.setLnInst(device.getLn0().getInst());
                fcda.setPrefix("");
                return resolveDoDaName(fcda, doDaPart);
            }
        }

        // Try each LN — match prefix + lnClass + lnInst against lnPart
        for (SclLN ln : device.getLns()) {
            String curLnName = (ln.getPrefix() == null || ln.getPrefix().isEmpty())
                    ? ln.getLnClass() + ln.getInst()
                    : ln.getPrefix() + ln.getLnClass() + ln.getInst();
            if (curLnName.equals(lnPart)) {
                fcda.setLnClass(ln.getLnClass());
                fcda.setLnInst(ln.getInst());
                fcda.setPrefix(ln.getPrefix() != null ? ln.getPrefix() : "");
                return resolveDoDaName(fcda, doDaPart);
            }
        }

        return null;
    }

    private static SclIED.SclFCDA resolveDoDaName(SclIED.SclFCDA fcda, String doDaPart) {
        int dotIdx = doDaPart.indexOf('.');
        if (dotIdx >= 0) {
            fcda.setDoName(doDaPart.substring(0, dotIdx));
            fcda.setDaName(doDaPart.substring(dotIdx + 1));
        } else {
            fcda.setDoName(doDaPart);
        }
        return fcda;
    }

    private static SclIED.SclLDevice findLDevice(SclIED.SclServer server, String ldName) {
        for (SclIED.SclLDevice device : server.getLDevices()) {
            if (device.getInst().equals(ldName)) {
                return device;
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

    // ==================== CLI convenience ====================

    /**
     * Finds the first server from any IED in the SCL document.
     */
    public static SclServer findFirstServer(SclDocument sclDocument) {
        if (sclDocument == null || sclDocument.getIeds() == null) return null;
        for (SclIED ied : sclDocument.getIeds()) {
            if (ied.getAccessPoints() != null) {
                for (SclIED.SclAccessPoint ap : ied.getAccessPoints()) {
                    if (ap.getServer() != null) {
                        return ap.getServer();
                    }
                }
            }
        }
        return null;
    }

    /**
     * Parses a reference string to resolve its bType from SCL templates.
     * Supports formats:
     * <ul>
     *   <li>LD/LN.DO (DO-level, uses first DA's type)
     *   <li>LD/LN.DO.DA (direct DA)
     *   <li>LD/LN.DO.SDI.BDA (sub-structure BDA, via DAType)
     * </ul>
     */
    public static String resolveBType(String ref, SclServer server, SclDataTypeTemplates templates) {
        if (server == null || templates == null) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 2) return null;
        String lnName = parts[0];
        String doName = parts[1];
        if (parts.length == 4) {
            // LD/LN.DO.SDI.BDA — e.g. sVC.offset
            return resolveSdiBType(server, templates, ldName, lnName, doName, parts[2], parts[3]);
        }
        if (parts.length >= 3) {
            // LD/LN.DO.DA — direct DA
            return resolveBType(server, templates, ldName, lnName, doName, parts[2]);
        }
        // LD/LN.DO — DO-level, use first DA's type
        var das = listDasFromType(server, templates, ldName, lnName, doName);
        if (das != null && !das.isEmpty()) {
            return resolveBType(server, templates, ldName, lnName, doName, das.get(0).getName());
        }
        return null;
    }

    /**
     * Resolves a value into the appropriate CmsType by looking up the bType from SCL.
     * Falls back to CmsVisibleString if type resolution fails.
     *
     * <p>This is the data-value variant (for SetDataValues), using a string fallback.
     * For control operations, see {@link #parseControlValue(CmsConfig, String, String)}.
     */
    public static CmsType<?> resolveTypedValue(CmsConfig config, String ref, String value) {
        // DO-level ref (LD/LN.DO, no DA specified) — use VisibleString, no single type fits all DAs
        if (ref != null) {
            String[] dotParts = ref.split("\\.");
            if (dotParts.length == 2) {
                return new CmsVisibleString(value).max(255);
            }
        }
        try {
            String sclPath = config.getServer().getSclFile();
            SclDocument doc = new SclReader().read(sclPath);
            SclServer server = findFirstServer(doc);
            SclDataTypeTemplates templates = doc != null ? doc.getDataTypeTemplates() : null;
            if (server == null || templates == null) {
                return new CmsVisibleString(value).max(255);
            }
            String bType = resolveBType(ref, server, templates);
            if (bType != null) {
                return createTypedValue(bType, value);
            }
        } catch (Exception e) {
            // fall through
        }
        return new CmsVisibleString(value).max(255);
    }

    /**
     * Parses a control value string into the appropriate CmsType based on SCL type resolution.
     * Falls back to CmsBoolean if SCL resolution fails.
     *
     * <p>This is the control variant (for Operate/Select/Cancel), using a boolean fallback.
     * For data values, see {@link #resolveTypedValue(CmsConfig, String, String)}.
     */
    public static CmsType<?> parseControlValue(CmsConfig config, String ref, String value) {
        try {
            String sclPath = config.getServer().getSclFile();
            SclDocument doc = new SclReader().read(sclPath);
            SclServer server = findFirstServer(doc);
            SclDataTypeTemplates templates = doc != null ? doc.getDataTypeTemplates() : null;
            if (server == null || templates == null) {
                return new CmsBoolean(value.equalsIgnoreCase("true"));
            }
            String bType = resolveBType(ref, server, templates);
            if (bType != null) {
                return createTypedValue(bType, value);
            }
        } catch (Exception e) {
            // fall through
        }
        return new CmsBoolean(value.equalsIgnoreCase("true"));
    }

    /**
     * Creates a typed CmsType value from a string value and its bType.
     * Falls back to CmsVisibleString for unknown types or parse failures.
     */
    public static CmsType<?> createTypedValue(String bType, String value) {
        if (bType == null || value == null) {
            return new CmsVisibleString(value != null ? value : "").max(255);
        }
        try {
            switch (bType) {
                case "BOOLEAN":
                case "BOOL":            return new CmsBoolean(Boolean.parseBoolean(value.trim()));
                case "INT8":            return new CmsInt8(Integer.parseInt(value.trim()));
                case "INT16":           return new CmsInt16(Integer.parseInt(value.trim()));
                case "INT32":           return new CmsInt32(Integer.parseInt(value.trim()));
                case "INT64":           return new CmsInt64(Long.parseLong(value.trim()));
                case "INT8U":           return new CmsInt8U(Integer.parseInt(value.trim()));
                case "INT16U":          return new CmsInt16U(Integer.parseInt(value.trim()));
                case "INT32U":          return new CmsInt32U(Long.parseLong(value.trim()));
                case "INT64U":          return new CmsInt64U(new java.math.BigInteger(value.trim()));
                case "FLOAT32":         return new CmsFloat32(Float.parseFloat(value.trim()));
                case "FLOAT64":         return new CmsFloat64(Double.parseDouble(value.trim()));
                case "Enum":
                case "Dbpos":
                case "Tcmd":            return new CmsInt32(Integer.parseInt(value.trim()));
                case "VisString255":
                case "VISIBLE STRING":  return new CmsVisibleString(value).max(255);
                case "Unicode255":
                case "UNICODE STRING":  return new CmsUtf8String(value).max(255);
                case "Check":           return new CmsInt32(Integer.parseInt(value.trim()));
                default:                return new CmsVisibleString(value).max(255);
            }
        } catch (Exception e) {
            return new CmsVisibleString(value).max(255);
        }
    }
}