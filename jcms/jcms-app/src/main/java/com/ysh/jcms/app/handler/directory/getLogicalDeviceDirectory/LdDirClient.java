package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LdDirClient extends BaseClientHandler<LdDirDao> {

    private final List<String> accumulatedRefs = new ArrayList<>();

    @Override
    public void execute(LdDirDao dao) throws Exception {
        accumulatedRefs.clear();
        send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, dao);
        node.getContentManager().initLdDir(new ArrayList<>(accumulatedRefs));
        log.info("GetLogicalDeviceDirectory succeeded: {} logical nodes", accumulatedRefs.size());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = decodeErr(frame, new CmsGetLogicalDeviceDirectoryError());
        throw new IOException("GetLogicalDeviceDirectory rejected: " + err.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalDeviceDirectoryResponse());

        for (CmsSubReference ref : resp.lnReference) {
            accumulatedRefs.add(ref.value());
        }
        lastMoreFollows = resp.moreFollows.value();
        if (!resp.lnReference.isEmpty()) {
            lastReference = resp.lnReference.get(resp.lnReference.size() - 1).value();
        }
        log.info("GetLogicalDeviceDirectory page: {} lnRefs (moreFollows={})", resp.lnReference.size(), lastMoreFollows);
    }

    @Override
    protected void setPaginationCursor(LdDirDao dao, String cursor) {
        dao.referenceAfter(cursor);
    }
}
