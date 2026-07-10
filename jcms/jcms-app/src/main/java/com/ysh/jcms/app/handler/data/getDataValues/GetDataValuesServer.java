package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.choice.CmsData;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.info.FunctionalConstraint;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataValuesError;
import com.ysh.jcms.svc.data.CmsGetDataValuesRequest;
import com.ysh.jcms.svc.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataValuesServer extends BaseServerHandler {

    public GetDataValuesServer() {
        super(ServiceName.GET_DATA_VALUES, CmsGetDataValuesRequest.class, CmsGetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataValuesRequest req = (CmsGetDataValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetDataValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SclIED ied = getSclIed(session);
        if (ied == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetDataValuesResponse resp = new CmsGetDataValuesResponse().reqId(reqId);

        try {
            for (int i = 0; i < req.data.count; i++) {
                CmsDataRefEntry refEntry = req.data.items.get(i);
                String ref = str(refEntry.reference);
                if (ref == null)
                    continue;

                String fcCode = null;
                if (refEntry.fcPresent.value()) {
                    int fcVal = refEntry.fc.value();
                    if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                        fcCode = FunctionalConstraint.values()[fcVal].name();
                        if ("XX".equals(fcCode))
                            fcCode = null;
                    }
                }

                Navigator nav = Navigator.go(getScl2Document(session), ied, ref);
                DataValueEntry dv = DataValueResolver.resolve(nav, fcCode);
                if (dv != null && dv.val() != null && !dv.val().isEmpty()) {
                    resp.value.add(DataConverter.toCmsData(dv));
                } else {
                    CmsData err = new CmsData();
                    err.choice(CmsData.CHOICE_VISIBLE_STRING);
                    err.alt_visible_string.value("(unavailable)");
                    resp.value.add(err);
                }
            }
        } catch (Exception e) {
            log.error("GetDataValues: unhandled exception", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        resp.moreFollows(false);
        log.info("GetDataValues: returning {} values", resp.value.items.size());
        return ok(resp, reqId);
    }
}
