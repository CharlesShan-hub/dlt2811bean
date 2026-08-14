package com.ysh.jcms.app.handler.goose.getGoReference;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.sequence.goose.CmsGoRefFcEntry;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceError;
import com.ysh.jcms.core.pdu.goose.CmsGetGoReferenceResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetGoReferenceClient extends BaseClientHandler<GetGoReferenceDao> {

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

    @Override
    public void execute(GetGoReferenceDao dao) throws Exception {
        send(CmsServiceInfo.GET_GO_REFERENCE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGoReferenceError err = decodeErr(frame, new CmsGetGoReferenceError());
        throw new IOException("GetGoReference rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetGoReferenceDao dao) throws IOException {
        CmsGetGoReferenceResponse resp = decodeResp(frame, new CmsGetGoReferenceResponse());

        List<MemberDataEntry> members = new ArrayList<>();
        for (CmsGoRefFcEntry entry : resp.memberData) {
            String ref = entry.reference.value();
            int fc = entry.fc.value();
            members.add(new MemberDataEntry(ref, fc));
        }

        content().res(new GoRefResult(resp.gocbReference.value(), resp.confRev.value(), resp.datSet.value(), members));
    }
}
