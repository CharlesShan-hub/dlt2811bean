package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl2.model.input.SclDataSet;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteDataSetServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(DeleteDataSetServer.class);

    public DeleteDataSetServer() {
        super(ServiceName.DELETE_DATA_SET, CmsDeleteDataSetRequest.class, CmsDeleteDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsDeleteDataSetRequest req = (CmsDeleteDataSetRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("DeleteDataSet from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        SclLDevice device = findLd(doc, ldName);
        if (device == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet ds = ln.findDataSetByName(dsName);
        if (ds == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        if (!ds.dynamic()) return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);

        ln.dataSets().remove(ds);
        log.info("DeleteDataSet: removed '{}'", ref);
        return ok(new CmsDeleteDataSetResponse().reqId(reqId), reqId);
    }

    /** 跨 IED/AccessPoint 查找指定 LD 的 LDevice。 */
    private static SclLDevice findLd(SclDocument doc, String ldName) {
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null) return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null) return ld;
            }
        }
        return null;
    }
}
