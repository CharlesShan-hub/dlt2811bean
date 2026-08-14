package com.ysh.jcms.app.handler.file.getFileDirectory;

import com.ysh.jcms.app.handler.base.BaseClientHandler;
import com.ysh.jcms.core.data.sequence.common.CmsFileEntry;
import com.ysh.jcms.core.pdu.file.CmsGetFileDirectoryError;
import com.ysh.jcms.core.pdu.file.CmsGetFileDirectoryResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetFileDirectoryClient extends BaseClientHandler<GetFileDirectoryDao> {

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

    @Override
    public void execute(GetFileDirectoryDao dao) throws Exception {
        send(CmsServiceInfo.GET_FILE_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetFileDirectoryError err = decodeErr(frame, new CmsGetFileDirectoryError());
        throw new IOException("GetFileDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetFileDirectoryDao dao) throws IOException {
        CmsGetFileDirectoryResponse resp = decodeResp(frame, new CmsGetFileDirectoryResponse());

        List<FileEntryResult> entries = new ArrayList<>();
        for (CmsFileEntry fe : resp.fileEntry) {
            long epochSeconds = fe.lastModified.secondsSinceEpoch.value();
            int fractionMicros = fe.lastModified.fractionOfSecond.value();
            entries.add(new FileEntryResult(fe.fileName.value(), fe.fileSize.value(), epochSeconds * 1000 + fractionMicros / 1000,
                    fe.checkSum.value()));
        }

        content().res(new FileDirectoryResult(entries, resp.moreFollows.value()));
    }
}
