package com.ysh.jcms.app.handler.dataset.getDataSetDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
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
        send(ServiceName.GET_DATA_SET_DIRECTORY, dao);
    }

    @Override
    protected void beforeAll(GetDataSetDirectoryDao dao) throws IOException {
        CmsClientOperator.initResult(dao, "memberData");
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataSetDirectoryError err = decodeErr(frame, new CmsGetDataSetDirectoryError());
        throw new IOException("GetDataSetDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, GetDataSetDirectoryDao dao) throws IOException {
        CmsGetDataSetDirectoryResponse resp = decodeResp(frame, new CmsGetDataSetDirectoryResponse());

        List<DirEntry> entries = new ArrayList<>();
        for (CmsDataRefFcEntry e : resp.memberData) {
            entries.add(new DirEntry(e.reference.value(), null));
        }
        CmsClientOperator.page(dao).add("memberData", entries).moreFollows(resp.moreFollows.value()).lastRef(entries, e -> e.reference);
        log.info("GetDataSetDirectory page: {} entries (moreFollows={})", entries.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(GetDataSetDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
