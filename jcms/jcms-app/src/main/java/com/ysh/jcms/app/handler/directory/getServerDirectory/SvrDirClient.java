package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(SvrDirDao dao) throws Exception {
        send(ServiceName.GET_SERVER_DIRECTORY, dao);
        Map<String, Object> map = (Map<String, Object>) dao.result();
        List<CmsObjectReference> refs = (List<CmsObjectReference>) map.get("reference");
        node.getContentManager().initServerDir(refs.stream().map(CmsObjectReference::value).collect(Collectors.toList()));
        log.info("GetServerDirectory succeeded: {} logical devices", refs.size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onSuccess(Frame frame, SvrDirDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());

        // Accumulate into the result map (protocol field names as keys)
        Map<String, Object> map = (Map<String, Object>) dao.result();
        if (map == null) {
            map = new LinkedHashMap<>();
            map.put("reference", new java.util.ArrayList<CmsObjectReference>());
            dao.result(map);
        }
        ((List<CmsObjectReference>) map.get("reference")).addAll(resp.reference);
        map.put("moreFollows", resp.moreFollows.value());

        ctx.setLastMoreFollows(resp.moreFollows.value());
        if (!resp.reference.isEmpty()) {
            ctx.setLastReference(resp.reference.get(resp.reference.size() - 1).value());
        }
        log.info("GetServerDirectory page: {} refs (moreFollows={})", resp.reference.size(), ctx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(SvrDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
