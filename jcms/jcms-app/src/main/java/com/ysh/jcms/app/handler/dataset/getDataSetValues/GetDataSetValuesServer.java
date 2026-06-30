package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
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

public class GetDataSetValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataSetValuesServer.class);

    public GetDataSetValuesServer() {
        super(ServiceName.GET_DATA_SET_VALUES, CmsGetDataSetValuesRequest.class, CmsGetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataSetValuesRequest req = (CmsGetDataSetValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetDataSetValues from {}: reqId={}", session.getSessionId(), reqId);

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

        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        // Parse "LD0/LLN0.dsAlarm"
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

        CmsGetDataSetValuesResponse resp = new CmsGetDataSetValuesResponse()
            .reqId(reqId);
        resp.value.allocSize = CmsConfigLoader.load().getProtocol().getMaxArraySize();
        int pageSize = pageSize();
        int count = 0;

        boolean skipUntilAfter = (refAfter != null && !refAfter.isEmpty());
        for (SclFCDA fcda : dataSet.getFcDas()) {
            if (skipUntilAfter) {
                String fcdaRef = fcda.buildFcdaRef();
                if (fcdaRef.equals(refAfter)) {
                    skipUntilAfter = false;
                }
                continue;
            }

            String fcdaRef = fcda.buildFcdaRef();
            String fcCode = fcda.getFc(); // fc from FCDA (e.g. "ST")
            SclDataValue sv = server.resolveDataValue(fcdaRef, templates, fcCode);
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                resp.value.add(toCmsData(sv));
                count++;
            }
            // Skip members without values (continue to next)

            if (count >= pageSize) break;
        }

        resp.moreFollows(count >= pageSize);

        log.info("GetDataSetValues: '{}' -> {} values", ref, count);

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetDataSetValuesResponse", e);
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
                case "FLOAT32":
                    data.choice(CmsData.CHOICE_FLOAT32);
                    data.alt_float32.value(Float.parseFloat(val));
                    break;
                case "FLOAT64":
                    data.choice(CmsData.CHOICE_FLOAT64);
                    data.alt_float64.value(Double.parseDouble(val));
                    break;
                default:
                    if (bType.startsWith("VISIBLE_STRING") || bType.isEmpty()) {
                        data.choice(CmsData.CHOICE_VISIBLE_STRING);
                        data.alt_visible_string.value(val);
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
}
