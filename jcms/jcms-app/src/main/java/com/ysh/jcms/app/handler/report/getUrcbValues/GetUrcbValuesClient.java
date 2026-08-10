package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsUrcb;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetUrcbValuesClient extends BaseClientHandler<GetUrcbValuesDao> {

    public static final class UrcbEntry {
        public final String desc;
        public UrcbEntry(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(GetUrcbValuesDao dao) throws Exception {
        send(ServiceName.GET_URCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetUrcbValuesError err = decodeErr(frame, new CmsGetUrcbValuesError());
        throw new IOException("GetURCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetUrcbValuesDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetUrcbValuesResponse resp = decodeResp(frame, new CmsGetUrcbValuesResponse());

        List<UrcbEntry> entries = new ArrayList<>();
        for (CmsUrcbValueChoice choice : resp.urcb) {
            if (choice.choice() == CmsUrcbValueChoice.VALUE) {
                CmsUrcb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("rptID=").append(b.rptID.value());
                sb.append(" rptEna=").append(b.rptEna.value());
                sb.append(" datSet=").append(b.datSet.value());
                sb.append(" confRev=").append(b.confRev.value());
                sb.append(" bufTm=").append(b.bufTm.value());
                sb.append(" sqNum=").append(b.sqNum.value());
                sb.append(" intgPd=").append(b.intgPd.value());
                entries.add(new UrcbEntry(sb.toString()));
            } else {
                entries.add(new UrcbEntry("(error)"));
            }
        }
        ctx.setResult(entries);
        log.info("GetURCBValues succeeded: {} entries", entries.size());
    }
}
