package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.file.CmsDeleteFileError;
import com.ysh.jcms.svc.file.CmsDeleteFileRequest;
import com.ysh.jcms.svc.file.CmsDeleteFileResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class DeleteFileClient extends BaseClientHandler {

    public void execute(DeleteFileDao dao) throws Exception {
        CmsDeleteFileRequest req = new CmsDeleteFileRequest().reqId(nextReqId()).filename(dao.fileName());

        send(ServiceName.DELETE_FILE, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsDeleteFileError err = decodeErr(frame, new CmsDeleteFileError());
        throw new IOException("DeleteFile rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsDeleteFileResponse resp = new CmsDeleteFileResponse();
        resp.decode(frame.asduBytes());
        log.info("DeleteFile succeeded");
    }
}
