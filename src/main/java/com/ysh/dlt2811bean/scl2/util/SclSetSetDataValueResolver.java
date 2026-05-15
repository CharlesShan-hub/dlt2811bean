package com.ysh.dlt2811bean.scl2.util;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.scl2.model.SclDO;
import com.ysh.dlt2811bean.scl2.model.SclDOType;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclLNodeType;
import com.ysh.dlt2811bean.scl2.model.SclDAI;
import com.ysh.dlt2811bean.scl2.model.SclDOI;
import com.ysh.dlt2811bean.scl2.model.SclSDI;
import com.ysh.dlt2811bean.scl2.model.SclServer;

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

        dai.setVal(value);
        return CmsServiceError.NO_ERROR;
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
