package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.handler.CmsClientOperator;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    @Override
    public void execute(SvrDirDao dao) throws Exception {
        send(ServiceName.GET_SERVER_DIRECTORY, dao);
    }

    @Override
    protected void beforeAll(SvrDirDao dao) throws IOException {
        CmsClientOperator.initResult(dao, "reference");
    }

    @Override
    protected void afterAll(SvrDirDao dao) throws IOException {
        List<String> refs = CmsClientOperator.getResultList(dao, "reference");
        node.contentManager().initServerDir(refs);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame, SvrDirDao dao) throws IOException {
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());
        List<String> refs = resp.reference.stream().map(CmsObjectReference::value).collect(Collectors.toList());
        CmsClientOperator.page(dao).add("reference", refs).moreFollows(resp.moreFollows.value()).lastRef(resp.reference,
                CmsObjectReference::value);
        log.info("GetServerDirectory page: {} refs (moreFollows={})", resp.reference.size(), resp.moreFollows.value());
    }

    @Override
    protected void setPaginationCursor(SvrDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
