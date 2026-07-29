package com.ysh.jcms.app.handler.file.deleteFile;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.file.CmsDeleteFileError;
import com.ysh.jcms.pdu.file.CmsDeleteFileRequest;
import com.ysh.jcms.pdu.file.CmsDeleteFileResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DeleteFileServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(DeleteFileServer.class);

    public DeleteFileServer() {
        super(ServiceName.DELETE_FILE, CmsDeleteFileRequest.class, CmsDeleteFileError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsDeleteFileRequest req = (CmsDeleteFileRequest) rawReq;
        String fileName = str(req.filename);
        log.info("DeleteFile from {}: reqId={}, file={}", session.getSessionId(), reqId, fileName);

        if (fileName == null || fileName.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        Path filePath = resolvePath(fileName);
        if (!Files.exists(filePath)) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        try {
            Files.delete(filePath);
            log.info("DeleteFile: deleted '{}'", fileName);
            return ok(new CmsDeleteFileResponse().reqId(reqId), reqId);
        } catch (Exception e) {
            log.error("DeleteFile: failed to delete '{}'", fileName, e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private static Path resolvePath(String fileName) {
        String root = CmsConfigLoader.load().getProtocol().getFile().getRootPath();
        // Sanitize: prevent path traversal
        String safe = fileName.replaceAll("\\.\\./|\\.\\.\\\\", "");
        return Paths.get(root, safe).normalize();
    }
}
