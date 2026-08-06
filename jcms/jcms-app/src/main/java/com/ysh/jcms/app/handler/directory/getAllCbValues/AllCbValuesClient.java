package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesRequest;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllCbValuesClient extends BaseClientHandler<AllCbValuesDao> {

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

    @Override
    public void execute(AllCbValuesDao dao) throws Exception {
        CmsGetAllCbValuesRequest req = new CmsGetAllCbValuesRequest().referenceAfter(dao.referenceAfter()).acsiClass(dao.acsiClass());

        if (dao.ldName() != null) {
            req.reference.altLdName(dao.ldName());
        } else if (dao.lnReference() != null) {
            req.reference.altLnReference(dao.lnReference());
        }

        send(ServiceName.GET_ALL_CB_VALUES, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllCbValuesError err = decodeErr(frame, new CmsGetAllCbValuesError());
        throw new IOException("GetAllCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetAllCbValuesResponse resp = decodeResp(frame, new CmsGetAllCbValuesResponse());

        List<CbEntry> entries = new ArrayList<>();
        for (CmsCbValueEntry src : resp.cbValue) {
            String ref = src.reference.value();
            if (ref.isEmpty())
                continue; // skip empty entries
            entries.add(new CbEntry(ref, src.value.choice()));
        }
        this.lastEntries = entries;
        log.info("GetAllCBValues succeeded: {} entries", entries.size());
    }
}
