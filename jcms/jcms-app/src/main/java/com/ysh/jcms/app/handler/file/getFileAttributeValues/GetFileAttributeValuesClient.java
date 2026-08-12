package com.ysh.jcms.app.handler.file.getFileAttributeValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.core.pdu.file.CmsGetFileAttributeValuesError;
import com.ysh.jcms.core.pdu.file.CmsGetFileAttributeValuesResponse;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class GetFileAttributeValuesClient extends BaseClientHandler<GetFileAttributeValuesDao> {

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

    @Override
    public void execute(GetFileAttributeValuesDao dao) throws Exception {
        send(CmsServiceInfo.GET_FILE_ATTRIBUTE_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetFileAttributeValuesError err = decodeErr(frame, new CmsGetFileAttributeValuesError());
        throw new IOException("GetFileAttributeValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetFileAttributeValuesDao dao) throws IOException {
        CmsGetFileAttributeValuesResponse resp = decodeResp(frame, new CmsGetFileAttributeValuesResponse());

        long epochSeconds = resp.lastModified.secondsSinceEpoch.value();
        int fractionMicros = resp.lastModified.fractionOfSecond.value();
        content().res(new FileAttrResult(resp.fileName.value(), resp.fileSize.value(), epochSeconds * 1000 + fractionMicros / 1000,
                resp.checkSum.value()));
    }
}
