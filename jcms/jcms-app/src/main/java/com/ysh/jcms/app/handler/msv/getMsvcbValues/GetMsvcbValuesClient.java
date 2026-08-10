package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class GetMsvcbValuesClient extends BaseClientHandler<GetMsvcbValuesDao> {

    @Override
    public void execute(GetMsvcbValuesDao dao) throws Exception {
        send(ServiceName.GET_MSVCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsGetMsvcbValuesError());
        throw new IOException("GetMSVCBValues rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsGetMsvcbValuesResponse());
    }
}
