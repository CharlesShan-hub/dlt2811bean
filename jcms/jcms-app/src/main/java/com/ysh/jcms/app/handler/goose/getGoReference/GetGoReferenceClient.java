package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.pdu.goose.CmsGetGoReferenceError;
import com.ysh.jcms.pdu.goose.CmsGetGoReferenceRequest;
import com.ysh.jcms.pdu.goose.CmsGetGoReferenceResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetGoReferenceClient extends BaseClientHandler {

    public static final class MemberDataEntry {
        public final String reference;
        public final int fc;
        public MemberDataEntry(String reference, int fc) {
            this.reference = reference;
            this.fc = fc;
        }
    }

    public static final class GoRefResult {
        public final String gocbReference;
        public final long confRev;
        public final String datSet;
        public final List<MemberDataEntry> members;
        public GoRefResult(String gocbReference, long confRev, String datSet, List<MemberDataEntry> members) {
            this.gocbReference = gocbReference;
            this.confRev = confRev;
            this.datSet = datSet;
            this.members = members;
        }
    }

    private GoRefResult lastResult;
    public GoRefResult getLastResult() {
        return lastResult;
    }

    public void execute(GetGoReferenceDao dao) throws Exception {
        CmsGetGoReferenceRequest req = new CmsGetGoReferenceRequest().reqId(nextReqId()).gocbReference(dao.gocbReference());
        for (int offset : dao.memberOffsets()) {
            req.memberOfs.add(new CmsInt16U(offset));
        }
        send(ServiceName.GET_GO_REFERENCE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGoReferenceError err = decodeErr(frame, new CmsGetGoReferenceError());
        throw new IOException("GetGoReference rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetGoReferenceResponse resp = decodeResp(frame, new CmsGetGoReferenceResponse());

        List<MemberDataEntry> members = new ArrayList<>();
        for (int i = 0; i < resp.memberData.count; i++) {
            String ref = new String(resp.memberData.items.get(i).reference.value(), java.nio.charset.StandardCharsets.UTF_8);
            int fc = resp.memberData.items.get(i).fc.value();
            members.add(new MemberDataEntry(ref, fc));
        }

        lastResult = new GoRefResult(new String(resp.gocbReference.value(), java.nio.charset.StandardCharsets.UTF_8), resp.confRev.value(),
                new String(resp.datSet.value(), java.nio.charset.StandardCharsets.UTF_8), members);
    }
}
