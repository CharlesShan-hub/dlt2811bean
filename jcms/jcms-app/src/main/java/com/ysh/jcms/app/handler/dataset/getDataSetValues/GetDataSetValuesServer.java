package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.pdu.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataSetValuesServer extends BaseServerHandler {

    public GetDataSetValuesServer() {
        super(ServiceName.GET_DATA_SET_VALUES, CmsGetDataSetValuesRequest.class, CmsGetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetDataSetValuesRequest req = (CmsGetDataSetValuesRequest) rawReq;
        log.info("GetDataSetValues from {}: reqId={}", session.getSessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        // Parse "LD0/LLN0.dsName"
        if (!SclRefParser.isValid(ref))
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String dsName = sclRef.doName();
        if (dsName == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

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

        CmsGetDataSetValuesResponse resp = new CmsGetDataSetValuesResponse();
        int ps = pageSize(), count = 0;

        for (SclFCDA fcda : dataSet.fcDas()) {
            if (refAfter != null) {
                if (fcda.buildFcdaRef().equals(refAfter)) {
                    refAfter = null;
                }
                continue;
            }
            DataValueEntry dv = DataValueResolver.resolve(doc, fcda.buildFcdaRef(), fcda.fc());
            if (dv != null && dv.val() != null && !dv.val().isEmpty()) {
                resp.value.add(DataConverter.toCmsData(dv));
                if (++count >= ps)
                    break;
            }
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetValues: '{}' -> {} values", ref, count);
        return ok(resp, reqId);
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
