package com.ysh.jcms.app.handler.log.getLogStatusValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsLogStatusValueChoice;
import com.ysh.jcms.data.sequence.log.CmsLogStatusValue;
import com.ysh.jcms.pdu.log.CmsGetLogStatusValuesError;
import com.ysh.jcms.pdu.log.CmsGetLogStatusValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetLogStatusValuesClient extends BaseClientHandler<GetLogStatusValuesDao> {

    public static final class LogStatusEntry {
        public final String desc;
        public LogStatusEntry(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(GetLogStatusValuesDao dao) throws Exception {
        send(ServiceName.GET_LOG_STATUS_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogStatusValuesError err = decodeErr(frame, new CmsGetLogStatusValuesError());
        throw new IOException("GetLogStatusValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetLogStatusValuesDao dao) throws IOException {
        CmsGetLogStatusValuesResponse resp = decodeResp(frame, new CmsGetLogStatusValuesResponse());

        List<LogStatusEntry> entries = new ArrayList<>();
        for (CmsLogStatusValueChoice ch : resp.log) {
            if (ch.choice() == CmsLogStatusValueChoice.VALUE) {
                CmsLogStatusValue val = ch.altValue;
                entries.add(new LogStatusEntry("oldEntrTm=" + val.oldEntrTm.msOfDay.value() + "/" + val.oldEntrTm.daysSince1984.value()
                        + " newEntrTm=" + val.newEntrTm.msOfDay.value() + "/" + val.newEntrTm.daysSince1984.value()
                        + " oldEntr=" + new String(val.oldEntr.value(), StandardCharsets.UTF_8)
                        + " newEntr=" + new String(val.newEntr.value(), StandardCharsets.UTF_8)));
            } else {
                entries.add(new LogStatusEntry("error=" + ch.altError.value()));
            }
        }
        content().res(entries);
    }
}