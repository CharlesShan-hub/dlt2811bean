package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.PaginationContext;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;

public class LdDirClient extends BaseClientHandler<LdDirDao> {

    @Override
    public void execute(LdDirDao dao) throws Exception {
        execute(dao, new PaginationContext());
    }

    @Override
    public void execute(LdDirDao dao, PaginationContext ctx) throws Exception {
        ctx.getAccumulatedRefs().clear();
        send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, dao, ctx);
        node.getContentManager().initLdDir(new ArrayList<>(ctx.getAccumulatedRefs()));
        log.info("GetLogicalDeviceDirectory succeeded: {} logical nodes", ctx.getAccumulatedRefs().size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = decodeErr(frame, new CmsGetLogicalDeviceDirectoryError());
        throw new IOException("GetLogicalDeviceDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, PaginationContext ctx) throws IOException {
        CmsGetLogicalDeviceDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalDeviceDirectoryResponse());

        for (CmsSubReference ref : resp.lnReference) {
            ctx.getAccumulatedRefs().add(ref.value());
        }
        ctx.setLastMoreFollows(resp.moreFollows.value());
        if (resp.lnReference.size() > 0) {
            ctx.setLastReference(resp.lnReference.get(resp.lnReference.size() - 1).value());
        }
        log.info("GetLogicalDeviceDirectory page: {} lnRefs (moreFollows={})", resp.lnReference.size(), ctx.isLastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(LdDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
