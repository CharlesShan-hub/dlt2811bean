package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.choice.CmsRcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesError;
import com.ysh.jcms.pdu.report.CmsGetBrcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetBrcbValuesClient extends BaseClientHandler<GetBrcbValuesDao> {

    public static final class BrcbEntry {
        public final String desc;
        public BrcbEntry(String desc) {
            this.desc = desc;
        }
    }

    @Override
    public void execute(GetBrcbValuesDao dao) throws Exception {
        send(ServiceName.GET_BRCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetBrcbValuesError err = decodeErr(frame, new CmsGetBrcbValuesError());
        throw new IOException("GetBRCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetBrcbValuesDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetBrcbValuesResponse resp = decodeResp(frame, new CmsGetBrcbValuesResponse());

        List<BrcbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.brcb.size(); i++) {
            CmsRcbValueChoice choice = resp.brcb.get(i);
            if (choice.choice() == CmsRcbValueChoice.VALUE) {
                CmsBrcb b = choice.altValue;
                StringBuilder sb = new StringBuilder();
                sb.append("rptID=").append(b.rptID.value());
                sb.append(" rptEna=").append(b.rptEna.value());
                sb.append(" datSet=").append(b.datSet.value());
                sb.append(" confRev=").append(b.confRev.value());
                sb.append(" bufTm=").append(b.bufTm.value());
                sb.append(" sqNum=").append(b.sqNum.value());
                sb.append(" intgPd=").append(b.intgPd.value());
                entries.add(new BrcbEntry(sb.toString()));
            } else {
                entries.add(new BrcbEntry("(error)"));
            }
        }
        ctx.setResult(entries);
        log.info("GetBRCBValues succeeded: {} entries", entries.size());
    }
}
