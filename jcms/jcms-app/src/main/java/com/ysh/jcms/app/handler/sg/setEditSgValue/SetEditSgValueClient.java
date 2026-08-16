package com.ysh.jcms.app.handler.sg.setEditSgValue;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.sg.CmsSetEditSgValueError;
import com.ysh.jcms.core.pdu.sg.CmsSetEditSgValueResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SetEditSgValueClient extends BaseClientHandler<SetEditSgValueDao> {

    @Override
    public void execute(SetEditSgValueDao dao) throws Exception {
        send(CmsServiceInfo.SET_EDIT_SG_VALUE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetEditSgValueError err = CmsFrameDecoder.decodeErr(frame, new CmsSetEditSgValueError());
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
    protected void onSuccess(Frame frame, SetEditSgValueDao dao) throws IOException {
        CmsFrameDecoder.decodeResp(frame, new CmsSetEditSgValueResponse());
    }
}
