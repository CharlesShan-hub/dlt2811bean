package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
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

    public List<CbEntry> getLastEntries() {
        return lastEntries;
    }

    public void execute(AllCbValuesDao dao) throws Exception {
        CmsGetAllCbValuesRequest req = new CmsGetAllCbValuesRequest().reqId(nextReqId()).refAfter(dao.referenceAfter());

        if (dao.ldName() != null) {
            req.reference.choice(CmsReferenceChoice.LD_NAME);
            req.reference.altLdName.value(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.choice(CmsReferenceChoice.LN_REFERENCE);
            req.reference.altLnReference.value(dao.lnReference());
        }

        req.acsiClass(dao.acsiClass());

        send(ServiceName.GET_ALL_CB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllCbValuesError err = decodeErr(frame, new CmsGetAllCbValuesError());
        throw new IOException("GetAllCBValues rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllCbValuesResponse resp = decodeResp(frame, new CmsGetAllCbValuesResponse());

        List<CbEntry> entries = new ArrayList<>();
        for (int i = 0; i < resp.cbValue.count; i++) {
            CmsCbValueEntry src = resp.cbValue.items.get(i);
            String ref = new String(src.reference.value(), java.nio.charset.StandardCharsets.UTF_8);
            if (ref.isEmpty())
                continue; // skip empty entries
            entries.add(new CbEntry(ref, src.value.choice.value()));
        }
        this.lastEntries = entries;
        log.info("GetAllCBValues succeeded: {} entries", entries.size());
    }
}
