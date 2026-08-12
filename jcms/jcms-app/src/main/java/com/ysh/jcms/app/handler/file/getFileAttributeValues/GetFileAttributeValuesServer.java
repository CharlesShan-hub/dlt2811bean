package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.core.pdu.file.CmsGetFileAttributeValuesError;
import com.ysh.jcms.core.pdu.file.CmsGetFileAttributeValuesRequest;
import com.ysh.jcms.core.pdu.file.CmsGetFileAttributeValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.CRC32;

public class GetFileAttributeValuesServer extends BaseServerHandler<CmsGetFileAttributeValuesRequest, CmsGetFileAttributeValuesError> {

    public GetFileAttributeValuesServer() {
        super(ServiceName.GET_FILE_ATTRIBUTE_VALUES, CmsGetFileAttributeValuesRequest.class, CmsGetFileAttributeValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetFileAttributeValuesRequest req, int reqId) {
        String fileName = str(req.filename);
        log.info("GetFileAttributeValues from {}: reqId={}, file={}", session.sessionId(), reqId, fileName);

        if (fileName == null || fileName.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        String root = CmsConfigLoader.load().protocol().file().rootPath();
        String safe = fileName.replaceAll("\\.\\./|\\.\\.\\\\", "");
        Path filePath = Paths.get(root, safe).normalize();

        if (!Files.exists(filePath)) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        try {
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            byte[] data = Files.readAllBytes(filePath);
            CRC32 crc = new CRC32();
            crc.update(data);

            long lastMod = attrs.lastModifiedTime().toMillis();
            long seconds = lastMod / 1000;
            int micros = (int) ((lastMod % 1000) * 1000);
            CmsGetFileAttributeValuesResponse resp = new CmsGetFileAttributeValuesResponse().fileName(fileName).fileSize(attrs.size())
                    .lastModified(new CmsUtcTime().secondsSinceEpoch(seconds).fractionOfSecond(micros)).checkSum(crc.getValue());

            log.info("GetFileAttributeValues: file='{}' size={}", fileName, attrs.size());
            return ok(resp, reqId);
        } catch (Exception e) {
            log.error("GetFileAttributeValues: failed for '{}'", fileName, e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
