package com.ysh.jcms.app.handler.sg.confirmEditSgValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesError;
import com.ysh.jcms.core.pdu.sg.CmsConfirmEditSgValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class ConfirmEditSgValuesClient extends BaseClientHandler<ConfirmEditSgValuesDao> {

    @Override
    public void execute(ConfirmEditSgValuesDao dao) throws Exception {
        send(CmsServiceInfo.CONFIRM_EDIT_SG_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsConfirmEditSgValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsConfirmEditSgValuesError());
        throw new IOException("ConfirmEditSGValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, ConfirmEditSgValuesDao dao) throws IOException {
        CmsFrameDecoder.decodeResp(frame, new CmsConfirmEditSgValuesResponse());
    }
}
