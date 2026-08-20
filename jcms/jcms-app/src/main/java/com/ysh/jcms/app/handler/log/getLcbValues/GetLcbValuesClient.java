package com.ysh.jcms.app.handler.log.getLcbValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.core.pdu.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetLcbValuesClient extends BaseClientHandler<GetLcbValuesDao> {

    @Override
    public void execute(GetLcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_LCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLcbValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetLcbValuesError());
        throw new IOException("GetLCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetLcbValuesDao dao) throws IOException {
        CmsGetLcbValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetLcbValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "lcb");
    }
}