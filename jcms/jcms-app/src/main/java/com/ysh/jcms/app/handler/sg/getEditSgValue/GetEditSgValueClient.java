package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetEditSgValueClient extends BaseClientHandler<GetEditSgValueDao> {

    @Override
    public void execute(GetEditSgValueDao dao) throws Exception {
        send(ServiceName.GET_EDIT_SG_VALUE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetEditSgValueError err = decodeErr(frame, new CmsGetEditSgValueError());
        throw new IOException("GetEditSGValue rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetEditSgValueDao dao) throws IOException {
        CmsGetEditSgValueResponse resp = decodeResp(frame, new CmsGetEditSgValueResponse());
        content().res(resp);
    }
}
