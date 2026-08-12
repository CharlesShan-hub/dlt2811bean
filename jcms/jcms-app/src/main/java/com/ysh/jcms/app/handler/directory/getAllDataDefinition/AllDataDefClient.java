package com.ysh.jcms.app.handler.directory.getAllDataDefinition;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataDefinitionError;
import com.ysh.jcms.core.pdu.directory.CmsGetAllDataDefinitionResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class AllDataDefClient extends BaseClientHandler<AllDataDefDao> {

    @Override
    public void execute(AllDataDefDao dao) throws Exception {
        send(ServiceName.GET_ALL_DATA_DEFINITION, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataDefinitionError err = decodeErr(frame, new CmsGetAllDataDefinitionError());
        throw new IOException("GetAllDataDefinition rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllDataDefDao dao) throws IOException {
        CmsGetAllDataDefinitionResponse resp = decodeResp(frame, new CmsGetAllDataDefinitionResponse());
        CmsClientOperator.accumulatePage(content(), resp, "data");
    }

    @Override
    protected void setPaginationCursor(AllDataDefDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
