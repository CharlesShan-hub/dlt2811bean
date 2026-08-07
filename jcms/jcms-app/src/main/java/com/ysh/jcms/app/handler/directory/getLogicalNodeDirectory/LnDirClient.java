package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LnDirClient extends BaseClientHandler<LnDirDao> {

    private int acsiClass;
    private final List<String> accumulatedRefs = new ArrayList<>();

    @Override
    public void execute(LnDirDao dao) throws Exception {
        this.acsiClass = dao.acsiClass();
        accumulatedRefs.clear();
        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, dao);
        node.getContentManager().initNodeDir(acsiClass, new ArrayList<>(accumulatedRefs));
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", accumulatedRefs.size(), acsiClass);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());

        for (CmsSubReference ref : resp.reference) {
            accumulatedRefs.add(ref.value());
        }
        lastMoreFollows = resp.moreFollows.value();
        if (!resp.reference.isEmpty()) {
            lastReference = resp.reference.get(resp.reference.size() - 1).value();
        }
        log.info("GetLogicalNodeDirectory page: {} refs (moreFollows={})", resp.reference.size(), lastMoreFollows);
    }

    @Override
    protected void setPaginationCursor(LnDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
