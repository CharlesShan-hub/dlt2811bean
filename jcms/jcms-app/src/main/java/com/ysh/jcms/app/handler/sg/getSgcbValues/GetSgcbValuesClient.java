package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.block.CmsSgcb;
import com.ysh.jcms.data.common.CmsObjectReference;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesRequest;
import com.ysh.jcms.svc.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.svc.sg.CmsSgcbValueChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetSgcbValuesClient extends BaseClientHandler {

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

    private List<SgcbResult> lastResults = new ArrayList<>();

    public List<SgcbResult> getLastResults() {
        return lastResults;
    }

    public void execute(GetSgcbValuesDao dao) throws Exception {
        CmsGetSgcbValuesRequest req = new CmsGetSgcbValuesRequest().reqId(nextReqId());

        for (String ref : dao.references()) {
            CmsObjectReference objRef = new CmsObjectReference(ref);
            req.sgcbReference.add(objRef);
        }

        send(ServiceName.GET_SGCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetSgcbValuesError err = decodeErr(frame, new CmsGetSgcbValuesError());
        throw new IOException("GetSGCBValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetSgcbValuesResponse resp = decodeResp(frame, new CmsGetSgcbValuesResponse());

        List<SgcbResult> results = new ArrayList<>();
        for (int i = 0; i < resp.sgscb.count; i++) {
            CmsSgcbValueChoice choice = resp.sgscb.items.get(i);
            if (choice.choice.value() == CmsSgcbValueChoice.VALUE) {
                CmsSgcb sgcb = choice.altValue;
                results.add(new SgcbResult(true, sgcb.numOfSG.value(), sgcb.actSG.value(), sgcb.editSG.value()));
            } else {
                results.add(new SgcbResult(false, 0, 0, 0));
            }
        }
        this.lastResults = results;
        log.info("GetSGCBValues succeeded: {} entries", results.size());
    }
}
