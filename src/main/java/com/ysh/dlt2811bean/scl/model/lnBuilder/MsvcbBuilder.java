package com.ysh.dlt2811bean.scl.model.lnBuilder;

import com.ysh.dlt2811bean.datatypes.compound.CmsMSVCB;
import com.ysh.dlt2811bean.scl.model.control.SclSampledValueControl;
import com.ysh.dlt2811bean.service.svc.directory.datatypes.CmsCBValue;
public class MsvcbBuilder {

    /**
     * 构建 MSV_CB 控制块
     */
    public static CmsCBValue buildMsvcb(SclSampledValueControl sv) {
        CmsMSVCB msvcb = new CmsMSVCB();
        msvcb.msvCBName.set(sv.getName());
        if (sv.getDatSet() != null) {
            msvcb.datSet.set(sv.getDatSet());
        }
        if (sv.getSvID() != null) {
            msvcb.msvID.set(sv.getSvID());
        }
        if (sv.getConfRev() != null) {
            msvcb.confRev.set(Long.parseLong(sv.getConfRev()));
        }
        if (sv.getSmpRate() != null && !sv.getSmpRate().isEmpty()) {
            msvcb.smpRate.set(Integer.parseInt(sv.getSmpRate()));
        }
        return new CmsCBValue().selectMsvcb();
    }
}
