package com.ysh.dlt2811bean.scl.util;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.scl.model.data.SclDataValue;
import com.ysh.dlt2811bean.scl.model.template.SclDataTypeTemplates;
import com.ysh.dlt2811bean.scl.model.ied.SclLDevice;
import com.ysh.dlt2811bean.scl.model.ied.SclLN;
import com.ysh.dlt2811bean.scl.model.instance.SclDOI;
import com.ysh.dlt2811bean.scl.model.instance.SclDAI;
import com.ysh.dlt2811bean.scl.model.template.SclLNodeType;
import com.ysh.dlt2811bean.scl.model.template.SclDOType;
import com.ysh.dlt2811bean.scl.model.template.SclDO;
import com.ysh.dlt2811bean.scl.model.template.SclBDA;
import com.ysh.dlt2811bean.scl.model.instance.SclSDI;
import com.ysh.dlt2811bean.scl.model.template.SclDA;
import com.ysh.dlt2811bean.scl.model.template.SclDAType;
import com.ysh.dlt2811bean.scl.model.ied.SclServer;
import com.ysh.dlt2811bean.scl.model.control.SclSGCBState;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

import java.util.Map;


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
        return resolveDataValue(server, ref, templates, null);
    }

    public static SclDataValue resolveDataValue(SclServer server, String ref, SclDataTypeTemplates templates, CmsServerSession session) {
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

        // Dynamic SG data: if DO is SG1~SGn and not in SCL model, resolve from SGCB state
        if (doi == null && doName.startsWith("SG") && session != null) {
            String sgNumStr = doName.substring(2);
            try {
                int sgNum = Integer.parseInt(sgNumStr);
                Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(session);
                CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();
                if (!setting.isSgDefaultEnabled()) return null;
                String sgcbRef = lnName + "." + setting.getSgDefaultName();
                SclSGCBState state = sgcbStates.get(sgcbRef);
                if (state != null && sgNum >= 1 && sgNum <= state.getNumOfSG()) {
                    if (parts.length == 3) {
                        String daName = parts[2];
                        String val = state.getSgValue(sgNum, daName);
                        if (val != null) {
                            return new SclDataValue(ref, val, "INT32");
                        }
                    }
                    return null;
                }
            } catch (NumberFormatException e) {
                // not a valid SG number, fall through
            }
            return null;
        }

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
