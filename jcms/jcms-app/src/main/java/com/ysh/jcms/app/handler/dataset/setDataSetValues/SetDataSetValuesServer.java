package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.pdu.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataWriterResolver;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SetDataSetValuesServer extends BaseServerHandler {


    public SetDataSetValuesServer() {
        super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValuesRequest.class, CmsSetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsSetDataSetValuesRequest req = (CmsSetDataSetValuesRequest) rawReq;
        log.info("SetDataSetValues from {}: reqId={}, {} values", session.getSessionId(), reqId, req.value.size());

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String dsName = ref.substring(dotIdx + 1);

        SclLDevice device = findLd(ied, ldName);
        if (device == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

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

            String valueStr = extractValue(req.value.get(valueIdx++));
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

    private static String extractValue(CmsData d) {
        int ct = d.choice();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN :
                return Boolean.toString(d.alt_boolean.value());
            case CmsData.CHOICE_INT8 :
                return Integer.toString(d.alt_int8.value());
            case CmsData.CHOICE_INT16 :
                return Integer.toString(d.alt_int16.value());
            case CmsData.CHOICE_INT32 :
                return Integer.toString(d.alt_int32.value());
            case CmsData.CHOICE_INT64 :
                return Long.toString(d.alt_int64.value());
            case CmsData.CHOICE_INT8U :
                return Integer.toString(d.alt_int8u.value());
            case CmsData.CHOICE_INT16U :
                return Integer.toString(d.alt_int16u.value());
            case CmsData.CHOICE_INT32U :
                return Long.toString(d.alt_int32u.value());
            case CmsData.CHOICE_FLOAT32 :
                return Float.toString(d.alt_float32.value());
            case CmsData.CHOICE_FLOAT64 :
                return Double.toString(d.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING :
                return (String) d.alt_visible_string.toJsonValue();
            case CmsData.CHOICE_UNICODE_STRING :
                return (String) d.alt_unicode_string.toJsonValue();
            default :
                return null;
        }
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
