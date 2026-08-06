package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class LnDirClient extends BaseClientHandler<LnDirDao> {

    private int acsiClass;

    @Override
    public void execute(LnDirDao dao) throws Exception {
        this.acsiClass = dao.acsiClass();
        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, dao.toRequest());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());
        List<String> names = resp.refs();
        node.getContentManager().initNodeDir(acsiClass, names);
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", names.size(), acsiClass);
    }
}
