package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SvrDirClient extends BaseClientHandler<SvrDirDao> {

    private final List<String> accumulatedRefs = new ArrayList<>();

    @Override
    public void execute(SvrDirDao dao) throws Exception {
        accumulatedRefs.clear();
        send(ServiceName.GET_SERVER_DIRECTORY, dao);
        node.getContentManager().initServerDir(new ArrayList<>(accumulatedRefs));
        log.info("GetServerDirectory succeeded: {} logical devices", accumulatedRefs.size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetServerDirectoryError err = decodeErr(frame, new CmsGetServerDirectoryError());
        throw new IOException("GetServerDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetServerDirectoryResponse resp = decodeResp(frame, new CmsGetServerDirectoryResponse());

        for (CmsObjectReference ref : resp.reference) {
            accumulatedRefs.add(ref.value());
        }
        lastMoreFollows = resp.moreFollows.value();
        if (!resp.reference.isEmpty()) {
            lastReference = resp.reference.get(resp.reference.size() - 1).value();
        }
        log.info("GetServerDirectory page: {} refs (moreFollows={})", resp.reference.size(), lastMoreFollows);
    }

    @Override
    protected void setPaginationCursor(SvrDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
