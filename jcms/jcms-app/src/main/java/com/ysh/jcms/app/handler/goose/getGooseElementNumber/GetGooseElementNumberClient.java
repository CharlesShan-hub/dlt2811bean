package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.svc.goose.CmsGetGooseElementNumberRequest;
import com.ysh.jcms.svc.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.svc.goose.CmsGoRefFcEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetGooseElementNumberClient extends BaseClientHandler {

    public static final class MemberSpec {
        public final String reference;
        public final int fc;
        public MemberSpec(String reference, int fc) {
            this.reference = reference;
            this.fc = fc;
        }
    }

    public static final class ElementNumberResult {
        public final String gocbReference;
        public final long confRev;
        public final String datSet;
        public final List<Integer> memberOffsets;
        public ElementNumberResult(String gocbReference, long confRev, String datSet, List<Integer> memberOffsets) {
            this.gocbReference = gocbReference;
            this.confRev = confRev;
            this.datSet = datSet;
            this.memberOffsets = memberOffsets;
        }
    }

    private ElementNumberResult lastResult;
    public ElementNumberResult getLastResult() {
        return lastResult;
    }

    public void execute(GetGooseElementNumberDao dao) throws Exception {
        CmsGetGooseElementNumberRequest req = new CmsGetGooseElementNumberRequest().reqId(nextReqId()).gocbReference(dao.gocbReference());
        for (MemberSpec spec : dao.members()) {
            CmsGoRefFcEntry entry = new CmsGoRefFcEntry().reference(spec.reference).fc(spec.fc);
            req.memberData.add(entry);
        }
        send(ServiceName.GET_GOOSE_ELEMENT_NUMBER, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGooseElementNumberError err = decodeErr(frame, new CmsGetGooseElementNumberError());
        throw new IOException("GetGOOSEElementNumber rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetGooseElementNumberResponse resp = decodeResp(frame, new CmsGetGooseElementNumberResponse());

        List<Integer> offsets = new ArrayList<>();
        for (int i = 0; i < resp.memberOffset.count; i++) {
            offsets.add(resp.memberOffset.items.get(i).value());
        }

        lastResult = new ElementNumberResult(new String(resp.gocbReference.value(), StandardCharsets.UTF_8), resp.confRev.value(),
                new String(resp.datSet.value(), StandardCharsets.UTF_8), offsets);
    }
}
