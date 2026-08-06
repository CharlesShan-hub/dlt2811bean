package com.ysh.jcms.app.handler.directory.getLogicalDeviceDirectory;

import com.ysh.jcms.app.handler.BaseServerHandler;
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

public class LdDirServer extends BaseServerHandler<CmsGetLogicalDeviceDirectoryRequest, CmsGetLogicalDeviceDirectoryError> {

    public LdDirServer() {
        super(ServiceName.GET_LOGIC_DEVICE_DIRECTORY, CmsGetLogicalDeviceDirectoryRequest.class, CmsGetLogicalDeviceDirectoryError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetLogicalDeviceDirectoryRequest req, int reqId) {
        String ldName = req.isPresent("ldName") ? req.ldName.value() : null;
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;
        log.info("GetLogicalDeviceDirectory from {}: reqId={}, ldName={}, referenceAfter={}", session.getSessionId(), reqId, ldName,
                refAfter);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        List<String> lnNames;
        if (ldName != null) {
            SclLDevice device = findLdByInst(ied, ldName);
            if (device == null)
                return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
            lnNames = getLnNames(device);
        } else {
            // 省略 ldName：只返回当前连接 IED 的逻辑设备（标准 8.3.2）。
            // 不能遍历整个 SCL 的所有 IED，否则 referenceAfter 会因多 IED 重复引用而歧义
            lnNames = getAllLnNames(ied);
        }

        lnNames = after(lnNames, refAfter, reqId);

        // 分页：单次响应不超过 pageSize 条，moreFollows 提示客户端续拉
        int pageSize = pageSize();
        boolean more = lnNames.size() > pageSize;
        int limit = more ? pageSize : lnNames.size();

        CmsGetLogicalDeviceDirectoryResponse resp = new CmsGetLogicalDeviceDirectoryResponse();
        for (int i = 0; i < limit; i++)
            resp.lnReference.add(new CmsSubReference(lnNames.get(i)));
        resp.moreFollows(more);
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

    private static List<String> getAllLnNames(SclIED ied) {
        List<String> names = new ArrayList<>();
        for (SclLDevice ld : ied.lDevices()) {
            // 未指定 ldName：返回完整引用（LD/LN），满足标准 8.3.2.2 a) 与 d)
            for (String n : getLnNames(ld)) {
                names.add(ld.inst() + "/" + n);
            }
        }
        return names;
    }
}
