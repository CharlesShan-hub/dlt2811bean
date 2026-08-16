package com.ysh.jcms.app.handler.sg.selectEditSg;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.sg.CmsSelectEditSgError;
import com.ysh.jcms.core.pdu.sg.CmsSelectEditSgResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectEditSgClient extends BaseClientHandler<SelectEditSgDao> {

    @Override
    public void execute(SelectEditSgDao dao) throws Exception {
        send(CmsServiceInfo.SELECT_EDIT_SG, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectEditSgError err = CmsFrameDecoder.decodeErr(frame, new CmsSelectEditSgError());
        throw new IOException("SelectEditSG rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SelectEditSgDao dao) throws IOException {
        CmsFrameDecoder.decodeResp(frame, new CmsSelectEditSgResponse());
    }
}
