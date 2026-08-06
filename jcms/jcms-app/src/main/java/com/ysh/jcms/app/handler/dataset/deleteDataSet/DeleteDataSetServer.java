package com.ysh.jcms.app.handler.dataset.deleteDataSet;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetError;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetRequest;
import com.ysh.jcms.pdu.dataset.CmsDeleteDataSetResponse;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

public class DeleteDataSetServer extends BaseServerHandler {

    public DeleteDataSetServer() {
        super(ServiceName.DELETE_DATA_SET, CmsDeleteDataSetRequest.class, CmsDeleteDataSetError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsDeleteDataSetRequest req = (CmsDeleteDataSetRequest) rawReq;
        log.info("DeleteDataSet from {}: reqId={}", session.getSessionId(), reqId);

        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        if (!SclRefParser.isValid(ref))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String dsName = sclRef.doName();
        if (dsName == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclLDevice device = findLd(ied, ldName);
        if (device == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet ds = ln.findDataSetByName(dsName);
        if (ds == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        if (!ds.dynamic())
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);

        ln.dataSets().remove(ds);
        log.info("DeleteDataSet: removed '{}'", ref);
        return ok(new CmsDeleteDataSetResponse(), reqId);
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
