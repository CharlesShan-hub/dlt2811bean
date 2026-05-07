package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsSetFile;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

public class SetFileHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(SetFileHandler.class);

    @Override
    public ServiceName getServiceName() {
        return ServiceName.SET_FILE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling SetFile: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsSetFile) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsSetFile asdu = (CmsSetFile) request.getAsdu();
        String fileName = asdu.fileName.get();
        long startPosition = asdu.startPosition.get();

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] SetFile: empty filename");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (startPosition == 0) {
            pendingFile(session, fileName).reset();
            log.debug("[Server] SetFile: cancelled write for {}", fileName);
            return new CmsApdu(new CmsSetFile(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        if (startPosition < 1) {
            log.warn("[Server] SetFile: invalid startPosition {}", startPosition);
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        PendingFile pf = pendingFile(session, fileName);

        if (startPosition == 1) {
            pf.reset();
        }

        byte[] data = asdu.fileData.get();
        if (data != null) {
            pf.buf.write(data, 0, data.length);
        }

        boolean eof = asdu.endOfFile.get();
        if (eof) {
            byte[] fullFile = pf.buf.toByteArray();
            pf.reset();
            log.debug("[Server] SetFile: saved {} ({} bytes)", fileName, fullFile.length);
        }

        log.debug("[Server] SetFile: {} pos={}, chunk={}B, eof={}", fileName, startPosition,
                data != null ? data.length : 0, eof);
        return new CmsApdu(new CmsSetFile(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get()));
    }

    private CmsApdu buildNegativeResponse(CmsSetFile request, int errorCode) {
        CmsSetFile response = new CmsSetFile(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }

    private static PendingFile pendingFile(CmsServerSession session, String fileName) {
        String key = "setfile:" + fileName;
        PendingFile pf = (PendingFile) session.getAttribute(key);
        if (pf == null) {
            pf = new PendingFile();
            session.setAttribute(key, pf);
        }
        return pf;
    }

    private static class PendingFile {
        final ByteArrayOutputStream buf = new ByteArrayOutputStream();

        void reset() {
            buf.reset();
        }
    }
}
