package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.sequence.block.CmsLcb;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesResponse;
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
        CmsGetLcbValuesRequest req = new CmsGetLcbValuesRequest();
        for (String ref : dao.refs()) {
            req.reference.add(new CmsObjectReference(ref));
        }
        send(ServiceName.GET_LCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLcbValuesError err = decodeErr(frame, new CmsGetLcbValuesError());
        throw new IOException("GetLCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLcbValuesResponse resp = decodeResp(frame, new CmsGetLcbValuesResponse());

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
                entries.add(new LcbEntry(sb.toString()));
            } else {
                entries.add(new LcbEntry("error=" + choice.altError.value()));
            }
        }
        lastEntries = entries;
    }
}
