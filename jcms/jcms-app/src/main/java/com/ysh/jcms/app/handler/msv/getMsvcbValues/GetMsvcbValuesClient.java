package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesRequest;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetMsvcbValuesClient extends BaseClientHandler<GetMsvcbValuesDao> {

    @Override
    public void execute(GetMsvcbValuesDao dao) throws Exception {
        CmsGetMsvcbValuesRequest req = new CmsGetMsvcbValuesRequest();
        for (String ref : dao.refs()) {
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
