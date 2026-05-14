package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsSetFile;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;
import java.io.ByteArrayOutputStream;

public class SetFileHandler extends AbstractCmsServiceHandler<CmsSetFile> {

    public SetFileHandler() {
        super(ServiceName.SET_FILE, CmsSetFile::new);
    }

    @Override
    protected CmsApdu doServerHandle() {
        CmsSetFile asdu = (CmsSetFile) request.getAsdu();
        String fileName = asdu.fileName.get();
        long startPosition = asdu.startPosition.get();

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] SetFile: empty filename");
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (startPosition == 0) {
            pendingFile(serverSession, fileName).reset();
            log.debug("[Server] SetFile: cancelled write for {}", fileName);
            return new CmsApdu(new CmsSetFile(MessageType.RESPONSE_POSITIVE)
                    .reqId(asdu.reqId().get()));
        }

        if (startPosition < 1) {
            log.warn("[Server] SetFile: invalid startPosition {}", startPosition);
            return buildNegativeResponse(CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        PendingFile pf = pendingFile(serverSession, fileName);

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
