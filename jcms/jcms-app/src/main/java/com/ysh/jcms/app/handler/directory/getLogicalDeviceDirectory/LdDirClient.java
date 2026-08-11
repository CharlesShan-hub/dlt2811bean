package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class LdDirClient extends BaseClientHandler<LdDirDao> {

    @Override
    public void execute(LdDirDao dao) throws Exception {
        send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, dao);
    }

    @Override
    protected void afterAll(LdDirDao dao) throws IOException {
        List<String> refs = CmsClientOperator.getResultList(content(), "lnReference");
        node.contentManager().initLdDir(refs);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = decodeErr(frame, new CmsGetLogicalDeviceDirectoryError());
        throw new IOException("GetLogicalDeviceDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, LdDirDao dao) throws IOException {
        CmsGetLogicalDeviceDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalDeviceDirectoryResponse());
        CmsClientOperator.accumulatePage(content(), resp, "lnReference");
    }

    @Override
    protected void setPaginationCursor(LdDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
