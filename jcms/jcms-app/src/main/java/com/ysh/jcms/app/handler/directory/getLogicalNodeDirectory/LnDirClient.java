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

    // ThreadLocal for concurrency safety — each thread has its own accumulator
    private final ThreadLocal<List<String>> tlAccumulatedRefs = ThreadLocal.withInitial(ArrayList::new);
    private final ThreadLocal<Integer> tlAcsiClass = new ThreadLocal<>();

    @Override
    public void execute(LnDirDao dao) throws Exception {
        tlAcsiClass.set(dao.acsiClass());
        tlAccumulatedRefs.get().clear();
        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, dao);
        int acsiClass = tlAcsiClass.get();
        List<String> refs = new ArrayList<>(tlAccumulatedRefs.get());
        node.getContentManager().initNodeDir(acsiClass, refs);
        log.info("GetLogicalNodeDirectory succeeded: {} references, acsiClass={}", refs.size(), acsiClass);
        tlAccumulatedRefs.remove();
        tlAcsiClass.remove();
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());

        List<String> acc = tlAccumulatedRefs.get();
        for (CmsSubReference ref : resp.reference) {
            acc.add(ref.value());
        }
        lastMoreFollows(resp.moreFollows.value());
        if (resp.reference.size() > 0) {
            lastReference(resp.reference.get(resp.reference.size() - 1).value());
        }
        log.info("GetLogicalNodeDirectory page: {} refs (moreFollows={})", resp.reference.size(), lastMoreFollows());
    }

    @Override
    protected void setPaginationCursor(LnDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}