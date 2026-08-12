package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.data.choice.CmsMsvcbValueChoice;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.core.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetMsvcbValuesClient extends BaseClientHandler<GetMsvcbValuesDao> {

    public static final class MsvcbEntry {
        public final String desc;
        public MsvcbEntry(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(GetMsvcbValuesDao dao) throws Exception {
        send(ServiceName.GET_MSVCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetMsvcbValuesError err = decodeErr(frame, new CmsGetMsvcbValuesError());
        throw new IOException("GetMSVCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetMsvcbValuesDao dao) throws IOException {
        CmsGetMsvcbValuesResponse resp = decodeResp(frame, new CmsGetMsvcbValuesResponse());

        List<MsvcbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.msvcb.size(); i++) {
            CmsMsvcbValueChoice ch = resp.msvcb.get(i);
            if (ch.choice() == CmsMsvcbValueChoice.VALUE) {
                CmsMsvcb val = ch.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("svEna=").append(val.svEna.value());
                sb.append(" msvID=").append(val.msvID.value());
                sb.append(" datSet=").append(val.datSet.value());
                sb.append(" confRev=").append(val.confRev.value());
                if (val.isPresent("smpMod")) {
                    sb.append(" smpMod=").append(val.smpMod.value());
                }
                sb.append(" smpRate=").append(val.smpRate.value());
                sb.append(" optFlds=").append(val.optFlds.value());
                if (val.isPresent("dstAddress")) {
                    sb.append(" dstAddress=").append(val.dstAddress);
                }
                entries.add(new MsvcbEntry(sb.toString()));
            } else {
                entries.add(new MsvcbEntry("error=" + ch.altError.value()));
            }
        }
        content().res(entries);
        log.info("GetMSVCBValues returned {} entries, moreFollows={}", resp.msvcb.size(), resp.moreFollows.value());
    }
}
