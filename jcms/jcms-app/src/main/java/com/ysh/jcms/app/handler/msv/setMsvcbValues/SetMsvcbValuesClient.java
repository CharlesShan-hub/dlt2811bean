package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.Collections;

public class SetMsvcbValuesClient extends BaseClientHandler<SetMsvcbValuesDao> {

    @Override
    public void execute(SetMsvcbValuesDao dao) throws Exception {
        send(ServiceName.SET_MSVCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetMsvcbValuesError err = decodeErr(frame, new CmsSetMsvcbValuesError());
        throw new IOException("SetMSVCBValues rejected: " + err);
    }

    @Override
    protected void onSuccess(Frame frame, SetMsvcbValuesDao dao) throws IOException {
        decodeResp(frame, new CmsSetMsvcbValuesResponse());
        content().res(Collections.singletonList("MSVCB values set successfully"));
    }
}
