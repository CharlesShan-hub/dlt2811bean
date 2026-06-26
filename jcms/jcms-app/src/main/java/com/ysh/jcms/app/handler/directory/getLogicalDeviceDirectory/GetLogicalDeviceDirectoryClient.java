package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.app.node.CmsNode;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetLogicalDeviceDirectoryClient extends BaseClientHandler {

    public GetLogicalDeviceDirectoryClient(CmsNode node) {
        super(node);
    }

    public void execute(GetLogicalDeviceDirectoryDao dao) throws Exception {
        CmsGetLogicalDeviceDirectoryRequest req = new CmsGetLogicalDeviceDirectoryRequest()
            .reqId(nextReqId());
        if (dao.ldName() != null) {
            req.ldName(dao.ldName());
        }
        if (dao.referenceAfter() != null) {
            req.refAfter(dao.referenceAfter());
        }
        send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, req.encode());
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = new CmsGetLogicalDeviceDirectoryError();
        err.decode(frame.asduBytes());
        throw new IOException("GetLogicalDeviceDirectory rejected: error=" + err.serviceError.value());
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse();
        resp.decode(frame.asduBytes());
        List<String> names = new ArrayList<>();
        for (int i = 0; i < resp.lnReference.count; i++) {
            names.add(new String(resp.lnReference.items.get(i).value()));
        }
        node.getContentManager().initLdDir(names);
        log.info("GetLogicalDeviceDirectory succeeded: {} logical nodes", names.size());
    }
}