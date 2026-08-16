package com.ysh.jcms.app.handler.log.getLcbValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsLcb;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLcbValuesClient extends BaseClientHandler<GetLcbValuesDao> {

    public static final class LcbEntry {
        public final String desc;
        public LcbEntry(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(GetLcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_LCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLcbValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetLcbValuesError());
        throw new IOException("GetLCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetLcbValuesDao dao) throws IOException {
        CmsGetLcbValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetLcbValuesResponse());

        List<LcbEntry> entries = new ArrayList<>();
        for (CmsLcbValueChoice choice : resp.lcb) {
            if (choice.choice() == CmsLcbValueChoice.VALUE) {
                CmsLcb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("logEna=").append(b.logEna.value());
                sb.append(" datSet=").append(b.datSet.value());
                sb.append(" intgPd=").append(b.intgPd.value());
                sb.append(" logRef=").append(b.logRef.value());
                sb.append(" trgOps=dc:").append(b.trgOps.data_change()).append(",qc:").append(b.trgOps.quality_change()).append(",du:")
                        .append(b.trgOps.data_update()).append(",integrity:").append(b.trgOps.integrity()).append(",gi:")
                        .append(b.trgOps.general_interrogation());
                sb.append(" optFlds=").append(b.optFlds != null && b.isPresent("optFlds") ? b.optFlds.bit0() : "-");
                sb.append(" bufTm=").append(b.isPresent("bufTm") ? b.bufTm.value() : "-");
                entries.add(new LcbEntry(sb.toString()));
            } else {
                entries.add(new LcbEntry("error=" + choice.altError.value()));
            }
        }
        content().res(entries);
    }
}
