package com.ysh.dlt2811bean.transport.protocol.file;

import com.ysh.dlt2811bean.datatypes.enumerated.CmsServiceError;
import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.svc.file.CmsGetFile;
import com.ysh.dlt2811bean.transport.protocol.CmsServiceHandler;
import com.ysh.dlt2811bean.transport.session.CmsServerSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class GetFileHandler implements CmsServiceHandler {

    private static final Logger log = LoggerFactory.getLogger(GetFileHandler.class);

    private static final int MAX_CHUNK_SIZE = 4096;

    private final String fileRoot;

    private final Map<String, byte[]> builtinFiles = new HashMap<>();

    public GetFileHandler() {
        this(null);
    }

    public GetFileHandler(String fileRoot) {
        this.fileRoot = fileRoot;
        builtinFiles.put("/README.txt", buildReadme());
        builtinFiles.put("/config.yaml", buildConfigYaml());
        builtinFiles.put("/data/log.txt", buildLog());
    }

    private static byte[] buildReadme() {
        return ("DL/T 2811 CMS Server — File Service Test\n"
                + "========================================\n"
                + "This directory contains sample files for testing\n"
                + "the GetFile (SC=0x80) service.\n"
                + "\n"
                + "Files:\n"
                + "  /README.txt     — this file\n"
                + "  /config.yaml    — sample configuration\n"
                + "  /data/log.txt   — sample log data\n").getBytes(StandardCharsets.UTF_8);
    }

    private static byte[] buildConfigYaml() {
        StringBuilder sb = new StringBuilder();
        sb.append("# Server Configuration\n");
        sb.append("server:\n");
        sb.append("  port: 8102\n");
        sb.append("  sslPort: 9102\n");
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
        return ServiceName.GET_FILE;
    }

    @Override
    public CmsApdu handleRequest(CmsServerSession session, CmsApdu request) {
        try {
            return doHandle(session, request);
        } catch (Exception e) {
            log.error("[Server] Error handling GetFile: {}", e.getMessage(), e);
            return buildNegativeResponse((CmsGetFile) request.getAsdu(),
                    CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsApdu doHandle(CmsServerSession session, CmsApdu request) {
        CmsGetFile asdu = (CmsGetFile) request.getAsdu();
        String fileName = asdu.fileName.get();
        long startPosition = asdu.startPosition.get();

        if (fileName == null || fileName.isEmpty()) {
            log.warn("[Server] GetFile: empty filename");
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        if (startPosition == 0) {
            log.debug("[Server] GetFile: cancel read for {}", fileName);
            session.removeAttribute(fileKey(fileName));
            return buildNegativeResponse(asdu, CmsServiceError.NO_ERROR);
        }

        byte[] fileData = resolveFile(fileName);
        if (fileData == null) {
            log.warn("[Server] GetFile: file not found: {}", fileName);
            return buildNegativeResponse(asdu, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        if (startPosition < 1 || startPosition > fileData.length + 1) {
            log.warn("[Server] GetFile: invalid startPosition {} for file {} (size={})",
                    startPosition, fileName, fileData.length);
            return buildNegativeResponse(asdu, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int offset = (int) (startPosition - 1);
        int remaining = fileData.length - offset;
        int chunkSize = Math.min(remaining, MAX_CHUNK_SIZE);
        boolean eof = (chunkSize == remaining);

        byte[] chunk = new byte[chunkSize];
        System.arraycopy(fileData, offset, chunk, 0, chunkSize);

        session.setAttribute(fileKey(fileName), System.currentTimeMillis());

        CmsGetFile response = new CmsGetFile(MessageType.RESPONSE_POSITIVE)
                .reqId(asdu.reqId().get())
                .fileData(chunk)
                .endOfFile(eof);

        log.debug("[Server] GetFile: {} pos={}, size={}, eof={}", fileName, startPosition, chunkSize, eof);
        return new CmsApdu(response);
    }

    private byte[] resolveFile(String fileName) {
        byte[] builtin = builtinFiles.get(fileName);
        if (builtin != null) {
            return builtin;
        }

        if (fileRoot != null) {
            Path filePath = Paths.get(fileRoot, fileName.startsWith("/") ? fileName.substring(1) : fileName);
            try {
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                log.debug("[Server] GetFile: file not found on filesystem: {}", filePath);
            }
        }

        return null;
    }

    private CmsApdu buildNegativeResponse(CmsGetFile request, int errorCode) {
        CmsGetFile response = new CmsGetFile(MessageType.RESPONSE_NEGATIVE)
                .reqId(request.reqId().get())
                .serviceError(errorCode);
        return new CmsApdu(response);
    }

    private static String fileKey(String fileName) {
        return "file:" + fileName;
    }
}
