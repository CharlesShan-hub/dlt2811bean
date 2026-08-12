package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryError;
import com.ysh.jcms.core.pdu.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetDataDirectoryClient extends BaseClientHandler<GetDataDirectoryDao> {

    @Override
    public void execute(GetDataDirectoryDao dao) throws Exception {
        send(CmsServiceInfo.GET_DATA_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDirectoryError err = decodeErr(frame, new CmsGetDataDirectoryError());
        throw new IOException("GetDataDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataDirectoryDao dao) throws IOException {
        CmsGetDataDirectoryResponse resp = decodeResp(frame, new CmsGetDataDirectoryResponse());
        CmsClientOperator.accumulatePage(content(), resp, "dataAttribute");
    }

    @Override
    protected void setPaginationCursor(GetDataDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
