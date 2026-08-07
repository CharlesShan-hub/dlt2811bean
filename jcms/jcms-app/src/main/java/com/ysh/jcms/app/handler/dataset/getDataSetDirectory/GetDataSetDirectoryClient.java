package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.sequence.dataset.CmsDataRefFcEntry;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataSetDirectoryClient extends BaseClientHandler<GetDataSetDirectoryDao> {

    public static final class DirEntry {
        public final String reference;
        public final String fc;

        public DirEntry(String reference, String fc) {
            this.reference = reference;
            this.fc = fc;
        }
    }

    @Override
    public void execute(GetDataSetDirectoryDao dao) throws Exception {
        execute(dao, new PaginationContext());
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(GetDataSetDirectoryDao dao, PaginationContext ctx) throws Exception {
        ctx.setResult(new ArrayList<DirEntry>());
        send(ServiceName.GET_DATA_SET_DIRECTORY, dao, ctx);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetDirectoryError err = decodeErr(frame, new CmsGetDataSetDirectoryError());
        throw new IOException("GetDataSetDirectory rejected: " + err.value());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        CmsGetDataSetDirectoryResponse resp = decodeResp(frame, new CmsGetDataSetDirectoryResponse());

        List<DirEntry> entries = new ArrayList<>();
        for (CmsDataRefFcEntry e : resp.memberData) {
            entries.add(new DirEntry(e.reference.value(), null));
        }
        List<DirEntry> all = (List<DirEntry>) ctx.getResult();
        all.addAll(entries);
        ctx.setLastMoreFollows(resp.moreFollows.value());
        if (resp.memberData.size() > 0) {
            ctx.setLastReference(resp.memberData.get(resp.memberData.size() - 1).reference.value());
        }
        log.info("GetDataSetDirectory page: {} entries (moreFollows={})", entries.size(), ctx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(GetDataSetDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
