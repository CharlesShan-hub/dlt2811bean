package com.ysh.jcms.utils.scl.model.lnBuilder;

import com.ysh.jcms.data.block.CmsMsvcb;
import com.ysh.jcms.svc.directory.CmsCbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;

/**
 * MSVCB 构建器
 */
public class MsvcbBuilder {

    private final CmsMsvcb msvcb = new CmsMsvcb();

    public MsvcbBuilder msvId(String msvId) {
        if (msvId != null) msvcb.msvID(msvId);
        return this;
    }

    public MsvcbBuilder datSet(String datSet) {
        if (datSet != null) msvcb.datSet(datSet);
        return this;
    }

    public MsvcbBuilder confRev(long confRev) {
        msvcb.confRev(confRev);
        return this;
    }

    public MsvcbBuilder smpRate(int smpRate) {
        msvcb.smpRate(smpRate);
        return this;
    }

    public CmsCbValueChoice build() {
        CmsCbValueChoice result = new CmsCbValueChoice();
        result.choice(CmsCbValueChoice.MSVCB);
        result.altMsvcb = msvcb;
        return result;
    }

    public static CmsCbValueChoice from(SclSampledValueControl sv) {
        MsvcbBuilder builder = new MsvcbBuilder()
                .msvId(sv.getSvID())
                .datSet(sv.getDatSet())
                .confRev(sv.getConfRev() != null ? Long.parseLong(sv.getConfRev()) : 0);
        if (sv.getSmpRate() != null && !sv.getSmpRate().isEmpty()) {
            builder.smpRate(Integer.parseInt(sv.getSmpRate()));
        }
        return builder.build();
    }
}
