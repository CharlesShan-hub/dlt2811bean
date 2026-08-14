package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.core.pdu.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class AllCbValuesClient extends BaseClientHandler<AllCbValuesDao> {

    @Override
    public void execute(AllCbValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_ALL_CB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllCbValuesError err = decodeErr(frame, new CmsGetAllCbValuesError());
        throw new IOException("GetAllCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllCbValuesDao dao) throws IOException {
        CmsGetAllCbValuesResponse resp = decodeResp(frame, new CmsGetAllCbValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "cbValue");
    }

    @Override
    protected void setPaginationCursor(AllCbValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
