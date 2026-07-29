package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.common.CmsObjectReference;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesRequest;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;
import java.util.List;

public class GetMsvcbValuesClient extends BaseClientHandler {

    public void execute(List<String> refs) throws Exception {
        CmsGetMsvcbValuesRequest req = new CmsGetMsvcbValuesRequest().reqId(nextReqId());
        for (String ref : refs) {
            CmsObjectReference objRef = new CmsObjectReference();
            objRef.value(ref);
            req.reference.add(objRef);
        }
        send(ServiceName.GET_MSVCB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetMsvcbValuesError());
        throw new IOException("GetMSVCBValues rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetMsvcbValuesResponse());
        log.info("GetMSVCBValues succeeded");
    }
}
