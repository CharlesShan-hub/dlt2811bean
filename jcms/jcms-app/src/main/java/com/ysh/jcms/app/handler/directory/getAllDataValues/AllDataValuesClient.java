package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.app.node.ContentManager;
import com.ysh.jcms.data.sequence.directory.CmsDataValueEntry;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.pdu.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AllDataValuesClient extends BaseClientHandler<AllDataValuesDao> {

    @Override
    public void execute(AllDataValuesDao dao) throws Exception {
        send(ServiceName.GET_ALL_DATA_VALUES, dao);
    }

    @Override
    protected void beforeAll(AllDataValuesDao dao) throws IOException {
        CmsClientOperator.initResult(dao, "data");
        node.getContentManager().initAllData(new ArrayList<>()); // clear before fresh pull
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, AllDataValuesDao dao) throws IOException {
        CmsGetAllDataValuesResponse resp = decodeResp(frame, new CmsGetAllDataValuesResponse());

        List<ContentManager.AllDataEntry> entries = new ArrayList<>();
        for (CmsDataValueEntry e : resp.data) {
            CmsDataValueEntryWrap entry = new CmsDataValueEntryWrap(e);
            if (entry.choiceType == 0)
                continue; // skip error/empty entries
            entries.add(new ContentManager.AllDataEntry(entry.reference, entry.choiceType, entry.valueString));
        }
        node.getContentManager().addAllData(entries);
        CmsClientOperator.page(dao).add("data", entries).moreFollows(resp.moreFollows.value()).lastRef(entries, e -> e.reference);
        log.info("GetAllDataValues page: {} entries (moreFollows={})", entries.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(AllDataValuesDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }

    /** Wraps CmsDataValueEntry to extract readable values after native decode. */
    private static class CmsDataValueEntryWrap {
        final String reference;
        final int choiceType;
        final String valueString;

        CmsDataValueEntryWrap(CmsDataValueEntry e) {
            this.reference = e.reference.value();
            int ct = e.value.choice();
            this.choiceType = ct;
            this.valueString = e.value.toValueString();
        }
    }
}
