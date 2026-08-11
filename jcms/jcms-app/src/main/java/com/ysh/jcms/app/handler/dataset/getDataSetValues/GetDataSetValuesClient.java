package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetDataSetValuesClient extends BaseClientHandler<GetDataSetValuesDao> {

    @Override
    public void execute(GetDataSetValuesDao dao) throws Exception {
        send(ServiceName.GET_DATA_SET_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetValuesError err = decodeErr(frame, new CmsGetDataSetValuesError());
        throw new IOException("GetDataSetValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataSetValuesDao dao) throws IOException {
        CmsGetDataSetValuesResponse resp = decodeResp(frame, new CmsGetDataSetValuesResponse());
        CmsClientOperator.accumulatePage(content(), resp, "value");
    }

    @Override
    protected void setPaginationCursor(GetDataSetValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
