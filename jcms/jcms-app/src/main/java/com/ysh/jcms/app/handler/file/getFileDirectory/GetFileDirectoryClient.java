package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.data.common.CmsFileEntry;
import com.ysh.jcms.svc.file.CmsGetFileDirectoryError;
import com.ysh.jcms.svc.file.CmsGetFileDirectoryRequest;
import com.ysh.jcms.svc.file.CmsGetFileDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetFileDirectoryClient extends BaseClientHandler {

    public static final class FileEntryResult {
        public final String fileName;
        public final long fileSize;
        public final long lastModified;
        public final long checkSum;
        public FileEntryResult(String fileName, long fileSize, long lastModified, long checkSum) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.lastModified = lastModified;
            this.checkSum = checkSum;
        }
    }

    public static final class FileDirectoryResult {
        public final List<FileEntryResult> entries;
        public final boolean moreFollows;
        public FileDirectoryResult(List<FileEntryResult> entries, boolean moreFollows) {
            this.entries = entries;
            this.moreFollows = moreFollows;
        }
    }

    private FileDirectoryResult lastResult;

    public GetFileDirectoryClient(CmsNode node) {
        super(node);
    }
    public FileDirectoryResult getLastResult() {
        return lastResult;
    }

    public void execute(GetFileDirectoryDao dao) throws Exception {
        CmsGetFileDirectoryRequest req = new CmsGetFileDirectoryRequest().reqId(nextReqId());

        if (dao.pathName() != null && !dao.pathName().isEmpty()) {
            req.pathName(dao.pathName());
        }
        if (dao.fileAfter() != null && !dao.fileAfter().isEmpty()) {
            req.fileAfter(dao.fileAfter());
        }
        // startTime/stopTime would need parsing — skipped for CLI simplicity

        send(ServiceName.GET_FILE_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetFileDirectoryError err = new CmsGetFileDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetFileDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetFileDirectoryResponse resp = new CmsGetFileDirectoryResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<FileEntryResult> entries = new ArrayList<>();
        for (int i = 0; i < resp.fileEntry.count; i++) {
            CmsFileEntry fe = resp.fileEntry.items.get(i);
            long epochSeconds = fe.lastModified.secondsSinceEpoch.value();
            int fractionMicros = fe.lastModified.fractionOfSecond.value();
            entries.add(new FileEntryResult(new String(fe.fileName.value(), StandardCharsets.UTF_8), fe.fileSize.value(),
                    epochSeconds * 1000 + fractionMicros / 1000, fe.checkSum.value()));
        }

        lastResult = new FileDirectoryResult(entries, resp.moreFollows.value());
    }
}
