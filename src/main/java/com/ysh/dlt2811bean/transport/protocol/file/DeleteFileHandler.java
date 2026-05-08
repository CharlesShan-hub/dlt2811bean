package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class DeleteFileHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(DeleteFileHandler.class);

    private static final Set<String> PROTECTED_FILES = Set.of(
            "/README.txt", "/config.yaml", "/data/log.txt"
    );

    private final String fileRoot;

    public DeleteFileHandler() {
        this(null);
    }

    public DeleteFileHandler(String fileRoot) {
        this.fileRoot = fileRoot;
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.DELETE_FILE;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling DeleteFile: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsDeleteFile) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsDeleteFile asdu = (CmsDeleteFile) request.getAsdu();
        String fileName = asdu.fileName.get();

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] DeleteFile: empty filename");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (PROTECTED_FILES.contains(fileName)) {
            log.warn("[Server] DeleteFile: cannot delete protected file {}", fileName);
            return buildNegativeResponse(asdu, CmsServiceError.ACCESS_VIOLATION);
        }

        if (fileRoot != null) {
            Path filePath = Paths.get(fileRoot, fileName.startsWith("/") ? fileName.substring(1) : fileName);
            try {
                if (Files.deleteIfExists(filePath)) {
                    log.debug("[Server] DeleteFile: deleted {}", fileName);
                    return new CmsApdu(new CmsDeleteFile(MessageType.RESPONSE_POSITIVE)
                            .reqId(asdu.reqId().get()));
                }
            } catch (IOException e) {
                log.warn("[Server] DeleteFile: IO error deleting {}: {}", fileName, e.getMessage());
                return buildNegativeResponse(asdu, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        log.warn("[Server] DeleteFile: file not found {}", fileName);
        return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }

    private CmsApdu buildNegativeResponse(CmsDeleteFile request, int errorCode) {
        CmsDeleteFile response = new CmsDeleteFile(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
