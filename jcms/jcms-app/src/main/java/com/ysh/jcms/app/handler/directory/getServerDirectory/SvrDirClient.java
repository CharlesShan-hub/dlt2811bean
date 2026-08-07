package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    @Override
    public void execute(SvrDirDao dao) throws Exception {
        execute(dao, new PaginationContext());
    }

    @Override
    public void execute(SvrDirDao dao, PaginationContext ctx) throws Exception {
        ctx.getAccumulatedRefs().clear();
        send(ServiceName.GET_SERVER_DIRECTORY, dao, ctx);
        node.getContentManager().initServerDir(new java.util.ArrayList<>(ctx.getAccumulatedRefs()));
        log.info("GetServerDirectory succeeded: {} logical devices", ctx.getAccumulatedRefs().size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());

        for (CmsObjectReference ref : resp.reference) {
            ctx.getAccumulatedRefs().add(ref.value());
        }
        ctx.setLastMoreFollows(resp.moreFollows.value());
        if (resp.reference.size() > 0) {
            ctx.setLastReference(resp.reference.get(resp.reference.size() - 1).value());
        }
        log.info("GetServerDirectory page: {} refs (moreFollows={})", resp.reference.size(), ctx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(SvrDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
