package com.ysh.jcms.app.handler.directory.getServerDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetServerDirectoryResponse;
import com.ysh.jcms.data.enumerate.CmsObjectClass;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;
import java.util.stream.Collectors;

public class SvrDirServer extends BaseServerHandler {

    public SvrDirServer() {
        super(ServiceName.GET_SERVER_DIRECTORY, CmsGetServerDirectoryRequest.class, CmsGetServerDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetServerDirectoryRequest req = (CmsGetServerDirectoryRequest) rawReq;
        log.info("GetServerDirectory from {}: reqId={}, objectClass={}, refAfter={}, present={}", session.getSessionId(), reqId,
                req.getObjectClass(), req.isPresent("referenceAfter") ? req.referenceAfter.value() : null, req.isPresent("referenceAfter"));

        if (req.getObjectClass() != CmsObjectClass.LOGICAL_DEVICE)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // 只返回当前关联 IED（访问点）下的逻辑设备，而非全站所有 IED
        SclIED ied = requireIed(session, reqId);
        List<String> ldNames = ied.lDevices().stream().map(SclLDevice::inst).collect(Collectors.toList());

        List<String> afterList = after(ldNames, req.isPresent("referenceAfter") ? req.referenceAfter.value() : null, reqId);

        CmsGetServerDirectoryResponse resp = new CmsGetServerDirectoryResponse();
        for (String name : afterList)
            resp.reference.add(new CmsObjectReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }
}
