package com.ysh.jcms.app.handler.sg.getSgcbValues;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesError;
import com.ysh.jcms.core.pdu.sg.CmsGetSgcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetSgcbValuesClient extends BaseClientHandler<GetSgcbValuesDao> {

    @Override
    public void execute(GetSgcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_SGCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetSgcbValuesError err = decodeErr(frame, new CmsGetSgcbValuesError());
        throw new IOException("GetSGCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetSgcbValuesDao dao) throws IOException {
        CmsGetSgcbValuesResponse resp = decodeResp(frame, new CmsGetSgcbValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "sgscb");
    }
}
