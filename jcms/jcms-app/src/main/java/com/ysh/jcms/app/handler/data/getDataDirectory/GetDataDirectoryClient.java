package com.ysh.jcms.app.handler.data.getDataDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.sequence.data.CmsSubRefEntry;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryError;
import com.ysh.jcms.pdu.data.CmsGetDataDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetDataDirectoryClient extends BaseClientHandler<GetDataDirectoryDao> {

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
    public void execute(GetDataDirectoryDao dao) throws Exception {
        lastEntries.clear();
        send(ServiceName.GET_DATA_DIRECTORY, dao);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetDataDirectoryError err = decodeErr(frame, new CmsGetDataDirectoryError());
        throw new IOException("GetDataDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetDataDirectoryResponse resp = decodeResp(frame, new CmsGetDataDirectoryResponse());

        List<DirEntry> entries = new ArrayList<>();
        for (CmsSubRefEntry e : resp.dataAttribute) {
            entries.add(new DirEntry(e.reference.value(), fcCode(e)));
        }
        lastEntries.addAll(entries);
        lastMoreFollows(resp.moreFollows.value());
        if (resp.dataAttribute.size() > 0) {
            lastReference(resp.dataAttribute.get(resp.dataAttribute.size() - 1).reference.value());
        }
        log.info("GetDataDirectory page: {} entries (moreFollows={})", entries.size(), lastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(GetDataDirectoryDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }

    /** FC 码值转 2 字符码；条目无 fc 或为 XX 时返回 null。 */
    private static String fcCode(CmsSubRefEntry e) {
        if (!e.isPresent("fc"))
            return null;
        int v = e.fc.value();
        if (v < 0 || v >= FunctionalConstraint.values().length)
            return null;
        String code = FunctionalConstraint.values()[v].name();
        return "XX".equals(code) ? null : code;
    }
}
