package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesError;
import com.ysh.jcms.core.pdu.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetUrcbValuesClient extends BaseClientHandler<GetUrcbValuesDao> {

    @Override
    public void execute(GetUrcbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_URCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetUrcbValuesError err = decodeErr(frame, new CmsGetUrcbValuesError());
        throw new IOException("GetURCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetUrcbValuesDao dao) throws IOException {
        CmsGetUrcbValuesResponse resp = decodeResp(frame, new CmsGetUrcbValuesResponse());
        content().res(resp.urcb);
    }
}
