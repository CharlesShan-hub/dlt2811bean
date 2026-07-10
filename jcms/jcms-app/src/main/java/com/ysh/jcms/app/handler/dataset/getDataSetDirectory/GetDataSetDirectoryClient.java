package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class GetDataSetDirectoryClient extends BaseClientHandler {

    public static final class DirEntry {
        public final String reference;
        public final String fc;

        public DirEntry(String reference, String fc) {
            this.reference = reference;
            this.fc = fc;
        }
    }

    private List<DirEntry> lastEntries = new ArrayList<>();

    public GetDataSetDirectoryClient(CmsNode node) {
        super(node);
    }

    public List<DirEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(GetDataSetDirectoryDao dao) throws Exception {
        CmsGetDataSetDirectoryRequest req = new CmsGetDataSetDirectoryRequest().reqId(nextReqId()).datasetReference(dao.datasetReference());

        if (dao.referenceAfter() != null && !dao.referenceAfter().isEmpty()) {
            req.refAfter(dao.referenceAfter());
        }

        send(ServiceName.GET_DATA_SET_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetDirectoryError err = new CmsGetDataSetDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetDataSetDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataSetDirectoryResponse resp = new CmsGetDataSetDirectoryResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<DirEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.memberData.count; i++) {
            CmsDataRefFcEntry src = resp.memberData.items.get(i);
            String ref = new String(src.reference.value(), StandardCharsets.UTF_8);
            String fc = null;
            int fcVal = src.fc.value();
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                fc = FunctionalConstraint.values()[fcVal].name();
                if ("XX".equals(fc))
                    fc = null;
            }
            entries.add(new DirEntry(ref, fc));
        }
        this.lastEntries = entries;
        log.info("GetDataSetDirectory succeeded: {} entries", entries.size());
    }
}
