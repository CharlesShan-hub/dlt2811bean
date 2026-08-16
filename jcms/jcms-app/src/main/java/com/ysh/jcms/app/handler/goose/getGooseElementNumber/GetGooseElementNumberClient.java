package com.ysh.jcms.app.handler.goose.getGooseElementNumber;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberError;
import com.ysh.jcms.core.pdu.goose.CmsGetGooseElementNumberResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
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

    @Override
    public void execute(GetGooseElementNumberDao dao) throws Exception {
        send(CmsServiceInfo.GET_GOOSE_ELEMENT_NUMBER, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGooseElementNumberError err = CmsFrameDecoder.decodeErr(frame, new CmsGetGooseElementNumberError());
        throw new IOException("GetGOOSEElementNumber rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetGooseElementNumberDao dao) throws IOException {
        CmsGetGooseElementNumberResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetGooseElementNumberResponse());

        List<Integer> offsets = new ArrayList<>();
        for (CmsInt16U off : resp.memberOffset) {
            offsets.add(off.value());
        }

        content().res(new ElementNumberResult(resp.gocbReference.value(), resp.confRev.value(), resp.datSet.value(), offsets));
    }
}
