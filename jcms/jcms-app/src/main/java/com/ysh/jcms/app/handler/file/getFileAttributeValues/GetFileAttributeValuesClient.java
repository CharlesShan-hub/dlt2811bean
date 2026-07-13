package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.file.CmsGetFileAttributeValuesError;
import com.ysh.jcms.svc.file.CmsGetFileAttributeValuesRequest;
import com.ysh.jcms.svc.file.CmsGetFileAttributeValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GetFileAttributeValuesClient extends BaseClientHandler {

    public static final class FileAttrResult {
        public final String fileName;
        public final long fileSize;
        public final long lastModified;
        public final long checkSum;
        public FileAttrResult(String fileName, long fileSize, long lastModified, long checkSum) {
            this.fileName = fileName;
            this.fileSize = fileSize;
            this.lastModified = lastModified;
            this.checkSum = checkSum;
        }
    }

    private FileAttrResult lastResult;
    public FileAttrResult getLastResult() {
        return lastResult;
    }

    public void execute(GetFileAttributeValuesDao dao) throws Exception {
        CmsGetFileAttributeValuesRequest req = new CmsGetFileAttributeValuesRequest().reqId(nextReqId()).filename(dao.fileName());

        send(ServiceName.GET_FILE_ATTRIBUTE_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetFileAttributeValuesError err = decodeErr(frame, new CmsGetFileAttributeValuesError());
        throw new IOException(
                "GetFileAttributeValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetFileAttributeValuesResponse resp = decodeResp(frame, new CmsGetFileAttributeValuesResponse());

        long epochSeconds = resp.fileEntry.lastModified.secondsSinceEpoch.value();
        int fractionMicros = resp.fileEntry.lastModified.fractionOfSecond.value();
        lastResult = new FileAttrResult(new String(resp.fileEntry.fileName.value(), StandardCharsets.UTF_8),
                resp.fileEntry.fileSize.value(), epochSeconds * 1000 + fractionMicros / 1000, resp.fileEntry.checkSum.value());
    }
}
