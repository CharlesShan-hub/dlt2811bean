package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryError;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataDirectoryClient extends BaseClientHandler {

    public static final class DirEntry {
        public final String reference;
        public final String fc;

        public DirEntry(String reference, String fc) {
            this.reference = reference;
            this.fc = fc;
        }
    }

    private List<DirEntry> lastEntries = new ArrayList<>();

    public List<DirEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetDataDirectoryDao dao) throws Exception {
        CmsGetDataDirectoryRequest req = new CmsGetDataDirectoryRequest().reqId(nextReqId()).dataReference(dao.dataReference())
                .refAfter(dao.referenceAfter());

        send(ServiceName.GET_DATA_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDirectoryError err = decodeErr(frame, new CmsGetDataDirectoryError());
        throw new IOException("GetDataDirectory rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataDirectoryResponse resp = decodeResp(frame, new CmsGetDataDirectoryResponse());

        List<DirEntry> entries = new ArrayList<>();
        for (String name : resp.dataAttributes()) {
            entries.add(new DirEntry(name, null));
        }
        this.lastEntries = entries;
        log.info("GetDataDirectory succeeded: {} entries", entries.size());
    }
}
