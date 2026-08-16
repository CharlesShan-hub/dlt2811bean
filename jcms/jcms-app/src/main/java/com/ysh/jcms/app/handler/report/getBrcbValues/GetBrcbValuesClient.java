package com.ysh.jcms.app.handler.report.getBrcbValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.report.CmsGetBrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsGetBrcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetBrcbValuesClient extends BaseClientHandler<GetBrcbValuesDao> {

    @Override
    public void execute(GetBrcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_BRCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetBrcbValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetBrcbValuesError());
        throw new IOException("GetBRCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetBrcbValuesDao dao) throws IOException {
        CmsGetBrcbValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetBrcbValuesResponse());
        content().res(resp.brcb);
    }
}
