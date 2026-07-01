package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.svc.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.util.RefUtil;
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

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        RefUtil.DataSetResolveResult r = RefUtil.resolveDataSet(server, ref);
        if (r == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet ds = r.dataSet;
        if (!ds.isDynamic()) return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);

        r.ln.getDataSets().remove(ds);
        log.info("DeleteDataSet: removed '{}'", ref);
        return ok(new CmsDeleteDataSetResponse().reqId(reqId), reqId);
    }
}
