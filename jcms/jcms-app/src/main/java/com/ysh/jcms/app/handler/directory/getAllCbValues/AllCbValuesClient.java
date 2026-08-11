package com.ysh.jcms.app.handler.directory.getAllCbValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllCbValuesResponse;
import com.ysh.jcms.data.sequence.directory.CmsCbValueEntry;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllCbValuesClient extends BaseClientHandler<AllCbValuesDao> {

    public static final class CbEntry {
        public final String reference;
        public final int cbType;

        public CbEntry(String reference, int cbType) {
            this.reference = reference;
            this.cbType = cbType;
        }
    }

    @Override
    public void execute(AllCbValuesDao dao) throws Exception {
        send(ServiceName.GET_ALL_CB_VALUES, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllCbValuesError err = decodeErr(frame, new CmsGetAllCbValuesError());
        throw new IOException("GetAllCBValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllCbValuesDao dao) throws IOException {
        CmsGetAllCbValuesResponse resp = decodeResp(frame, new CmsGetAllCbValuesResponse());

        List<CbEntry> entries = new ArrayList<>();
        for (CmsCbValueEntry src : resp.cbValue) {
            String ref = src.reference.value();
            if (ref.isEmpty())
                continue;
            entries.add(new CbEntry(ref, src.value.choice()));
        }
        CmsClientOperator.page(content()).add("cbValue", entries).moreFollows(resp.moreFollows.value()).lastRef(entries, e -> e.reference);
        log.info("GetAllCBValues page: {} entries (moreFollows={})", entries.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(AllCbValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}