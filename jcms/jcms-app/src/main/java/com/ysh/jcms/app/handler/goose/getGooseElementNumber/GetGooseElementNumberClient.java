package com.ysh.jcms.app.handler.goose.getGooseElementNumber;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.pdu.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetGooseElementNumberClient extends BaseClientHandler<GetGooseElementNumberDao> {

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

    @Override
    public void execute(GetGooseElementNumberDao dao) throws Exception {
        send(ServiceName.GET_GOOSE_ELEMENT_NUMBER, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGooseElementNumberError err = decodeErr(frame, new CmsGetGooseElementNumberError());
        throw new IOException("GetGOOSEElementNumber rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetGooseElementNumberResponse resp = decodeResp(frame, new CmsGetGooseElementNumberResponse());

        List<Integer> offsets = new ArrayList<>();
        for (CmsInt16U off : resp.memberOffset) {
            offsets.add(off.value());
        }

        lastResult = new ElementNumberResult(resp.gocbReference.value(), resp.confRev.value(), resp.datSet.value(), offsets);
    }
}
