package com.ysh.jcms.app.handler.dataset.setDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesError;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesRequest;
import com.ysh.jcms.svc.dataset.CmsSetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataWriterResolver;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SetDataSetValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetDataSetValuesServer.class);

    public SetDataSetValuesServer() {
        super(ServiceName.SET_DATA_SET_VALUES, CmsSetDataSetValuesRequest.class, CmsSetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetDataSetValuesRequest req = (CmsSetDataSetValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("SetDataSetValues from {}: reqId={}, {} values", session.getSessionId(), reqId, req.value.count);

        SclDocument doc = getScl2Document(session);
        if (doc == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

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

        SclLDevice device = findLd(doc, ldName);
        if (device == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclLN ln = device.findLnByFullName(lnName);
        if (ln == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataSet dataSet = ln.findDataSetByName(dsName);
        if (dataSet == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        String refAfter = opt(req.refAfterPresent, req.refAfter);

        int successCount = 0, valueIdx = 0;
        for (SclFCDA fcda : dataSet.fcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) {
                    refAfter = null;
                }
                continue;
            }
            if (valueIdx >= req.value.count)
                break;

            String valueStr = extractValue(req.value.items.get(valueIdx++));
            if (valueStr == null)
                continue;

            String fcdaRef = fcda.buildFcdaRef();
            Navigator nav = Navigator.go(doc, fcdaRef);
            if (nav.isValid() && DataWriterResolver.setValue(nav, valueStr) == CmsServiceError.NO_ERROR) {
                successCount++;
            }
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
                return str(d.alt_visible_string.value());
            case CmsData.CHOICE_UNICODE_STRING :
                return str(d.alt_unicode_string.value());
            default :
                return null;
        }
    }

    /** 跨 IED/AccessPoint 查找指定 LD 的 LDevice。 */
    private static SclLDevice findLd(SclDocument doc, String ldName) {
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null)
            return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null)
                    return ld;
            }
        }
        return null;
    }
}
