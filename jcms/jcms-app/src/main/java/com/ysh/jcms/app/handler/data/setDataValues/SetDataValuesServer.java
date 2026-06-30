package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.data.CmsDataRefValueEntry;
import com.ysh.jcms.svc.data.CmsSetDataValuesError;
import com.ysh.jcms.svc.data.CmsSetDataValuesRequest;
import com.ysh.jcms.svc.data.CmsSetDataValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;

public class SetDataValuesServer extends BaseServerHandler {

    public SetDataValuesServer() {
        super(ServiceName.SET_DATA_VALUES, CmsSetDataValuesRequest.class, CmsSetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) decoded;
        int allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        req.data.allocSize = allocSize;
        // Pre-allocate array entries so the C native decoder has valid
        // elements pointers to write decoded data into.
        for (int i = 0; i < allocSize; i++) {
            req.data.add(new CmsDataRefValueEntry());
        }
        req.data.write();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("SetDataValues from {}: reqId={}, {} entries",
            session.getSessionId(), reqId, req.data.count);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        int successCount = 0;
        for (int i = 0; i < req.data.count; i++) {
            CmsDataRefValueEntry entry = req.data.items.get(i);
            String ref = entry.reference.len > 0
                ? new String(entry.reference.value(), StandardCharsets.UTF_8) : null;

            if (ref == null || ref.isEmpty()) {
                log.warn("SetDataValues: empty reference at index {}", i);
                continue;
            }

            String valueStr = extractValue(entry.value);
            if (valueStr == null) {
                log.warn("SetDataValues: cannot extract value for ref={}", ref);
                continue;
            }

            int err = server.setDataValue(ref, valueStr, templates);
            if (err == CmsServiceError.NO_ERROR) {
                successCount++;
                log.debug("SetDataValues: set {} = {}", ref, valueStr);
            } else {
                log.warn("SetDataValues: failed to set {}: error={}", ref, err);
            }
        }

        log.info("SetDataValues: {}/{} entries set successfully", successCount, req.data.count);

        if (successCount < req.data.count) {
            log.warn("SetDataValues: {} entries failed on server side", req.data.count - successCount);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        try {
            return buildSuccess(new CmsSetDataValuesResponse().reqId(reqId).encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetDataValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Extract string value from a CmsData CHOICE. */
    private static String extractValue(CmsData data) {
        int ct = data.choice.value();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN:
                return Boolean.toString(data.alt_boolean.value());
            case CmsData.CHOICE_INT8:
                return Integer.toString(data.alt_int8.value());
            case CmsData.CHOICE_INT16:
                return Integer.toString(data.alt_int16.value());
            case CmsData.CHOICE_INT32:
                return Integer.toString(data.alt_int32.value());
            case CmsData.CHOICE_INT64:
                return Long.toString(data.alt_int64.value());
            case CmsData.CHOICE_INT8U:
                return Integer.toString(data.alt_int8u.value());
            case CmsData.CHOICE_INT16U:
                return Integer.toString(data.alt_int16u.value());
            case CmsData.CHOICE_INT32U:
                return Long.toString(data.alt_int32u.value());
            case CmsData.CHOICE_INT64U:
                return data.alt_int64u.value().toString();
            case CmsData.CHOICE_FLOAT32:
                return Float.toString(data.alt_float32.value());
            case CmsData.CHOICE_FLOAT64:
                return Double.toString(data.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING:
                return new String(data.alt_visible_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_UNICODE_STRING:
                return new String(data.alt_unicode_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_OCTET_STRING:
                return new String(data.alt_octet_string.value(), StandardCharsets.UTF_8);
            case CmsData.CHOICE_BIT_STRING:
                return new String(data.alt_bit_string.value(), StandardCharsets.UTF_8);
            default:
                return null;
        }
    }
}
