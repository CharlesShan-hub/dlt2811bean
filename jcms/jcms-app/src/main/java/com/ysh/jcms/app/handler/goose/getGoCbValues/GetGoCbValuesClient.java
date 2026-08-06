package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.choice.CmsGocbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsGoCb;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesError;
import com.ysh.jcms.pdu.goose.CmsGetGoCbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetGoCbValuesClient extends BaseClientHandler<GetGoCbValuesDao> {

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

    @Override
    public void execute(GetGoCbValuesDao dao) throws Exception {
        send(ServiceName.GET_GOCB_VALUES, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGoCbValuesError err = decodeErr(frame, new CmsGetGoCbValuesError());
        throw new IOException("GetGoCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetGoCbValuesResponse resp = decodeResp(frame, new CmsGetGoCbValuesResponse());

        List<GoCbEntry> entries = new ArrayList<>();
        for (CmsGocbValueChoice choice : resp.gocb) {
            if (choice.choice() == CmsGocbValueChoice.VALUE) {
                CmsGoCb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("goEna=").append(b.goEna.value());
                sb.append(" goID=").append(b.goID.value());
                sb.append(" datSet=").append(b.datSet.value());
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
