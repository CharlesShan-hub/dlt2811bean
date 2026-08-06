package com.ysh.jcms.app.handler.file.setFile;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.file.CmsSetFileError;
import com.ysh.jcms.pdu.file.CmsSetFileRequest;
import com.ysh.jcms.pdu.file.CmsSetFileResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SetFileClient extends BaseClientHandler {

    // Leave room for PER overhead
    private static final int CHUNK_SIZE = 64000;

    /**
     * Upload a local file to the server. Splits the file into chunks and sends
     * sequentially.
     */
    public void execute(SetFileDao dao) throws Exception {
        String remoteFile = dao.remoteFile();
        String localFile = dao.localFile();

        Path localPath = Paths.get(localFile);
        if (!Files.exists(localPath)) {
            throw new IOException("Local file not found: " + localFile);
        }

        byte[] allData = Files.readAllBytes(localPath);
        long totalBytes = allData.length;
        long position = 1;
        int chunks = 0;

        log.info("SetFile: uploading '{}' -> '{}' ({} bytes)", localFile, remoteFile, totalBytes);

        while (position <= totalBytes + 1) {
            int offset = (int) (position - 1);
            int remaining = (int) (totalBytes - offset);
            int chunkLen = Math.min(remaining, CHUNK_SIZE);
            boolean isLast = (offset + chunkLen >= totalBytes);

            byte[] chunk;
            if (chunkLen > 0) {
                chunk = new byte[chunkLen];
                System.arraycopy(allData, offset, chunk, 0, chunkLen);
            } else {
                chunk = new byte[0];
            }

            CmsSetFileRequest req = new CmsSetFileRequest().filename(remoteFile).startPosition(position).fileData(chunk).endOfFile(isLast);

            Frame frame = send(ServiceName.SET_FILE, req);
            CmsSetFileResponse resp = decodeFrame(frame, new CmsSetFileResponse());
            chunks++;

            if (isLast)
                break;
            position += chunkLen;
        }

        log.info("SetFile: uploaded '{}' -> '{}' ({} bytes, {} chunks)", localFile, remoteFile, totalBytes, chunks);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsSetFileError err = decodeFrame(frame, new CmsSetFileError());
        throw new IOException("SetFile rejected: " + err.value());
    }
}
