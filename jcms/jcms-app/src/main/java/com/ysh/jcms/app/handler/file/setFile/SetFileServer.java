package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.file.CmsSetFileError;
import com.ysh.jcms.core.pdu.file.CmsSetFileRequest;
import com.ysh.jcms.core.pdu.file.CmsSetFileResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class SetFileServer extends BaseServerHandler<CmsSetFileRequest, CmsSetFileError> {

    public SetFileServer() {
        super(CmsServiceInfo.SET_FILE, CmsSetFileRequest.class, CmsSetFileError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetFileRequest req, int reqId) {
        String fileName = str(req.filename);
        long startPosition = req.startPosition.value();
        byte[] fileData = req.fileData.value();
        boolean endOfFile = req.endOfFile.value();

        log.info("SetFile from {}: reqId={}, file={}, startPosition={}, dataLen={}, eof={}", session.sessionId(), reqId, fileName,
                startPosition, fileData != null ? fileData.length : 0, endOfFile);

        if (fileName == null || fileName.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String root = CmsConfigLoader.load().protocol().file().rootPath();
        String safe = fileName.replaceAll("\\.\\./|\\.\\.\\\\", "");
        Path filePath = Paths.get(root, safe).normalize();

        // startPosition == 0 means abort
        if (startPosition == 0) {
            try {
                Files.deleteIfExists(filePath);
                log.info("SetFile: aborted upload of '{}'", fileName);
            } catch (Exception e) {
                log.warn("SetFile: failed to delete aborted file '{}'", fileName, e);
            }
            return ok(new CmsSetFileResponse(), reqId);
        }

        try {
            Path parent = filePath.getParent();
            if (parent != null)
                Files.createDirectories(parent);

            if (startPosition == 1) {
                // First chunk — create/truncate
                Files.write(filePath, fileData != null ? fileData : new byte[0], StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } else {
                // Subsequent chunks — append
                Files.write(filePath, fileData != null ? fileData : new byte[0], StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }

            if (endOfFile) {
                log.info("SetFile: completed upload of '{}'", fileName);
            }

            return ok(new CmsSetFileResponse(), reqId);
        } catch (Exception e) {
            log.error("SetFile: failed to write '{}'", fileName, e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
