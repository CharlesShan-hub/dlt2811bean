package com.ysh.jcms.app.handler.directory.getLogicalNodeDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalNodeDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class LnDirClient extends BaseClientHandler<LnDirDao> {

    @Override
    public void execute(LnDirDao dao) throws Exception {
        send(ServiceName.GET_LOGIC_NODE_DIRECTORY, dao);
    }

    @Override
    protected void beforeAll(LnDirDao dao) throws IOException {
        CmsClientOperator.initResult(dao, "reference");
    }

    @Override
    protected void afterAll(LnDirDao dao) throws IOException {
        List<CmsSubReference> refs = CmsClientOperator.getResultList(dao, "reference");
        node.getContentManager().initNodeDir(dao.acsiClass(), refs.stream().map(CmsSubReference::value).collect(Collectors.toList()));
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalNodeDirectoryError err = decodeErr(frame, new CmsGetLogicalNodeDirectoryError());
        throw new IOException("GetLogicalNodeDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, LnDirDao dao) throws IOException {
        CmsGetLogicalNodeDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalNodeDirectoryResponse());
        CmsClientOperator.page(dao).add("reference", resp.reference).moreFollows(resp.moreFollows.value()).lastRef(resp.reference,
                CmsSubReference::value);
        log.info("GetLogicalNodeDirectory page: {} refs (moreFollows={})", resp.reference.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(LnDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
