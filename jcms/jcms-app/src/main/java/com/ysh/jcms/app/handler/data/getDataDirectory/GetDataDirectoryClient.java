package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryError;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryRequest;
import com.ysh.jcms.svc.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.svc.data.CmsSubRefEntry;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
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

    public GetDataDirectoryClient(CmsNode node) {
        super(node);
    }

    public List<DirEntry> getLastEntries() { return lastEntries; }

    public void execute(GetDataDirectoryDao dao) throws Exception {
        CmsGetDataDirectoryRequest req = new CmsGetDataDirectoryRequest()
            .reqId(nextReqId())
            .dataReference(dao.dataReference());

        if (dao.referenceAfter() != null && !dao.referenceAfter().isEmpty()) {
            req.refAfter(dao.referenceAfter());
        }

        send(ServiceName.GET_DATA_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDirectoryError err = new CmsGetDataDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetDataDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataDirectoryResponse resp = new CmsGetDataDirectoryResponse();
        resp.dataAttribute.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<DirEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.dataAttribute.count; i++) {
            CmsSubRefEntry src = resp.dataAttribute.items.get(i);
            String ref = new String(src.reference.value(), StandardCharsets.UTF_8);
            String fc = null;
            if (src.fcPresent.value()) {
                int fcVal = src.fc.value();
                if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                    fc = FunctionalConstraint.values()[fcVal].name();
                    if ("XX".equals(fc)) fc = null;
                }
            }
            entries.add(new DirEntry(ref, fc));
        }
        this.lastEntries = entries;
        log.info("GetDataDirectory succeeded: {} entries", entries.size());
    }
}
