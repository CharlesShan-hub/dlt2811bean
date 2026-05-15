package com.ysh.dlt2811bean.scl2.util;

import com.ysh.dlt2811bean.scl2.model.SclDataValue;
import com.ysh.dlt2811bean.scl2.model.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl2.model.SclLDevice;
import com.ysh.dlt2811bean.scl2.model.SclLN;
import com.ysh.dlt2811bean.scl2.model.SclDOI;
import com.ysh.dlt2811bean.scl2.model.SclDAI;
import com.ysh.dlt2811bean.scl2.model.SclLNodeType;
import com.ysh.dlt2811bean.scl2.model.SclDOType;
import com.ysh.dlt2811bean.scl2.model.SclDO;
import com.ysh.dlt2811bean.scl2.model.SclBDA;
import com.ysh.dlt2811bean.scl2.model.SclSDI;
import com.ysh.dlt2811bean.scl2.model.SclDA;
import com.ysh.dlt2811bean.scl2.model.SclDAType;
import com.ysh.dlt2811bean.scl2.model.SclServer;


public class SclDataValueResolver {
    // -------------------------------------------------------------------------
    // Data value resolution (for GetDataValues service)
    // -------------------------------------------------------------------------

    /**
     * Resolves a single data value from a reference string like "LD0/LLN0.DO.DA"
     * or "LD0/LLN0.DO.SDI.BDA".
     * <p>Looks up the DAI instance value and resolves the bType from the data type templates.
     *
     * @param ref       the data reference (e.g. "LD0/LLN0.Pos.stVal")
     * @param templates the data type templates for bType resolution
     * @return the data value with resolved bType, or null if not found
     */
    public static SclDataValue resolveDataValue(SclServer server, String ref, SclDataTypeTemplates templates) {
        if (ref == null || ref.isEmpty()) return null;
        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) return null;
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        String[] parts = rest.split("\\.");
        if (parts.length < 2) return null;

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) return null;

        String lnName = parts[0];
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return null;

        String doName = parts[1];
        SclDOI doi = ln.findDoiByName(doName);
        if (doi == null) return null;

        if (parts.length == 2) {
            // DO-level: find first DAI with a value
            for (SclDAI dai : doi.getDais()) {
                if (dai.getVal() != null && !dai.getVal().isEmpty()) {
                    String bType = resolveDaBType(templates, ln, doName, dai.getName());
                    return new SclDataValue(ref, dai.getVal(), bType);
                }
            }
            return null;
        }

        if (parts.length == 3) {
            // LD/LN.DO.DA
            String daName = parts[2];
            SclDAI dai = doi.findDaiByName(daName);
            if (dai != null && dai.getVal() != null && !dai.getVal().isEmpty()) {
                String bType = resolveDaBType(templates, ln, doName, daName);
                return new SclDataValue(ref, dai.getVal(), bType);
            }
            return null;
        }

        if (parts.length == 4) {
            // LD/LN.DO.SDI.BDA
            String sdiName = parts[2];
            String bdaName = parts[3];
            SclSDI sdi = doi.findSdiByName(sdiName);
            if (sdi == null) return null;
            SclDAI dai = sdi.findDaiByName(bdaName);
            if (dai != null && dai.getVal() != null && !dai.getVal().isEmpty()) {
                String bType = resolveSdiBdaBType(templates, ln, doName, sdiName, bdaName);
                return new SclDataValue(ref, dai.getVal(), bType);
            }
            return null;
        }

        return null;
    }

    public static String resolveDaBType(SclDataTypeTemplates templates, SclLN ln, String doName, String daName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return null;
        SclDA da = doType.findDaByName(daName);
        return da != null ? da.getBType() : null;
    }

    public static String resolveSdiBdaBType(SclDataTypeTemplates templates, SclLN ln,
                                              String doName, String sdiName, String bdaName) {
        if (templates == null || ln.getLnType() == null) return null;
        SclLNodeType lnt = templates.findLNodeTypeById(ln.getLnType());
        if (lnt == null) return null;
        SclDO doDef = lnt.findDoByName(doName);
        if (doDef == null || doDef.getType() == null) return null;
        SclDOType doType = templates.findDoTypeById(doDef.getType());
        if (doType == null) return null;
        for (SclDA da : doType.getDas()) {
            if (da.getName().equals(sdiName) && "Struct".equals(da.getBType()) && da.getType() != null) {
                SclDAType dat = templates.findDaTypeById(da.getType());
                if (dat != null) {
                    SclBDA bda = dat.findBdaByName(bdaName);
                    if (bda != null) return bda.getBType();
                }
            }
        }
        return null;
    }
}
