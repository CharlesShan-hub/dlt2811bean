package com.ysh.jcms.app.handler.sg.getEditSgValue;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.sg.SgSessionState;
import com.ysh.jcms.app.handler.sg.SgSessionState.SgcState;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueError;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueRequest;
import com.ysh.jcms.svc.sg.CmsGetEditSgValueResponse;
import com.ysh.jcms.svc.sg.CmsSgRefFcEntry;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.SclDataValueResolver;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class GetEditSgValueServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetEditSgValueServer.class);

    public GetEditSgValueServer() {
        super(ServiceName.GET_EDIT_SG_VALUE, CmsGetEditSgValueRequest.class, CmsGetEditSgValueError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetEditSgValueRequest req = (CmsGetEditSgValueRequest) decoded;
        req.data.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetEditSgValueRequest req = (CmsGetEditSgValueRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetEditSGValue from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SgcState state = SgSessionState.getState(session.getSessionId());
        SclServer server = null;
        SclDataTypeTemplates templates = null;

        CmsGetEditSgValueResponse resp = new CmsGetEditSgValueResponse()
            .reqId(reqId);
        resp.value.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();

        for (int i = 0; i < req.data.count; i++) {
            CmsSgRefFcEntry entry = req.data.items.get(i);
            String ref = entry.reference.len > 0
                ? new String(entry.reference.value(), StandardCharsets.UTF_8) : null;

            if (ref == null || ref.isEmpty()) {
                log.warn("GetEditSGValue: empty reference at index {}", i);
                continue;
            }

            // Determine fc: SG → committed layer, SE → edit buffer (strictly per standard)
            int fcVal = entry.fc.value();
            boolean isSE = false;
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                isSE = "SE".equals(FunctionalConstraint.values()[fcVal].name());
            }

            byte[] val = isSE ? state.getEditValue(ref) : state.getCommittedValue(ref);
            if (val != null) {
                try {
                    CmsData data = new CmsData();
                    data.decode(val);
                    resp.value.add(data);
                    continue;
                } catch (Exception e) {
                    log.warn("GetEditSGValue: failed to decode {} value for ref={}", isSE ? "SE" : "SG", ref, e);
                }
            }

            // Fall back to SCD resolution
            if (server == null) {
                server = getSclServer(session);
                if (server == null) {
                    return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
                }
                templates = getSclDataTypeTemplates(session);
            }

            String fcCode = null;
            if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                fcCode = FunctionalConstraint.values()[fcVal].name();
                if ("XX".equals(fcCode)) fcCode = null;
            }

            SclDataValue sv = SclDataValueResolver.resolveDataValue(server, ref, templates, fcCode);
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                resp.value.add(toCmsData(sv));
            } else {
                log.warn("GetEditSGValue: cannot resolve value for ref={} fc={}", ref, fcCode);
                CmsData err = new CmsData();
                err.choice(CmsData.CHOICE_VISIBLE_STRING);
                err.alt_visible_string.value("(unavailable)");
                resp.value.add(err);
            }
        }

        resp.moreFollows(false);

        log.info("GetEditSGValue: returning {} values", resp.value.count);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetEditSGValueResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

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
                case "FLOAT32":
                    data.choice(CmsData.CHOICE_FLOAT32);
                    data.alt_float32.value(Float.parseFloat(val));
                    break;
                case "FLOAT64":
                    data.choice(CmsData.CHOICE_FLOAT64);
                    data.alt_float64.value(Double.parseDouble(val));
                    break;
                case "VISSTRING255":
                case "VISIBLE_STRING":
                case "UNICODE_STRING":
                case "VISSTRING64":
                default:
                    data.choice(CmsData.CHOICE_VISIBLE_STRING);
                    data.alt_visible_string.value(val != null ? val.getBytes(StandardCharsets.UTF_8) : new byte[0]);
                    break;
            }
        } catch (Exception e) {
            log.warn("toCmsData: failed to parse val='{}' bType='{}'", val, bType);
            data.choice(CmsData.CHOICE_VISIBLE_STRING);
            data.alt_visible_string.value(val != null ? val.getBytes(StandardCharsets.UTF_8) : new byte[0]);
        }
        return data;
    }
}
