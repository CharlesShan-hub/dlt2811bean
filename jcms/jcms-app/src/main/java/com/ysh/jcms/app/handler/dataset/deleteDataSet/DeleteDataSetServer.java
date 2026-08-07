package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.service.SclDatasetService;
import com.ysh.jcms.utils.scl.service.SclDatasetService.DataSetResolution;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class DeleteDataSetServer extends BaseServerHandler<CmsDeleteDataSetRequest, CmsDeleteDataSetError> {

    public DeleteDataSetServer() {
        super(ServiceName.DELETE_DATA_SET, CmsDeleteDataSetRequest.class, CmsDeleteDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsDeleteDataSetRequest req, int reqId) {
        log.info("DeleteDataSet from {}: reqId={}", session.getSessionId(), reqId);

        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        DataSetResolution dsr = SclDatasetService.resolveDataSet(ied, ref);
        if (dsr == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclDataSet ds = dsr.dataSet;
        if (!ds.dynamic())
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);

        dsr.ln.dataSets().remove(ds);
        log.info("DeleteDataSet: removed '{}'", ref);
        return ok(new CmsDeleteDataSetResponse(), reqId);
    }
}
