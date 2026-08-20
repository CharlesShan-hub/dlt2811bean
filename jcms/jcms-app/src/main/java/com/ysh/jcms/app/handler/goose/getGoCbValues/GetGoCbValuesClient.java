package com.ysh.jcms.app.handler.goose.getGoCbValues;
import com.ysh.jcms.app.handler.support.CmsFrameDecoder;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.goose.CmsGetGoCbValuesError;
import com.ysh.jcms.core.pdu.goose.CmsGetGoCbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetGoCbValuesClient extends BaseClientHandler<GetGoCbValuesDao> {

    @Override
    public void execute(GetGoCbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_GOCB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetGoCbValuesError err = CmsFrameDecoder.decodeErr(frame, new CmsGetGoCbValuesError());
        throw new IOException("GetGoCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetGoCbValuesDao dao) throws IOException {
        CmsGetGoCbValuesResponse resp = CmsFrameDecoder.decodeResp(frame, new CmsGetGoCbValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "gocb");
    }
}