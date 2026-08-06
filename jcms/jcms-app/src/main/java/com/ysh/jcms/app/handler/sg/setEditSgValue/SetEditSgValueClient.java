package com.ysh.jcms.app.handler.sg.setEditSgValue;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueError;
import com.ysh.jcms.pdu.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetEditSgValueClient extends BaseClientHandler<SetEditSgValueDao> {

    @Override
    public void execute(SetEditSgValueDao dao) throws Exception {
        send(ServiceName.SET_EDIT_SG_VALUE, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetEditSgValueError err = decodeErr(frame, new CmsSetEditSgValueError());
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (CmsServiceError e : err.result) {
            if (sb.length() > 0)
                sb.append(", ");
            sb.append("entry[").append(i).append("]=").append(e.value());
            i++;
        }
        throw new IOException("SetEditSGValue rejected: " + sb);
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsSetEditSgValueResponse resp = decodeResp(frame, new CmsSetEditSgValueResponse());
        log.info("SetEditSGValue succeeded");
    }
}
