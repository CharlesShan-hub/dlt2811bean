package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.util.CmsDataUtil;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataWriterResolver;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.service.SclDatasetService;
import com.ysh.jcms.utils.scl.service.SclDatasetService.DataSetResolution;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SetDataSetValuesServer extends BaseServerHandler<CmsSetDataSetValuesRequest, CmsSetDataSetValuesError> {

    public SetDataSetValuesServer() {
        super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValuesRequest.class, CmsSetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetDataSetValuesRequest req, int reqId) {
        log.info("SetDataSetValues from {}: reqId={}, {} values", session.sessionId(), reqId, req.value.size());

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        DataSetResolution dsr = SclDatasetService.resolveDataSet(ied, ap, ref);
        if (dsr == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclDataSet dataSet = dsr.dataSet;
        String refAfter = req.isPresent("referenceAfter") ? req.referenceAfter.value() : null;

        int successCount = 0, valueIdx = 0;
        for (SclFCDA fcda : dataSet.fcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) {
                    refAfter = null;
                }
                continue;
            }
            if (valueIdx >= req.value.size())
                break;

            String valueStr = CmsDataUtil.toValueString(req.value.get(valueIdx++));
            if (valueStr == null)
                continue;

            String fcdaRef = fcda.buildFcdaRef();
            Navigator nav = Navigator.go(doc, fcdaRef);
            if (nav.isValid() && DataWriterResolver.setValue(nav, valueStr) == CmsServiceError.NO_ERROR) {
                successCount++;
            }
        }

        if (successCount == req.value.size()) {
            log.info("SetDataSetValues: all {} values set successfully", successCount);
            return ok(new CmsSetDataSetValuesResponse(), reqId);
        }
        log.warn("SetDataSetValues: {}/{} succeeded", successCount, req.value.size());
        return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }
}
