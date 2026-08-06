package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.file.CmsGetFileError;
import com.ysh.jcms.pdu.file.CmsGetFileRequest;
import com.ysh.jcms.pdu.file.CmsGetFileResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GetFileServer extends BaseServerHandler<CmsGetFileRequest, CmsGetFileError> {

    // Leave room for PER overhead: reqId(2) + filename(1+max255) + startPosition(4)
    private static final int CHUNK_SIZE = 64000;

    public GetFileServer() {
        super(ServiceName.GET_FILE, CmsGetFileRequest.class, CmsGetFileError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetFileRequest req, int reqId) {
        String fileName = str(req.filename);
        long startPosition = req.startPosition.value();

        log.info("GetFile from {}: reqId={}, file={}, startPosition={}", session.getSessionId(), reqId, fileName, startPosition);

        if (fileName == null || fileName.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        // startPosition == 0 means abort
        if (startPosition == 0) {
            log.info("GetFile: client aborted download of '{}'", fileName);
            return ok(new CmsGetFileResponse().fileData(new byte[0]).endOfFile(true), reqId);
        }

        String root = CmsConfigLoader.load().protocol().file().rootPath();
        String safe = fileName.replaceAll("\\.\\./|\\.\\.\\\\", "");
        Path filePath = Paths.get(root, safe).normalize();

        if (!Files.exists(filePath)) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        try {
            byte[] allData = Files.readAllBytes(filePath);
            // startPosition is 1-based
            int offset = (int) (startPosition - 1);
            if (offset >= allData.length) {
                // Beyond end of file — return empty with endOfFile
                return ok(new CmsGetFileResponse().fileData(new byte[0]).endOfFile(true), reqId);
            }

            int remaining = allData.length - offset;
            int chunkLen = Math.min(remaining, CHUNK_SIZE);
            byte[] chunk = new byte[chunkLen];
            System.arraycopy(allData, offset, chunk, 0, chunkLen);
            boolean endOfFile = (offset + chunkLen >= allData.length);

            log.info("GetFile: returning {} bytes (offset={}, eof={})", chunkLen, offset, endOfFile);
            return ok(new CmsGetFileResponse().fileData(chunk).endOfFile(endOfFile), reqId);
        } catch (Exception e) {
            log.error("GetFile: failed to read '{}'", fileName, e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
