package com.ysh.jcms.app.handler.file.getFile;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.file.CmsGetFileError;
import com.ysh.jcms.pdu.file.CmsGetFileRequest;
import com.ysh.jcms.pdu.file.CmsGetFileResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class GetFileClient extends BaseClientHandler<GetFileDao> {

    /**
     * Download a file from the server. The client loops, sending GetFile requests
     * with increasing startPosition until the server returns endOfFile=true.
     */
    @Override
    public void execute(GetFileDao dao) throws Exception {
        String remoteFile = dao.fileName();
        String outputFile = dao.outputFile();
        long position = 1;
        List<byte[]> chunks = new ArrayList<>();
        long totalBytes = 0;

        while (true) {
            CmsGetFileRequest req = new CmsGetFileRequest().filename(remoteFile).startPosition(position);

            Frame frame = send(ServiceName.GET_FILE, req);

            // Decode response from the returned frame
            CmsGetFileResponse resp = decodeFrame(frame, new CmsGetFileResponse());
            traceResp(resp);

            byte[] data = resp.fileData.value();
            if (data != null && data.length > 0) {
                chunks.add(data);
                totalBytes += data.length;
                position += data.length;
            }

            if (resp.endOfFile.value())
                break;
        }

        if (outputFile != null && !outputFile.isEmpty()) {
            Path outPath = Paths.get(outputFile);
            Path parent = outPath.getParent();
            if (parent != null)
                Files.createDirectories(parent);
            java.io.OutputStream out = Files.newOutputStream(outPath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            try {
                for (byte[] chunk : chunks) {
                    out.write(chunk);
                }
            } finally {
                out.close();
            }
        }
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetFileError err = decodeFrame(frame, new CmsGetFileError());
        throw new IOException("GetFile rejected: " + err.value());
    }
}
