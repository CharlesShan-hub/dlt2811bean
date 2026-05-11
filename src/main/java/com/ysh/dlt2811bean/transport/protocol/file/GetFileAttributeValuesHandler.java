package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.compound.CmsFileEntry;
import com.ysh.dlt2811bean.datatypes.compound.CmsUtcTime;
import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFileAttributeValues;
import com.ysh.dlt2811bean.transport.session.CmsSession;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.zip.CRC32;
import com.ysh.dlt2811bean.transport.protocol.AbstractCmsServiceHandler;

public class GetFileAttributeValuesHandler extends AbstractCmsServiceHandler<CmsGetFileAttributeValues> {

    private final String fileRoot;
    private final Map<String, byte[]> builtinFiles = new LinkedHashMap<>();

    public GetFileAttributeValuesHandler() {
        this(null);
    }

    public GetFileAttributeValuesHandler(String fileRoot) {
        super(ServiceName.GET_FILE_ATTRIBUTE_VALUES, CmsGetFileAttributeValues::new);
        this.fileRoot = fileRoot;
        builtinFiles.put("/README.txt", buildReadme());
        builtinFiles.put("/config.yaml", buildConfigYaml());
        builtinFiles.put("/data/log.txt", buildLog());
    }

    private static byte[] buildReadme() {
        return ("DL/T 2811 CMS Server — File Service Test\n"
                + "========================================\n"
                + "Sample files for testing the GetFile service.\n").getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] buildConfigYaml() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Server Configuration\nserver:\n  port: 8102\n");
        for (int i = 0; i < 50; i++) {
            sb.append("  setting_").append(i).append(": value_").append(i).append("\n");
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] buildLog() {
        StringBuilder sb = new StringBuilder();
        sb.append("Timestamp,Event,Source,Message\n");
        for (int i = 1; i <= 200; i++) {
            sb.append(String.format("2026-05-07 %02d:%02d:%02d,EVT_%04d,DEVICE_%d,%s\n",
                    8 + (i / 60), i % 60, (i * 17) % 60,
                    i, (i % 5) + 1, "Sample log entry number " + i));
        }
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ServiceName getServiceName() {
        return ServiceName.GET_FILE_ATTRIBUTE_VALUES;
    }

    @Override
    public CmsApdu handleRequest(CmsSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetFileAttributeValues: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetFileAttributeValues) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    @Override
    protected CmsApdu doHandle(CmsSession session, CmsApdu request) {
        CmsServerSession serverSession = (CmsServerSession) session;
        CmsGetFileAttributeValues asdu = (CmsGetFileAttributeValues) request.getAsdu();
        String fileName = asdu.fileName.get();

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] GetFileAttributeValues: empty filename");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        CmsFileEntry entry = resolveFileAttributes(fileName);
        if (entry == null) {
            log.warn("[Server] GetFileAttributeValues: file not found {}", fileName);
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetFileAttributeValues response = new CmsGetFileAttributeValues(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get());
        response.fileEntry = entry;

        log.debug("[Server] GetFileAttributeValues: {} size={}", fileName, entry.fileSize.get());
        return new CmsApdu(response);
    }

    private CmsFileEntry resolveFileAttributes(String fileName) {
        byte[] data = builtinFiles.get(fileName);
        if (data != null) {
            return buildEntry(fileName, data.length, 1715000000L, computeCrc32(data));
        }

        if (fileRoot != null) {
            Path filePath = Paths.get(fileRoot, fileName.startsWith("/") ? fileName.substring(1) : fileName);
            try {
                long size = Files.size(filePath);
                FileTime ft = Files.getLastModifiedTime(filePath);
                byte[] content = Files.readAllBytes(filePath);
                return buildEntry(fileName, size, ft.toMillis() / 1000, computeCrc32(content));
            } catch (IOException e) {
                log.debug("[Server] GetFileAttributeValues: file not found on filesystem: {}", filePath);
            }
        }

        return null;
    }

    private static CmsFileEntry buildEntry(String name, long size, long epochSeconds, long crc) {
        return new CmsFileEntry()
                .fileName(name)
                .fileSize(size)
                .lastModified(new CmsUtcTime(epochSeconds, 0, 0L))
                .checkSum(crc);
    }

    private static long computeCrc32(byte[] data) {
        CRC32 crc32 = new CRC32();
        crc32.update(data);
        return crc32.getValue();
    }

    private CmsApdu buildNegativeResponse(CmsGetFileAttributeValues request, int errorCode) {
        CmsGetFileAttributeValues response = new CmsGetFileAttributeValues(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }
}
