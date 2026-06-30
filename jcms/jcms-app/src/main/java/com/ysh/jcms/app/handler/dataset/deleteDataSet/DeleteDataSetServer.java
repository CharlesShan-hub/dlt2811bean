package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String ref = req.datasetReference.len > 0
            ? new String(req.datasetReference.value(), StandardCharsets.UTF_8) : null;
        if (ref == null || ref.isEmpty()) {
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);
        }

        int slashIdx = ref.indexOf('/');
        if (slashIdx < 0) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String ldName = ref.substring(0, slashIdx);
        String rest = ref.substring(slashIdx + 1);
        int dotIdx = rest.indexOf('.');
        if (dotIdx < 0) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        String lnName = rest.substring(0, dotIdx);
        String dsName = rest.substring(dotIdx + 1);

        SclLDevice device = server.findLDeviceByInst(ldName);
        if (device == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        SclDataSet ds = ln.findDataSetByName(dsName);
        if (ds == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        if (!ds.isDynamic()) {
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        ln.getDataSets().remove(ds);
        log.info("DeleteDataSet: removed '{}' (dynamic={})", ref, ds.isDynamic());

        try {
            return buildSuccess(new CmsDeleteDataSetResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode DeleteDataSetResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }
}
