package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    @SuppressWarnings("unchecked")
    @Override
    public void execute(SvrDirDao dao) throws Exception {
        PaginationContext ctx = dao.paginationContext();
        ctx.setResult(null);
        send(ServiceName.GET_SERVER_DIRECTORY, dao);
        List<CmsObjectReference> refs = (List<CmsObjectReference>) ctx.getResult();
        node.getContentManager().initServerDir(refs.stream().map(CmsObjectReference::value).collect(Collectors.toList()));
        log.info("GetServerDirectory succeeded: {} logical devices", refs.size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SvrDirDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());

        // Accumulate raw CmsObjectReference (not extracted String) across pages
        List<CmsObjectReference> accumulated;
        if (ctx.getResult() == null) {
            accumulated = new java.util.ArrayList<>();
            ctx.setResult(accumulated);
        } else {
            accumulated = (List<CmsObjectReference>) ctx.getResult();
        }
        accumulated.addAll(resp.reference);

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
