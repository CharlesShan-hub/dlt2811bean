package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class AllDataValuesClient extends BaseClientHandler<AllDataValuesDao> {

    @Override
    public void execute(AllDataValuesDao dao) throws Exception {
        send(ServiceName.GET_ALL_DATA_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllDataValuesDao dao) throws IOException {
        CmsGetAllDataValuesResponse resp = decodeResp(frame, new CmsGetAllDataValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "data");
    }

    @Override
    protected void setPaginationCursor(AllDataValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
