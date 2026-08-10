package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LnDirClient extends BaseClientHandler<LnDirDao> {

    @Override
    public void execute(LnDirDao dao) throws Exception {
        LnDirContext ctx = (LnDirContext) dao.paginationContext();
        ctx.setAcsiClass(dao.acsiClass());
        ctx.getAccumulatedRefs().clear();
        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, dao);
        List<String> refs = new ArrayList<>(ctx.getAccumulatedRefs());
        node.getContentManager().initNodeDir(ctx.getAcsiClass(), refs);
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", refs.size(), ctx.getAcsiClass());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, LnDirDao dao) throws IOException {
        PaginationContext ctx = dao.paginationContext();
        LnDirContext lnCtx = (LnDirContext) ctx;
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());

        for (CmsSubReference ref : resp.reference) {
            lnCtx.getAccumulatedRefs().add(ref.value());
        }
        lnCtx.setLastMoreFollows(resp.moreFollows.value());
        if (resp.reference.size() > 0) {
            lnCtx.setLastReference(resp.reference.get(resp.reference.size() - 1).value());
        }
        log.info("GetLogicalNodeDirectory page: {} refs (moreFollows={})", resp.reference.size(), lnCtx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(LnDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
