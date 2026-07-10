package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesError;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesRequest;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesResponse;
import com.ysh.jcms.svc.goose.CmsGocbValueChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetGoCbValuesClient extends BaseClientHandler {

    public static final class GoCbEntry {
        public final String desc;
        public GoCbEntry(String desc) {
            this.desc = desc;
        }
    }

    private List<GoCbEntry> lastEntries = new ArrayList<>();
    public List<GoCbEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetGoCbValuesDao dao) throws Exception {
        CmsGetGoCbValuesRequest req = new CmsGetGoCbValuesRequest().reqId(nextReqId());
        for (String ref : dao.refs()) {
            req.reference.add(new CmsObjectReference(ref));
        }
        send(ServiceName.GET_GOCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGoCbValuesError err = decodeErr(frame, new CmsGetGoCbValuesError());
        throw new IOException("GetGoCBValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetGoCbValuesResponse resp = decodeResp(frame, new CmsGetGoCbValuesResponse());

        List<GoCbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.gocb.count; i++) {
            CmsGocbValueChoice choice = resp.gocb.items.get(i);
            if (choice.choice.value() == CmsGocbValueChoice.VALUE) {
                CmsGoCb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("goEna=").append(b.goEna.value());
                sb.append(" goID=").append(new String(b.goID.value(), StandardCharsets.UTF_8));
                sb.append(" datSet=").append(new String(b.datSet.value(), StandardCharsets.UTF_8));
                sb.append(" confRev=").append(b.confRev.value());
                sb.append(" ndsCom=").append(b.ndsCom.value());
                entries.add(new GoCbEntry(sb.toString()));
            } else {
                entries.add(new GoCbEntry("error=" + choice.altError.value()));
            }
        }
        lastEntries = entries;
    }
}
