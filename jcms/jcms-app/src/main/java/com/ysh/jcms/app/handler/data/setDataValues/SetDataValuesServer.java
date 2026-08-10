package com.ysh.jcms.app.handler.data.setDataValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.sequence.data.CmsDataRefValueEntry;
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

public class SetDataValuesServer extends BaseServerHandler<CmsSetDataValuesRequest, CmsSetDataValuesError> {

    public SetDataValuesServer() {
        super(ServiceName.SET_DATA_VALUES, CmsSetDataValuesRequest.class, CmsSetDataValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetDataValuesRequest req = (CmsSetDataValuesRequest) decoded;
        req.data.add(new CmsDataRefValueEntry());
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetDataValuesRequest req, int reqId) {
        log.info("SetDataValues from {}: reqId={}, {} entries", session.sessionId(), reqId, req.data.size());

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        int successCount = 0;
        for (CmsDataRefValueEntry entry : req.data) {
            String ref = str(entry.reference);
            if (ref == null)
                continue;

            String valueStr = entry.value.toValueString();
            if (valueStr == null)
                continue;

            Navigator nav = Navigator.go(doc, ied, ref);
            if (!nav.isValid())
                continue;

            if (DataWriterResolver.setValue(nav, valueStr) == CmsServiceError.NO_ERROR) {
                successCount++;
            }
        }
        log.info("SetDataValues: {}/{} entries set successfully", successCount, req.data.size());
        if (successCount < req.data.size()) {
            CmsSetDataValuesError err = new CmsSetDataValuesError();
            for (int i = 0; i < req.data.size(); i++) {
                int code = i < successCount ? CmsServiceError.NO_ERROR : CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT;
                err.result.add(code);
            }
            try {
                return buildError(err.encode(), reqId);
            } catch (Exception ex) {
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }
        return ok(new CmsSetDataValuesResponse(), reqId);
    }

}
