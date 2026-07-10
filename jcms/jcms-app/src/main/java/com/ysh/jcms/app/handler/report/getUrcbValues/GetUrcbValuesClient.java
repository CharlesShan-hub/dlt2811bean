package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsRcbValueChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUrcbValuesClient extends BaseClientHandler {

    public static final class UrcbEntry {
        public final String desc;
        public UrcbEntry(String desc) {
            this.desc = desc;
        }
    }

    private List<UrcbEntry> lastEntries = new ArrayList<>();
    public List<UrcbEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetUrcbValuesDao dao) throws Exception {
        CmsGetUrcbValuesRequest req = new CmsGetUrcbValuesRequest().reqId(nextReqId());
        for (String ref : dao.refs()) {
            req.reference.add(new CmsObjectReference(ref));
        }
        send(ServiceName.GET_URCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetUrcbValuesError err = decodeErr(frame, new CmsGetUrcbValuesError());
        throw new IOException("GetURCBValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetUrcbValuesResponse resp = decodeResp(frame, new CmsGetUrcbValuesResponse());

        List<UrcbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.urcb.count; i++) {
            CmsRcbValueChoice choice = resp.urcb.items.get(i);
            if (choice.choice.value() == CmsRcbValueChoice.VALUE) {
                CmsBrcb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("rptID=").append(new String(b.rptID.value(), java.nio.charset.StandardCharsets.UTF_8));
                sb.append(" rptEna=").append(b.rptEna.value());
                sb.append(" datSet=").append(new String(b.datSet.value(), java.nio.charset.StandardCharsets.UTF_8));
                sb.append(" confRev=").append(b.confRev.value());
                sb.append(" bufTm=").append(b.bufTm.value());
                sb.append(" sqNum=").append(b.sqNum.value());
                sb.append(" intgPd=").append(b.intgPd.value());
                entries.add(new UrcbEntry(sb.toString()));
            } else {
                entries.add(new UrcbEntry("(error)"));
            }
        }
        this.lastEntries = entries;
        log.info("GetURCBValues succeeded: {} entries", entries.size());
    }
}
