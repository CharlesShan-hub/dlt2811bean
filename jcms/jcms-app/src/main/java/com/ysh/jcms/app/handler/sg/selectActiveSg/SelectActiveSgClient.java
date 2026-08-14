package com.ysh.jcms.app.handler.sg.selectActiveSg;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgError;
import com.ysh.jcms.core.pdu.sg.CmsSelectActiveSgResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SelectActiveSgClient extends BaseClientHandler<SelectActiveSgDao> {

    @Override
    public void execute(SelectActiveSgDao dao) throws Exception {
        send(CmsServiceInfo.SELECT_ACTIVE_SG, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSelectActiveSgError err = decodeErr(frame, new CmsSelectActiveSgError());
        throw new IOException("SelectActiveSG rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SelectActiveSgDao dao) throws IOException {
        decodeResp(frame, new CmsSelectActiveSgResponse());
    }
}
