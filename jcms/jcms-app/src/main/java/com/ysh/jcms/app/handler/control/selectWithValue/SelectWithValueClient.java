package com.ysh.jcms.app.handler.control.selectWithValue;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.control.CmsSelectWithValueError;
import com.ysh.jcms.core.pdu.control.CmsSelectWithValueResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import java.io.IOException;

public class SelectWithValueClient extends BaseClientHandler<SelectWithValueDao> {

    @Override
    public void execute(SelectWithValueDao dao) throws Exception {
        send(CmsServiceInfo.SELECT_WITH_VALUE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        decodeErr(frame, new CmsSelectWithValueError());
        throw new IOException("SelectWithValue rejected");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        decodeResp(frame, new CmsSelectWithValueResponse());
    }
}
