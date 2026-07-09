package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.data.common.CmsSubReference;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryError;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryRequest;
import com.ysh.jcms.svc.directory.CmsGetLogicalDeviceDirectoryResponse;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
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
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLogicalDeviceDirectoryRequest req = (CmsGetLogicalDeviceDirectoryRequest) rawReq;
        int reqId = req.reqId.value();
        String ldName = opt(req.ldNamePresent, req.ldName);
        String refAfter = opt(req.refAfterPresent, req.refAfter);
        log.info("GetLogicalDeviceDirectory from {}: reqId={}, ldName={}", session.getSessionId(), reqId, ldName);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        List<String> lnNames;
        if (ldName != null) {
            SclLDevice device = findLdByInst(doc, ldName);
            if (device == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            lnNames = getLnNames(device, refAfter);
        } else {
            lnNames = getAllLnNames(doc, refAfter);
        }
        if (lnNames == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse().reqId(reqId);
        for (String name : lnNames) resp.lnReference.add(new CmsSubReference(name));
        resp.moreFollows(false);
        return ok(resp, reqId);
    }

    private static SclLDevice findLdByInst(SclDocument doc, String ldInst) {
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    SclLDevice ld = srv.findLDeviceByInst(ldInst);
                    if (ld != null) return ld;
                }
            }
        }
        return null;
    }

    private static List<String> getLnNames(SclLDevice device, String after) {
        List<String> names = new ArrayList<>();
        SclLN ln0 = null;
        for (SclLN ln : device.lns()) {
            if ("LLN0".equals(ln.lnClass())) {
                ln0 = ln;
            } else {
                names.add(ln.getFullName());
            }
        }
        if (ln0 != null) names.add(0, ln0.getFullName());

        return filterAfter(names, after);
    }

    private static List<String> getAllLnNames(SclDocument doc, String after) {
        List<String> names = new ArrayList<>();
        for (SclIED ied : doc.ieds()) {
            for (SclAccessPoint ap : ied.accessPoints()) {
                SclServer srv = ap.server();
                if (srv != null) {
                    for (SclLDevice ld : srv.lDevices()) {
                        names.addAll(getLnNames(ld, null));
                    }
                }
            }
        }
        return filterAfter(names, after);
    }

    private static List<String> filterAfter(List<String> names, String after) {
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }
}
