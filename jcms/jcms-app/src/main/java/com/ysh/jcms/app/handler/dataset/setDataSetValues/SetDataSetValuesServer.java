package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

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
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

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
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        int successCount = 0;
        int valueIdx = 0;
        boolean skipUntilAfter = (refAfter != null && !refAfter.isEmpty());
        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (skipUntilAfter) {
                String fcdaRef = fcda.buildFcdaRef();
                if (fcdaRef.equals(refAfter)) {
                    skipUntilAfter = false;
                }
                continue;
            }
            if (valueIdx >= req.value.count) break;

            CmsData data = req.value.items.get(valueIdx);
            String valueStr = extractValue(data);
            if (valueStr != null) {
                String fcdaRef = fcda.buildFcdaRef();
                int err = server.setDataValue(fcdaRef, valueStr, templates);
                if (err == CmsServiceError.NO_ERROR) {
                    successCount++;
                    log.debug("SetDataSetValues: set {} = {}", fcdaRef, valueStr);
                } else {
                    log.warn("SetDataSetValues: failed to set {} (err={})", fcdaRef, err);
                }
            }
            valueIdx++;
        }

        if (successCount == req.value.count) {
            log.info("SetDataSetValues: all {} values set successfully", successCount);
            try {
                return buildSuccess(new CmsSetDataSetValuesResponse().reqId(reqId).encode(), reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetDataSetValuesResponse", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        } else {
            log.warn("SetDataSetValues: {}/{} succeeded, returning error", successCount, req.value.count);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
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
            case CmsData.CHOICE_VISIBLE_STRING:
                return new String(d.alt_visible_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_UNICODE_STRING:
                return new String(d.alt_unicode_string.value(), StandardCharsets.UTF_8);
            default:                            return null;
        }
    }
}
