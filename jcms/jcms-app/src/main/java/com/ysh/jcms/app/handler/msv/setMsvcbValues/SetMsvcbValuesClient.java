package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.pdu.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SetMsvcbValuesClient extends BaseClientHandler<SetMsvcbValuesDao> {

    @Override
    public void execute(SetMsvcbValuesDao dao) throws Exception {
        send(ServiceName.SET_MSVCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSetMsvcbValuesError());
        throw new IOException("SetMSVCBValues rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSetMsvcbValuesResponse());
    }
}
