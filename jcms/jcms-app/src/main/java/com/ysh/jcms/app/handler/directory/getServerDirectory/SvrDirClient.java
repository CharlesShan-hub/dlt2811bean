package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    @Override
    public void execute(SvrDirDao dao) throws Exception {
        send(ServiceName.GET_SERVER_DIRECTORY, dao);
    }

    @Override
    protected void afterAll(SvrDirDao dao) throws IOException {
        List<String> refs = CmsClientOperator.getResultList(content(), "reference");
        node.contentManager().initServerDir(refs);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SvrDirDao dao) throws IOException {
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());
        CmsClientOperator.accumulatePage(content(), resp, "reference");
    }

    @Override
    protected void setPaginationCursor(SvrDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
