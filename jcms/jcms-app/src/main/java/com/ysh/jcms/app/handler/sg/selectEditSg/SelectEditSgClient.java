package com.ysh.jcms.app.handler.sg.selectEditSg;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgError;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgRequest;
import com.ysh.jcms.pdu.sg.CmsSelectEditSgResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectEditSgClient extends BaseClientHandler {

    public void execute(SelectEditSgDao dao) throws Exception {
        CmsSelectEditSgRequest req = new CmsSelectEditSgRequest().sgcbReference(dao.sgcbReference())
                .settingGroupNumber(dao.settingGroupNumber());

        send(ServiceName.SELECT_EDIT_SG, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectEditSgError err = decodeErr(frame, new CmsSelectEditSgError());
        throw new IOException("SelectEditSG rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSelectEditSgResponse resp = decodeResp(frame, new CmsSelectEditSgResponse());
        log.info("SelectEditSG succeeded");
    }
}
