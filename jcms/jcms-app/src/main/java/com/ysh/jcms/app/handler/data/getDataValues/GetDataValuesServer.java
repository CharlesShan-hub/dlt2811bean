package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataValuesError;
import com.ysh.jcms.svc.data.CmsGetDataValuesRequest;
import com.ysh.jcms.svc.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;

public class GetDataValuesServer extends BaseServerHandler {

    public GetDataValuesServer() {
        super(ServiceName.GET_DATA_VALUES, CmsGetDataValuesRequest.class, CmsGetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetDataValuesRequest req = (CmsGetDataValuesRequest) decoded;
        req.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataValuesRequest req = (CmsGetDataValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetDataValues from {}: reqId={}, {} data references",
            session.getSessionId(), reqId, req.data.count);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        CmsGetDataValuesResponse resp = new CmsGetDataValuesResponse()
            .reqId(reqId);
        resp.value.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();

        for (int i = 0; i < req.data.count; i++) {
            CmsDataRefEntry refEntry = req.data.items.get(i);
            String ref = refEntry.reference.len > 0
                ? new String(refEntry.reference.value(), StandardCharsets.UTF_8) : null;

            if (ref == null || ref.isEmpty()) {
                log.warn("GetDataValues: empty reference at index {}", i);
                continue;
            }

            // Resolve data value from SCL
            SclDataValue sv = server.resolveDataValue(ref, templates);
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                log.debug("GetDataValues: resolved ref={} bType={} val={}", ref, sv.bType, sv.val);
                resp.value.add(toCmsData(sv));
            } else {
                log.warn("GetDataValues: cannot resolve data value for ref={}", ref);
                CmsData err = new CmsData();
                err.choice(CmsData.CHOICE_VISIBLE_STRING);
                err.alt_visible_string.value("(unavailable)");
                resp.value.add(err);
            }
        }

        resp.moreFollows(false);

        log.info("GetDataValues: returning {} values", resp.value.items.size());

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetDataValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    /** Convert SclDataValue (bType + val string) to CmsData CHOICE. */
    private static CmsData toCmsData(SclDataValue sv) {
        CmsData data = new CmsData();
        String bType = sv.bType != null ? sv.bType.toUpperCase() : "";
        String val = sv.val;

        try {
            switch (bType) {
                case "BOOLEAN":
                    data.choice(CmsData.CHOICE_BOOLEAN);
                    data.alt_boolean.value("true".equalsIgnoreCase(val) || "1".equals(val));
                    break;
                case "INT8":
                    data.choice(CmsData.CHOICE_INT8);
                    data.alt_int8.value(Byte.parseByte(val));
                    break;
                case "INT16":
                    data.choice(CmsData.CHOICE_INT16);
                    data.alt_int16.value(Short.parseShort(val));
                    break;
                case "INT32":
                case "ENUM":
                case "ENUMERATED":
                case "CODED_ENUM":
                    data.choice(CmsData.CHOICE_INT32);
                    data.alt_int32.value(Integer.parseInt(val));
                    break;
                case "INT64":
                    data.choice(CmsData.CHOICE_INT64);
                    data.alt_int64.value(Long.parseLong(val));
                    break;
                case "INT8U":
                    data.choice(CmsData.CHOICE_INT8U);
                    data.alt_int8u.value(Short.parseShort(val) & 0xFF);
                    break;
                case "INT16U":
                    data.choice(CmsData.CHOICE_INT16U);
                    data.alt_int16u.value(Integer.parseInt(val) & 0xFFFF);
                    break;
                case "INT32U":
                    data.choice(CmsData.CHOICE_INT32U);
                    data.alt_int32u.value(Long.parseLong(val) & 0xFFFFFFFFL);
                    break;
                case "INT64U":
                    data.choice(CmsData.CHOICE_INT64U);
                    data.alt_int64u.value(new java.math.BigInteger(val));
                    break;
                case "FLOAT32":
                    data.choice(CmsData.CHOICE_FLOAT32);
                    data.alt_float32.value(Float.parseFloat(val));
                    break;
                case "FLOAT64":
                    data.choice(CmsData.CHOICE_FLOAT64);
                    data.alt_float64.value(Double.parseDouble(val));
                    break;
                default:
                    if (bType.startsWith("OCTET_STRING")) {
                        data.choice(CmsData.CHOICE_OCTET_STRING);
                        data.alt_octet_string.value(val.getBytes(StandardCharsets.UTF_8));
                    } else if (bType.startsWith("BIT_STRING")) {
                        data.choice(CmsData.CHOICE_BIT_STRING);
                        data.alt_bit_string.value(val);
                    } else if (bType.startsWith("VISIBLE_STRING") || bType.startsWith("UNICODE") || bType.startsWith("UNICODE_STRING")) {
                        if (containsNonAscii(val)) {
                            data.choice(CmsData.CHOICE_UNICODE_STRING);
                            data.alt_unicode_string.value(val);
                        } else {
                            data.choice(CmsData.CHOICE_VISIBLE_STRING);
                            data.alt_visible_string.value(val);
                        }
                    } else {
                        data.choice(CmsData.CHOICE_VISIBLE_STRING);
                        data.alt_visible_string.value(val);
                    }
                    break;
            }
        } catch (Exception e) {
            data.choice(CmsData.CHOICE_VISIBLE_STRING);
            data.alt_visible_string.value(val);
        }
        return data;
    }

    private static boolean containsNonAscii(String s) {
        if (s == null) return false;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) > 127) return true;
        }
        return false;
    }
}
