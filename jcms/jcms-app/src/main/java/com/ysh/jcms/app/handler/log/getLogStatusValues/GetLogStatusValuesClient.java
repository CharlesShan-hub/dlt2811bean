package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesError;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLogStatusValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLogStatusValuesClient extends BaseClientHandler {

    public static final class LogStatusEntry {
        public final String desc;
        public LogStatusEntry(String desc) {
            this.desc = desc;
        }
    }

    private List<LogStatusEntry> lastEntries = new ArrayList<>();
    public List<LogStatusEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetLogStatusValuesDao dao) throws Exception {
        CmsGetLogStatusValuesRequest req = new CmsGetLogStatusValuesRequest().reqId(nextReqId());
        for (String ref : dao.refs()) {
            req.logReference.add(new CmsObjectReference(ref));
        }
        send(ServiceName.GET_LOG_STATUS_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogStatusValuesError err = decodeErr(frame, new CmsGetLogStatusValuesError());
        throw new IOException("GetLogStatusValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogStatusValuesResponse resp = decodeResp(frame, new CmsGetLogStatusValuesResponse());

        List<LogStatusEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.log.count; i++) {
            com.ysh.jcms.svc.log.CmsLogStatusValueChoice ch = resp.log.items.get(i);
            if (ch.choice.value() == 1) {
                com.ysh.jcms.svc.log.CmsLogStatusValue val = ch.altValue;
                entries.add(new LogStatusEntry("oldEntrTm=" + val.oldEntrTm.msOfDay.value() + "/" + val.oldEntrTm.daysSince1984.value()
                        + " newEntrTm=" + val.newEntrTm.msOfDay.value() + "/" + val.newEntrTm.daysSince1984.value()));
            } else {
                entries.add(new LogStatusEntry("error=" + ch.altError.value()));
            }
        }
        lastEntries = entries;
    }
}
