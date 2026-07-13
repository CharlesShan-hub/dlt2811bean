package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.log.CmsGetLcbValuesError;
import com.ysh.jcms.svc.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.svc.log.CmsLcbValueChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLcbValuesClient extends BaseClientHandler {

    public static final class LcbEntry {
        public final String desc;
        public LcbEntry(String desc) {
            this.desc = desc;
        }
    }

    private List<LcbEntry> lastEntries = new ArrayList<>();
    public List<LcbEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetLcbValuesDao dao) throws Exception {
        CmsGetLcbValuesRequest req = new CmsGetLcbValuesRequest().reqId(nextReqId());
        for (String ref : dao.refs()) {
            req.reference.add(new CmsObjectReference(ref));
        }
        send(ServiceName.GET_LCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLcbValuesError err = decodeErr(frame, new CmsGetLcbValuesError());
        throw new IOException("GetLCBValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLcbValuesResponse resp = decodeResp(frame, new CmsGetLcbValuesResponse());

        List<LcbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.lcb.count; i++) {
            CmsLcbValueChoice choice = resp.lcb.items.get(i);
            if (choice.choice.value() == CmsLcbValueChoice.VALUE) {
                CmsLcb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("logEna=").append(b.logEna.value());
                sb.append(" datSet=").append(new String(b.datSet.value(), java.nio.charset.StandardCharsets.UTF_8));
                sb.append(" intgPd=").append(b.intgPd.value());
                sb.append(" logRef=").append(new String(b.logRef.value(), java.nio.charset.StandardCharsets.UTF_8));
                sb.append(" trgOps=dc:").append(b.trgOps.data_change.value()).append(",qc:").append(b.trgOps.quality_change.value())
                        .append(",du:").append(b.trgOps.data_update.value()).append(",integrity:").append(b.trgOps.integrity.value())
                        .append(",gi:").append(b.trgOps.general_interrogation.value());
                entries.add(new LcbEntry(sb.toString()));
            } else {
                entries.add(new LcbEntry("error=" + choice.altError.value()));
            }
        }
        lastEntries = entries;
    }
}
