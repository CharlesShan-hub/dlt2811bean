package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseClientHandler;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;

import java.io.IOException;
import java.util.List;

public class LdDirClient extends BaseClientHandler {

    public void execute(LdDirDao dao) throws Exception {
        CmsGetLogicalDeviceDirectoryRequest req = new CmsGetLogicalDeviceDirectoryRequest().reqId(nextReqId())
                .refAfter(dao.referenceAfter());
        if (dao.ldName() != null) {
            req.ldName(dao.ldName());
        }
        send(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, req);
    }

    @Override
    protected void onError(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryError err = decodeErr(frame, new CmsGetLogicalDeviceDirectoryError());
        throw new IOException(
                "GetLogicalDeviceDirectory rejected: " + err.serviceError.constantName() + " (" + err.serviceError.value() + ")");
    }

    @Override
    protected void onSuccess(Frame frame) throws IOException {
        CmsGetLogicalDeviceDirectoryResponse resp = decodeResp(frame, new CmsGetLogicalDeviceDirectoryResponse());
        List<String> names = resp.lnNames();
        node.getContentManager().initLdDir(names);
        log.info("GetLogicalDeviceDirectory succeeded: {} logical nodes", names.size());
    }
}
