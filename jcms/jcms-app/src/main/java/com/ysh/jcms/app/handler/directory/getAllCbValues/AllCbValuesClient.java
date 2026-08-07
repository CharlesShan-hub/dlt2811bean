package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllCbValuesClient extends BaseClientHandler<AllCbValuesDao> {

    /** Last response entries (accumulated across auto-pull pages). */
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
        lastEntries.clear(); // clear before fresh pull
        send(ServiceName.GET_ALL_CB_VALUES, dao);
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
        lastEntries.addAll(entries);
        lastMoreFollows = resp.moreFollows.value();
        if (!resp.cbValue.isEmpty()) {
            lastReference = resp.cbValue.get(resp.cbValue.size() - 1).reference.value();
        }
        log.info("GetAllCBValues page: {} entries (moreFollows={})", entries.size(), lastMoreFollows);
    }

    @Override
    protected void setPaginationCursor(AllCbValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
