package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.config.CmsConfig;
import com.ysh.dlt2811bean.config.CmsConfigLoader;
import com.ysh.dlt2811bean.datatypes.compound.CmsSGCB;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
import com.ysh.dlt2811bean.scl.model.control.SclSGCBState;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;

import java.util.Map;

/**
 * SGCB 构建器
 */
public class SgcbBuilder {

    /**
     * 构建 SGCB 控制块
     */
    public static CmsCBValue buildSgcb(String sgcbRef, CmsServerSession session) {
        CmsConfig.Setting setting = CmsConfigLoader.load().getSetting();

        CmsCBValue result = new CmsCBValue();
        CmsSGCB sgb = result.sgb;
        sgb.sgcbName.set(setting.getSgDefaultName());
        sgb.sgcbRef.set(sgcbRef);

        if (session != null) {
            Map<String, SclSGCBState> sgcbStates = SclSGCBState.getOrCreateSessionState(session);
            SclSGCBState state = sgcbStates.get(sgcbRef);
            if (state != null) {
                sgb.numOfSG.set(state.getNumOfSG());
                sgb.actSG.set(state.getActSG());
                sgb.editSG.set(state.getEditSG());
                sgb.cnfEdit.set(state.isCnfEdit());
                sgb.lActTm.secondsSinceEpoch.set(state.getActTm());
                sgb.resvTms.set(state.getResvTms());
                return result.selectSgb();
            }
        }

        sgb.numOfSG.set(setting.getNumOfSG());
        sgb.actSG.set(1);
        sgb.editSG.set(1);
        sgb.cnfEdit.set(true);
        sgb.lActTm.secondsSinceEpoch.set(System.currentTimeMillis() / 1000);
        sgb.resvTms.set(0);
        return result.selectSgb();
    }
}
