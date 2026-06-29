package com.ysh.jcms.app.handler.directory.getAllDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.directory.CmsDataValueEntry;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesError;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesRequest;
import com.ysh.jcms.svc.directory.CmsGetAllDataValuesResponse;
import com.ysh.jcms.svc.other.CmsReferenceChoice;
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class AllDataValuesServer extends BaseServerHandler {

    public AllDataValuesServer() {
        super(ServiceName.GET_ALL_DATA_VALUES, CmsGetAllDataValuesRequest.class, CmsGetAllDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetAllDataValuesRequest req = (CmsGetAllDataValuesRequest) rawReq;
        int reqId = req.reqId.value();
        String refAfter = req.refAfterPresent.value() && req.refAfter.len > 0
            ? new String(req.refAfter.value(), StandardCharsets.UTF_8) : null;

        log.info("GetAllDataValues from {}: reqId={}", session.getSessionId(), reqId);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        String ldName = null;
        String lnReference = null;
        if (req.reference.choice.value() == CmsReferenceChoice.LD_NAME) {
            ldName = req.reference.altLdName.len > 0
                ? new String(req.reference.altLdName.value(), StandardCharsets.UTF_8) : null;
        } else if (req.reference.choice.value() == CmsReferenceChoice.LN_REFERENCE) {
            lnReference = req.reference.altLnReference.len > 0
                ? new String(req.reference.altLnReference.value(), StandardCharsets.UTF_8) : null;
        }

        List<SclLN> lns = server.resolveLns(ldName, lnReference);
        if (lns == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        // Collect data object names and resolve values
        List<CmsDataValueEntry> entries = new ArrayList<>();
        int pageSize = pageSize();
        outer:
        for (SclLN ln : lns) {
            List<String> doNames = ln.getDataObjectNames(templates);
            for (String name : doNames) {
                // referenceAfter pagination
                if (refAfter != null && !refAfter.isEmpty()) {
                    if (name.equals(refAfter)) {
                        refAfter = null; // Found, stop skipping
                    }
                    continue;
                }

                // Try to resolve value
                String fullRef = (ldName != null ? ldName + "/" + ln.getFullName() : lnReference) + "." + name;
                SclDataValue sv = server.resolveDataValue(fullRef, templates);
                if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                    CmsDataValueEntry entry = new CmsDataValueEntry();
                    entry.reference(name);
                    entry.value(toCmsData(sv));
                    entries.add(entry);
                }

                // Check page size
                if (entries.size() >= pageSize) break outer;
            }
        }

        CmsGetAllDataValuesResponse resp = new CmsGetAllDataValuesResponse()
            .reqId(reqId);
        for (CmsDataValueEntry e : entries) {
            resp.data.add(e);
        }
        resp.moreFollows(entries.size() >= pageSize());

        log.info("GetAllDataValues: returning {} entries", entries.size());

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetAllDataValuesResponse", e);
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
                    data.choice.value(CmsData.CHOICE_BOOLEAN);
                    data.alt_boolean.value("true".equalsIgnoreCase(val) || "1".equals(val));
                    break;
                case "INT8":
                    data.choice.value(CmsData.CHOICE_INT8);
                    data.alt_int8.value(Byte.parseByte(val));
                    break;
                case "INT16":
                    data.choice.value(CmsData.CHOICE_INT16);
                    data.alt_int16.value(Short.parseShort(val));
                    break;
                case "INT32":
                case "ENUMERATED":
                case "CODED_ENUM":
                    data.choice.value(CmsData.CHOICE_INT32);
                    data.alt_int32.value(Integer.parseInt(val));
                    break;
                case "INT64":
                    data.choice.value(CmsData.CHOICE_INT64);
                    data.alt_int64.value(Long.parseLong(val));
                    break;
                case "INT8U":
                    data.choice.value(CmsData.CHOICE_INT8U);
                    data.alt_int8u.value(Short.parseShort(val) & 0xFF);
                    break;
                case "INT16U":
                    data.choice.value(CmsData.CHOICE_INT16U);
                    data.alt_int16u.value(Integer.parseInt(val) & 0xFFFF);
                    break;
                case "INT32U":
                    data.choice.value(CmsData.CHOICE_INT32U);
                    data.alt_int32u.value(Long.parseLong(val) & 0xFFFFFFFFL);
                    break;
                case "INT64U":
                    data.choice.value(CmsData.CHOICE_INT64U);
                    data.alt_int64u.value(new java.math.BigInteger(val));
                    break;
                case "FLOAT32":
                    data.choice.value(CmsData.CHOICE_FLOAT32);
                    data.alt_float32.value(Float.parseFloat(val));
                    break;
                case "FLOAT64":
                    data.choice.value(CmsData.CHOICE_FLOAT64);
                    data.alt_float64.value(Double.parseDouble(val));
                    break;
                default:
                    // Handle all string-based bTypes: VISIBLE_STRING*, UNICODE_STRING*,
                    // Unicode*, OCTET_STRING, BIT_STRING
                    if (bType.startsWith("OCTET_STRING")) {
                        data.choice.value(CmsData.CHOICE_OCTET_STRING);
                        data.alt_octet_string.value(val.getBytes(java.nio.charset.StandardCharsets.UTF_8));
                    } else if (bType.startsWith("BIT_STRING")) {
                        data.choice.value(CmsData.CHOICE_BIT_STRING);
                        data.alt_bit_string.value(val);
                    } else if (bType.startsWith("VISIBLE_STRING") || bType.startsWith("UNICODE") || bType.startsWith("UNICODE_STRING")) {
                        // If the string contains non-ASCII, use UNICODE_STRING (UTF8String)
                        // instead of VISIBLE_STRING to preserve Chinese chars through C codec
                        if (containsNonAscii(val)) {
                            data.choice.value(CmsData.CHOICE_UNICODE_STRING);
                            data.alt_unicode_string.value(val);
                        } else {
                            data.choice.value(CmsData.CHOICE_VISIBLE_STRING);
                            data.alt_visible_string.value(val);
                        }
                    } else {
                        // Unknown bType — try as visible string
                        data.choice.value(CmsData.CHOICE_VISIBLE_STRING);
                        data.alt_visible_string.value(val);
                    }
                    break;
            }
        } catch (Exception e) {
            // Fallback to visible string on parse failure
            data.choice.value(CmsData.CHOICE_VISIBLE_STRING);
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

    private static int pageSize() {
        return CmsConfigLoader.load().getProtocol().getMaxArraySize();
    }
}
