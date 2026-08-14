package com.ysh.jcms.app.handler.dataset.getDataSetValues;

import com.ysh.jcms.core.util.CmsPrinter;
import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesError;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesRequest;
import com.ysh.jcms.core.pdu.dataset.CmsGetDataSetValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.service.SclDatasetService;
import com.ysh.jcms.utils.scl.service.SclDatasetService.DataSetResolution;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataSetValuesServer extends BaseServerHandler<CmsGetDataSetValuesRequest, CmsGetDataSetValuesError> {

    public GetDataSetValuesServer() {
        super(CmsServiceInfo.GET_DATA_SET_VALUES, CmsGetDataSetValuesRequest.class, CmsGetDataSetValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetDataSetValuesRequest req, int reqId) {
        log.info("GetDataSetValues from {}: reqId={}", session.sessionId(), reqId);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);
        SclAccessPoint ap = requireAp(session, reqId);

        String ref = str(req.datasetReference);
        if (ref == null)
            return onDecodeError(reqId, CmsServiceError.PARAMETER_VALUE_INAPPROPRIATE);

        DataSetResolution dsr = SclDatasetService.resolveDataSet(ied, ap, ref);
        if (dsr == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        SclDataSet dataSet = dsr.dataSet;
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
            Navigator nav = Navigator.go(doc, ap, fcda.buildFcdaRef());
            DataValueEntry dv = DataValueResolver.resolve(nav, fcda.fc());
            if (dv != null && dv.val() != null && !dv.val().isEmpty()) {
                CmsPrinter.consoleOnly("[DEBUG] fcdaRef=" + fcda.buildFcdaRef() + " bType=" + dv.bType() + " val=" + dv.val());
                resp.value.add(DataConverter.toCmsData(dv));
                if (++count >= ps)
                    break;
            }
        }
        resp.moreFollows(count >= ps);
        log.info("GetDataSetValues: '{}' -> {} values", ref, count);
        return ok(resp, reqId);
    }
}
