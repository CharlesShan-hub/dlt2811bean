package com.ysh.jcms.utils.scl.util;

import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.utils.scl.model.template.SclDO;
import com.ysh.jcms.utils.scl.model.template.SclDOType;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.template.SclLNodeType;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;
import com.ysh.jcms.utils.scl.model.ied.SclServer;

public class SclSetSetDataValueResolver {

    // -------------------------------------------------------------------------
    // Data value setting (for SetDataValues service)
    // -------------------------------------------------------------------------

    /**
     * Sets a data value identified by a reference string like "LD0/LLN0.DO.DA"
     * or "LD0/LLN0.DO.SDI.BDA".
     * <p>If the DAI does not exist, it will be created virtually (including DOI/SDI if needed).
     *
     * @param ref       the data reference (e.g. "LD0/LLN0.Pos.stVal")
     * @param value     the string value to set
     * @param templates the data type templates (may be null)
     * @return CmsServiceError.NO_ERROR on success, or an error code on failure
     */
    public static int setDataValue(SclServer server, String ref, String value, SclDataTypeTemplates templates) {
        if (ref == null || ref.isEmpty()) return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 2) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        String lnName = parts[0];
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return CmsServiceError.INSTANCE_NOT_AVAILABLE;

        String doName = parts[1];
        SclDOI doi = ln.findDoiByName(doName);
        if (doi == null) {
            if (templates == null) return CmsServiceError.INSTANCE_NOT_AVAILABLE;
            doi = new SclDOI();
            doi.setName(doName);
            ln.addDoi(doi);
        }

        SclDAI dai;
        if (parts.length == 2) {
            // DO-level: find or create first DA from type templates
            String firstDaName = findFirstDaName(templates, ln, doName);
            if (firstDaName == null) firstDaName = "stVal";
            dai = doi.findDaiByName(firstDaName);
            if (dai == null) {
                dai = new SclDAI();
                dai.setName(firstDaName);
                doi.addDai(dai);
            }
        } else if (parts.length == 3) {
            // LD/LN.DO.DA
            String daName = parts[2];
            dai = doi.findDaiByName(daName);
            if (dai == null) {
                dai = new SclDAI();
                dai.setName(daName);
                doi.addDai(dai);
            }
        } else if (parts.length == 4) {
            // LD/LN.DO.SDI.BDA
            String sdiName = parts[2];
            String bdaName = parts[3];
            SclSDI sdi = doi.findSdiByName(sdiName);
            if (sdi == null) {
                sdi = new SclSDI();
                sdi.setName(sdiName);
                doi.addSdi(sdi);
            }
            dai = sdi.findDaiByName(bdaName);
            if (dai == null) {
                dai = new SclDAI();
                dai.setName(bdaName);
                sdi.addDai(dai);
            }
        } else {
            return CmsServiceError.INSTANCE_NOT_AVAILABLE;
        }

        // Validate and canonicalize value against DA's bType before storing
        String bType = resolveDaBType(templates, ln, doName, dai.getName(), parts);
        if (bType != null) {
            String validated = validateAndConvert(value, bType);
            if (validated == null) {
                return CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT;
            }
            dai.setVal(validated);
        } else {
            dai.setVal(value);
        }
        return CmsServiceError.NO_ERROR;
    }

    /** Resolve the bType for the target DA or BDA. */
    private static String resolveDaBType(SclDataTypeTemplates templates, SclLN ln,
                                          String doName, String daName, String[] parts) {
        if (templates == null || ln.getLnType() == null) return null;
        if (parts.length == 4) {
            // LD/LN.DO.SDI.BDA → use resolveSdiBdaBType
            return SclDataValueResolver.resolveSdiBdaBType(templates, ln, doName, parts[2], daName);
        }
        // LD/LN.DO.DA or LD/LN.DO → use resolveDaBType
        return SclDataValueResolver.resolveDaBType(templates, ln, doName, daName);
    }

    /** Validate and canonicalize value according to bType. Returns null if invalid. */
    private static String validateAndConvert(String value, String bType) {
        if (value == null) return null;
        switch (bType.toUpperCase()) {
            case "BOOLEAN":
                if ("true".equalsIgnoreCase(value) || "1".equals(value)) return "true";
                if ("false".equalsIgnoreCase(value) || "0".equals(value)) return "false";
                return null; // invalid boolean
            case "INT8":
                try { Byte.parseByte(value); return value; } catch (NumberFormatException e) { return null; }
            case "INT16":
                try { Short.parseShort(value); return value; } catch (NumberFormatException e) { return null; }
            case "INT32":
            case "ENUM":
            case "ENUMERATED":
            case "CODED_ENUM":
                try { Integer.parseInt(value); return value; } catch (NumberFormatException e) { return null; }
            case "INT64":
                try { Long.parseLong(value); return value; } catch (NumberFormatException e) { return null; }
            case "INT8U":
                try { int v = Short.parseShort(value); if (v >= 0 && v <= 255) return Integer.toString(v); return null; } catch (NumberFormatException e) { return null; }
            case "INT16U":
                try { int v = Integer.parseInt(value); if (v >= 0 && v <= 65535) return Integer.toString(v); return null; } catch (NumberFormatException e) { return null; }
            case "INT32U":
                try { long v = Long.parseLong(value); if (v >= 0 && v <= 0xFFFFFFFFL) return Long.toString(v); return null; } catch (NumberFormatException e) { return null; }
            case "FLOAT32":
                try { Float.parseFloat(value); return value; } catch (NumberFormatException e) { return null; }
            case "FLOAT64":
                try { Double.parseDouble(value); return value; } catch (NumberFormatException e) { return null; }
            default:
                // Unicode255, VisString255, etc. — any string is valid
                return value;
        }
    }

    private static String findFirstDaName(SclDataTypeTemplates templates, SclLN ln, String doName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null || doType.getDas().isEmpty()) return null;
        return doType.getDas().get(0).getName();
    }

}
