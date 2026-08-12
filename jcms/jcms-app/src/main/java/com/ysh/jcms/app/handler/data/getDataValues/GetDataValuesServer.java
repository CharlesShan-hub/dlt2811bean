package com.ysh.jcms.app.handler.data.getDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.data.choice.CmsData;
import com.ysh.jcms.core.data.enumerate.CmsServiceError;
import com.ysh.jcms.core.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesError;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesRequest;
import com.ysh.jcms.core.pdu.data.CmsGetDataValuesResponse;
import com.ysh.jcms.utils.scl.convert.DataConverter;
import com.ysh.jcms.utils.scl.convert.DataValueResolver;
import com.ysh.jcms.utils.scl.convert.DataValueEntry;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataValuesServer extends BaseServerHandler<CmsGetDataValuesRequest, CmsGetDataValuesError> {

    public GetDataValuesServer() {
        super(CmsServiceInfo.GET_DATA_VALUES, CmsGetDataValuesRequest.class, CmsGetDataValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsGetDataValuesRequest req, int reqId) {
        log.info("GetDataValues from {}: reqId={}, {} refs", session.sessionId(), reqId, req.data.size());

        SclIED ied = requireIed(session, reqId);

        CmsGetDataValuesResponse resp = new CmsGetDataValuesResponse();

        try {
            for (CmsDataRefEntry refEntry : req.data) {
                String ref = str(refEntry.reference);
                if (ref == null)
                    continue;

                String fcCode = fcCode(refEntry.isPresent("fc") ? refEntry.fc.value() : -1);

                Navigator nav = Navigator.go(getSclDocument(session), ied, ref);
                DataValueEntry dv = DataValueResolver.resolve(nav, fcCode);
                if (dv != null && dv.val() != null && !dv.val().isEmpty()) {
                    resp.value.add(DataConverter.toCmsData(dv));
                } else {
                    resp.value.add(new CmsData().alt_visible_string("(unavailable)"));
                }
            }
        } catch (Exception e) {
            log.error("GetDataValues: unhandled exception", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }

        resp.moreFollows(false);
        log.info("GetDataValues: returning {} values", resp.value.size());
        return ok(resp, reqId);
    }
}
