package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsSubReference;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.pdu.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.ArrayList;
import java.util.List;

public class LdDirServer extends BaseServerHandler {

    public LdDirServer() {
        super(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, CmsGetLogicalDeviceDirectoryRequest.class, CmsGetLogicalDeviceDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetLogicalDeviceDirectoryRequest req = (CmsGetLogicalDeviceDirectoryRequest) rawReq;
        String ldName = opt(req.ldNamePresent, req.ldName);
        log.info("GetLogicalDeviceDirectory from {}: reqId={}, ldName={}", session.getSessionId(), reqId, ldName);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        List<String> lnNames;
        if (ldName != null) {
            SclLDevice device = findLdByInst(ied, ldName);
            if (device == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            lnNames = getLnNames(device);
        } else {
            lnNames = getAllLnNames(doc);
        }

        lnNames = after(lnNames, opt(req.refAfterPresent, req.refAfter), reqId);

        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse().reqId(reqId);
        for (String name : lnNames)
            resp.lnReference.add(new CmsSubReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }

    private static SclLDevice findLdByInst(SclIED ied, String ldInst) {
        return ied.lDevice(ldInst);
    }

    private static List<String> getLnNames(SclLDevice device) {
        List<String> names = new ArrayList<>();
        SclLN ln0 = null;
        for (SclLN ln : device.lns()) {
            if ("LLN0".equals(ln.lnClass())) {
                ln0 = ln;
            } else {
                names.add(ln.getFullName());
            }
        }
        if (ln0 != null)
            names.add(0, ln0.getFullName());
        return names;
    }

    private static List<String> getAllLnNames(SclDocument doc) {
        List<String> names = new ArrayList<>();
        for (SclIED ied : doc.ieds()) {
            for (SclLDevice ld : ied.lDevices()) {
                names.addAll(getLnNames(ld));
            }
        }
        return names;
    }
}
