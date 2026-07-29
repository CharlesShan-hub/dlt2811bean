package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.data.CmsDataRefValueEntry;
import com.ysh.jcms.pdu.data.CmsSetDataValuesError;
import com.ysh.jcms.pdu.data.CmsSetDataValuesRequest;
import com.ysh.jcms.pdu.data.CmsSetDataValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataWriterResolver;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class SetDataValuesServer extends BaseServerHandler {

    public SetDataValuesServer() {
        super(ServiceName.SET_DATA_VALUES, CmsSetDataValuesRequest.class, CmsSetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsTypeOld decoded) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) decoded;
        req.data.add(new CmsDataRefValueEntry());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) rawReq;
        log.info("SetDataValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.data.count);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        int successCount = 0;
        for (int i = 0; i < req.data.count; i++) {
            CmsDataRefValueEntry entry = req.data.items.get(i);
            String ref = str(entry.reference);
            if (ref == null)
                continue;

            String valueStr = extractValue(entry.value);
            if (valueStr == null)
                continue;

            Navigator nav = Navigator.go(doc, ied, ref);
            if (!nav.isValid())
                continue;

            if (DataWriterResolver.setValue(nav, valueStr) == CmsServiceError.NO_ERROR) {
                successCount++;
            }
        }
        log.info("SetDataValues: {}/{} entries set successfully", successCount, req.data.count);
        if (successCount < req.data.count) {
            CmsSetDataValuesError err = new CmsSetDataValuesError().reqId(reqId);
            for (int i = 0; i < req.data.count; i++) {
                int code = i < successCount ? CmsServiceError.NO_ERROR : CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT;
                err.result.add(new CmsServiceError(code));
            }
            try {
                return buildError(err.encode(), reqId);
            } catch (Exception ex) {
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }
        return ok(new CmsSetDataValuesResponse().reqId(reqId), reqId);
    }

    private static String extractValue(CmsData data) {
        int ct = data.choice.value();
        switch (ct) {
            case CmsData.CHOICE_BOOLEAN :
                return Boolean.toString(data.alt_boolean.value());
            case CmsData.CHOICE_INT8 :
                return Integer.toString(data.alt_int8.value());
            case CmsData.CHOICE_INT16 :
                return Integer.toString(data.alt_int16.value());
            case CmsData.CHOICE_INT32 :
                return Integer.toString(data.alt_int32.value());
            case CmsData.CHOICE_INT64 :
                return Long.toString(data.alt_int64.value());
            case CmsData.CHOICE_INT8U :
                return Integer.toString(data.alt_int8u.value());
            case CmsData.CHOICE_INT16U :
                return Integer.toString(data.alt_int16u.value());
            case CmsData.CHOICE_INT32U :
                return Long.toString(data.alt_int32u.value());
            case CmsData.CHOICE_INT64U :
                return data.alt_int64u.value().toString();
            case CmsData.CHOICE_FLOAT32 :
                return Float.toString(data.alt_float32.value());
            case CmsData.CHOICE_FLOAT64 :
                return Double.toString(data.alt_float64.value());
            case CmsData.CHOICE_VISIBLE_STRING :
                return str(data.alt_visible_string.value());
            case CmsData.CHOICE_UNICODE_STRING :
                return str(data.alt_unicode_string.value());
            case CmsData.CHOICE_OCTET_STRING :
                return str(data.alt_octet_string.value());
            default :
                return null;
        }
    }
}
