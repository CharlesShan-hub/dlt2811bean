package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgError;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectEditSgClient extends BaseClientHandler<SelectEditSgDao> {

    @Override
    public void execute(SelectEditSgDao dao) throws Exception {
        send(ServiceName.SELECT_EDIT_SG, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectEditSgError err = decodeErr(frame, new CmsSelectEditSgError());
        throw new IOException("SelectEditSG rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SelectEditSgDao dao) throws IOException {
        decodeResp(frame, new CmsSelectEditSgResponse());
    }
}
