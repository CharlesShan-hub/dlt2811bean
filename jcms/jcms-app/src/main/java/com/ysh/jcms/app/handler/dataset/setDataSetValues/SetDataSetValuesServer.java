package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.RefUtil;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDataSetValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetDataSetValuesServer.class);

    public SetDataSetValuesServer() {
        super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValuesRequest.class, null);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetDataSetValuesRequest req = (CmsSetDataSetValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("SetDataSetValues from {}: reqId={}, {} values", session.getSessionId(), reqId, req.value.count);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ref = str(req.datasetReference);
        if (ref == null) return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        RefUtil.ResolveResult r = RefUtil.resolve(server, ref);
        if (r == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        RefUtil.RefParts p = r.ref;
        SclDataSet dataSet = (p.doName != null) ? r.ln.findDataSetByName(p.doName) : null;
        if (dataSet == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        int successCount = 0, valueIdx = 0;
        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) { refAfter = null; }
                continue;
            }
            if (valueIdx >= req.value.count) break;

            String valueStr = extractValue(req.value.items.get(valueIdx++));
            if (valueStr != null && server.setDataValue(fcda.buildFcdaRef(), valueStr, templates) == CmsServiceError.NO_ERROR)
                successCount++;
        }

        if (successCount == req.value.count) {
            log.info("SetDataSetValues: all {} values set successfully", successCount);
            return ok(new CmsSetDataSetValuesResponse().reqId(reqId), reqId);
        }
        log.warn("SetDataSetValues: {}/{} succeeded", successCount, req.value.count);
        return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
    }

    private static String extractValue(CmsData d) {
        int ct = d.choice.value();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN:       return Boolean.toString(d.alt_boolean.value());
            case CmsData.CHOICE_INT8:           return Integer.toString(d.alt_int8.value());
            case CmsData.CHOICE_INT16:          return Integer.toString(d.alt_int16.value());
            case CmsData.CHOICE_INT32:          return Integer.toString(d.alt_int32.value());
            case CmsData.CHOICE_INT64:          return Long.toString(d.alt_int64.value());
            case CmsData.CHOICE_INT8U:          return Integer.toString(d.alt_int8u.value());
            case CmsData.CHOICE_INT16U:         return Integer.toString(d.alt_int16u.value());
            case CmsData.CHOICE_INT32U:         return Long.toString(d.alt_int32u.value());
            case CmsData.CHOICE_FLOAT32:        return Float.toString(d.alt_float32.value());
            case CmsData.CHOICE_FLOAT64:        return Double.toString(d.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING: return str(d.alt_visible_string.value());
            case CmsData.CHOICE_UNICODE_STRING: return str(d.alt_unicode_string.value());
            default:                            return null;
        }
    }
}
