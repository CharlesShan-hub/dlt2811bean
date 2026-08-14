package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.app.handler.support.CmsClientOperator;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.core.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class LnDirClient extends BaseClientHandler<LnDirDao> {

    @Override
    public void execute(LnDirDao dao) throws Exception {
        send(CmsServiceInfo.GET_LOGIC_NODE_DIRECTORY, dao);
    }

    @Override
    protected void afterAll(LnDirDao dao) throws IOException {
        List<String> refs = CmsClientOperator.getResultList(content(), "reference");
        node.contentManager().initNodeDir(dao.acsiClass(), refs);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, LnDirDao dao) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());
        CmsClientOperator.accumulatePage(content(), resp, "reference");
    }

    @Override
    protected void setPaginationCursor(LnDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
