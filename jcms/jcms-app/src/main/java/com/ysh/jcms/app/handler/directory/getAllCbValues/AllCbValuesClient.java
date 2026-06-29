package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.svc.directory.CmsCbValueEntry;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllCbValuesClient extends BaseClientHandler {

    /** Last response entries (set after each success) */
    public static final class CbEntry {
        public final String reference;
        public final int cbType;

        public CbEntry(String reference, int cbType) {
            this.reference = reference;
            this.cbType = cbType;
        }
    }

    private List<CbEntry> lastEntries = new ArrayList<>();

    public AllCbValuesClient(CmsNode node) {
        super(node);
    }

    public List<CbEntry> getLastEntries() { return lastEntries; }

    public void execute(AllCbValuesDao dao) throws Exception {
        CmsGetAllCbValuesRequest req = new CmsGetAllCbValuesRequest()
            .reqId(nextReqId());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        req.acsiClass(dao.acsiClass());

        if (dao.referenceAfter() != null) {
            req.refAfter(dao.referenceAfter());
        }

        send(ServiceName.GET_ALL_CB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllCbValuesError err = new CmsGetAllCbValuesError();
        err.decode(frame.asduBytes());
        throw new IOException("GetAllCBValues rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllCbValuesResponse resp = new CmsGetAllCbValuesResponse();
        resp.decode(frame.asduBytes());
        traceResp(resp);

        List<CbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.cbValue.count; i++) {
            CmsCbValueEntry src = resp.cbValue.items.get(i);
            String ref = new String(src.reference.value(), java.nio.charset.StandardCharsets.UTF_8);
            entries.add(new CbEntry(ref, src.value.choice.value()));
        }
        this.lastEntries = entries;
        log.info("GetAllCBValues succeeded: {} entries", entries.size());
    }
}
