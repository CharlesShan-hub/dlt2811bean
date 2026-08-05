package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.common.CmsFileEntry;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.common.CmsUtcTime;
import com.ysh.jcms.pdu.file.CmsGetFileDirectoryError;
import com.ysh.jcms.pdu.file.CmsGetFileDirectoryRequest;
import com.ysh.jcms.pdu.file.CmsGetFileDirectoryResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

public class GetFileDirectoryServer extends BaseServerHandler {


    public GetFileDirectoryServer() {
        super(ServiceName.GET_FILE_DIRECTORY, CmsGetFileDirectoryRequest.class, CmsGetFileDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetFileDirectoryRequest req = (CmsGetFileDirectoryRequest) rawReq;
        log.info("GetFileDirectory from {}: reqId={}", session.getSessionId(), reqId);

        String root = CmsConfigLoader.load().getProtocol().getFile().getRootPath();
        Path rootPath = Paths.get(root).normalize();

        if (!Files.exists(rootPath)) {
            try {
                Files.createDirectories(rootPath);
            } catch (Exception e) {
                /* ignore */ }
        }

        // List all files
        List<Path> files;
        try (java.util.stream.Stream<Path> walk = Files.walk(rootPath, 10)) {
            files = walk.filter(Files::isRegularFile).collect(Collectors.toList());
        } catch (IOException e) {
            log.error("GetFileDirectory: failed to list files", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        // Filter by pathName (path prefix)
        String pathName = str(req.pathName);
        if (pathName != null && !pathName.isEmpty()) {
            Path filterPath = Paths.get(root, pathName.replaceAll("\\.\\./|\\.\\.\\\\", "")).normalize();
            String filterStr = filterPath.toString().replace("\\", "/");
            files = files.stream().filter(f -> f.toString().replace("\\", "/").contains(filterStr)).collect(Collectors.toList());
        }

        // Sort by filename for consistent ordering
        files.sort(Path::compareTo);

        // Handle fileAfter (skip entries until we find the marker)
        String fileAfter = str(req.fileAfter);
        if (fileAfter != null && !fileAfter.isEmpty()) {
            boolean found = false;
            List<Path> after = new ArrayList<>();
            for (Path f : files) {
                if (found)
                    after.add(f);
                else if (f.getFileName().toString().equals(fileAfter))
                    found = true;
            }
            files = after;
        }

        // Build response with pagination
        int pageSize = pageSize();
        boolean moreFollows = files.size() > pageSize;
        if (files.size() > pageSize) {
            files = files.subList(0, pageSize);
        }

        CmsGetFileDirectoryResponse resp = new CmsGetFileDirectoryResponse().moreFollows(moreFollows);

        for (Path f : files) {
            try {
                BasicFileAttributes attrs = Files.readAttributes(f, BasicFileAttributes.class);
                long size = attrs.size();
                long lastMod = attrs.lastModifiedTime().toMillis();
                long checksum = computeCrc32(f);
                String relPath = "/" + rootPath.relativize(f).toString().replace("\\", "/");

                long seconds = lastMod / 1000;
                int micros = (int) ((lastMod % 1000) * 1000);
                CmsFileEntry fe = new CmsFileEntry().fileName(relPath).fileSize(size)
                        .lastModified(new CmsUtcTime().secondsSinceEpoch(seconds).fractionOfSecond(micros)).checkSum(checksum);
                resp.fileEntry.add(fe);
            } catch (Exception e) {
                log.warn("GetFileDirectory: skip file '{}' due to error", f, e);
            }
        }

        log.info("GetFileDirectory: returning {} entries (moreFollows={})", resp.fileEntry.size(), moreFollows);
        return ok(resp, reqId);
    }

    private static long computeCrc32(Path path) {
        try {
            byte[] data = Files.readAllBytes(path);
            CRC32 crc = new CRC32();
            crc.update(data);
            return crc.getValue();
        } catch (Exception e) {
            return 0;
        }
    }
}
