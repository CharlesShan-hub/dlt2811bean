package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
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
        execute(dao, new PaginationContext());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(AllDataValuesDao dao, PaginationContext ctx) throws Exception {
        ctx.setResult(new ArrayList<ContentManager.AllDataEntry>());
        node.getContentManager().initAllData(new ArrayList<>()); // clear before fresh pull
        send(ServiceName.GET_ALL_DATA_VALUES, dao, ctx);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetAllDataValuesError err = decodeErr(frame, new CmsGetAllDataValuesError());
        throw new IOException("GetAllDataValues rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        CmsGetAllDataValuesResponse resp = decodeResp(frame, new CmsGetAllDataValuesResponse());

        List<ContentManager.AllDataEntry> entries = new ArrayList<>();
        for (CmsDataValueEntry e : resp.data) {
            CmsDataValueEntryWrap entry = new CmsDataValueEntryWrap(e);
            if (entry.choiceType == 0)
                continue; // skip error/empty entries
            entries.add(new ContentManager.AllDataEntry(entry.reference, entry.choiceType, entry.valueString));
        }
        node.getContentManager().addAllData(entries);
        // also accumulate in ctx for thread-safe API responses
        List<ContentManager.AllDataEntry> all = (List<ContentManager.AllDataEntry>) ctx.getResult();
        if (all != null) {
            all.addAll(entries);
        }
        ctx.setLastMoreFollows(resp.moreFollows.value());
        if (resp.data.size() > 0) {
            ctx.setLastReference(resp.data.get(resp.data.size() - 1).reference.value());
        }
        log.info("GetAllDataValues page: {} entries (moreFollows={})", entries.size(), ctx.isLastMoreFollows());
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
