package com.ysh.jcms.app.handler.sg.confirmEditSgValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.sg.CmsConfirmEditSgValuesError;
import com.ysh.jcms.pdu.sg.CmsConfirmEditSgValuesRequest;
import com.ysh.jcms.pdu.sg.CmsConfirmEditSgValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class ConfirmEditSgValuesClient extends BaseClientHandler<ConfirmEditSgValuesDao> {

    @Override
    public void execute(ConfirmEditSgValuesDao dao) throws Exception {
        CmsConfirmEditSgValuesRequest req = new CmsConfirmEditSgValuesRequest().sgcbReference(dao.sgcbReference());

        send(ServiceName.CONFIRM_EDIT_SG_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsConfirmEditSgValuesError err = decodeErr(frame, new CmsConfirmEditSgValuesError());
        throw new IOException("ConfirmEditSGValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsConfirmEditSgValuesResponse resp = decodeResp(frame, new CmsConfirmEditSgValuesResponse());
        log.info("ConfirmEditSGValues succeeded");
    }
}
