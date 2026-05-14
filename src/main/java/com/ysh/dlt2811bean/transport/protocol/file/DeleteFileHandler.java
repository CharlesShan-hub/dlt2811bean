package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsDeleteFile;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

public class DeleteFileHandler extends AbstractCmsServiceHandler<CmsDeleteFile> {

    private static final Set<String> PROTECTED_FILES = Set.of(
            "/README.txt", "/config.yaml", "/data/log.txt"
    );

    private final String fileRoot;

    public DeleteFileHandler() {
        this(null);
    }

    public DeleteFileHandler(String fileRoot) {
        super(ServiceName.DELETE_FILE, CmsDeleteFile::new);
        this.fileRoot = fileRoot;
    }

    @Override
    protected CmsApdu doServerHandle() {
        String fileName = request.getAsdu() instanceof CmsDeleteFile ? ((CmsDeleteFile) request.getAsdu()).fileName.get() : null;

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] DeleteFile: empty filename");
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (PROTECTED_FILES.contains(fileName)) {
            log.warn("[Server] DeleteFile: cannot delete protected file {}", fileName);
            return buildNegativeResponse(CmsServiceError.ACCESS_VIOLATION);
        }

        if (fileRoot != null) {
            Path filePath = Paths.get(fileRoot, fileName.startsWith("/") ? fileName.substring(1) : fileName);
            try {
                if (Files.deleteIfExists(filePath)) {
                    log.debug("[Server] DeleteFile: deleted {}", fileName);
                    return new CmsApdu(new CmsDeleteFile(MessageType.RESPONSE_POSITIVE)
                            .reqId(request.getAsdu().reqId().get()));
                }
            } catch (IOException e) {
                log.warn("[Server] DeleteFile: IO error deleting {}: {}", fileName, e.getMessage());
                return buildNegativeResponse(CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        log.warn("[Server] DeleteFile: file not found {}", fileName);
        return buildNegativeResponse(CmsServiceError.INSTANCE_NOT_AVAILABLE);
    }
}
