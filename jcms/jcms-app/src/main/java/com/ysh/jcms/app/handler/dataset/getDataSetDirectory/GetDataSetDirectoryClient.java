package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
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

    private List<DirEntry> lastEntries = new ArrayList<>();

    public List<DirEntry> getLastEntries() {
        return lastEntries;
    }

    @Override
    public void execute(GetDataSetDirectoryDao dao) throws Exception {
        lastEntries.clear();
        send(ServiceName.GET_DATA_SET_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetDirectoryError err = decodeErr(frame, new CmsGetDataSetDirectoryError());
        throw new IOException("GetDataSetDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataSetDirectoryResponse resp = decodeResp(frame, new CmsGetDataSetDirectoryResponse());

        List<DirEntry> entries = new ArrayList<>();
        for (CmsDataRefFcEntry e : resp.memberData) {
            entries.add(new DirEntry(e.reference.value(), null));
        }
        lastEntries.addAll(entries);
        lastMoreFollows(resp.moreFollows.value());
        if (resp.memberData.size() > 0) {
            lastReference(resp.memberData.get(resp.memberData.size() - 1).reference.value());
        }
        log.info("GetDataSetDirectory page: {} entries (moreFollows={})", entries.size(), lastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(GetDataSetDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
