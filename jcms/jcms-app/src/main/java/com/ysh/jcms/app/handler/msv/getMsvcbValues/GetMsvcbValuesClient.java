package com.ysh.jcms.app.handler.msv.getMsvcbValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.core.pdu.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetMsvcbValuesClient extends BaseClientHandler<GetMsvcbValuesDao> {

    @Override
    public void execute(GetMsvcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_MSVCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetMsvcbValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetMsvcbValuesError());
        throw new IOException("GetMSVCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetMsvcbValuesDao dao) throws IOException {
        CmsGetMsvcbValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetMsvcbValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "msvcb");
    }
}