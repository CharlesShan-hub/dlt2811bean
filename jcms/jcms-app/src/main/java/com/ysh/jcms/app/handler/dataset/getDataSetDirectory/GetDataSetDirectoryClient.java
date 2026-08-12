package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetDataSetDirectoryClient extends BaseClientHandler<GetDataSetDirectoryDao> {

    @Override
    public void execute(GetDataSetDirectoryDao dao) throws Exception {
        send(CmsServiceInfo.GET_DATA_SET_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetDirectoryError err = decodeErr(frame, new CmsGetDataSetDirectoryError());
        throw new IOException("GetDataSetDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataSetDirectoryDao dao) throws IOException {
        CmsGetDataSetDirectoryResponse resp = decodeResp(frame, new CmsGetDataSetDirectoryResponse());
        CmsClientOperator.accumulatePage(content(), resp, "memberData");
    }

    @Override
    protected void setPaginationCursor(GetDataSetDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
