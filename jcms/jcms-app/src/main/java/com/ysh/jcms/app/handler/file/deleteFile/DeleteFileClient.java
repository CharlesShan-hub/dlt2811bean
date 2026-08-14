package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.pdu.file.CmsDeleteFileError;
import com.ysh.jcms.core.pdu.file.CmsDeleteFileResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class DeleteFileClient extends BaseClientHandler<DeleteFileDao> {

    @Override
    public void execute(DeleteFileDao dao) throws Exception {
        send(CmsServiceInfo.DELETE_FILE, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsDeleteFileError err = decodeErr(frame, new CmsDeleteFileError());
        throw new IOException("DeleteFile rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsDeleteFileResponse resp = new CmsDeleteFileResponse();
        resp.decode(frame.asduBytes());
    }
}
