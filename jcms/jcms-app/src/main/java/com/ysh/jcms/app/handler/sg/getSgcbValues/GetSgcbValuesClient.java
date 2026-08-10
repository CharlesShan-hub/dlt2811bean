package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.choice.CmsSgcbValueChoice;
import com.ysh.jcms.data.sequence.block.CmsSgcb;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.pdu.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetSgcbValuesClient extends BaseClientHandler<GetSgcbValuesDao> {

    public static final class SgcbResult {
        public final boolean success;
        public final int numOfSG;
        public final int actSG;
        public final int editSG;

        public SgcbResult(boolean success, int numOfSG, int actSG, int editSG) {
            this.success = success;
            this.numOfSG = numOfSG;
            this.actSG = actSG;
            this.editSG = editSG;
        }
    }

    @Override
    public void execute(GetSgcbValuesDao dao) throws Exception {
        send(ServiceName.GET_SGCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetSgcbValuesError err = decodeErr(frame, new CmsGetSgcbValuesError());
        throw new IOException("GetSGCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetSgcbValuesDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetSgcbValuesResponse resp = decodeResp(frame, new CmsGetSgcbValuesResponse());

        List<SgcbResult> results = new ArrayList<>();
        for (int i = 0; i < resp.sgscb.size(); i++) {
            CmsSgcbValueChoice choice = resp.sgscb.get(i);
            if (choice.choice() == CmsSgcbValueChoice.VALUE) {
                CmsSgcb sgcb = choice.altValue;
                results.add(new SgcbResult(true, sgcb.numOfSG.value(), sgcb.actSG.value(), sgcb.editSG.value()));
            } else {
                results.add(new SgcbResult(false, 0, 0, 0));
            }
        }
        ctx.setResult(results);
    }
}
