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
import com.ysh.jcms.utils.config.CmsConfigLoader;
import com.ysh.jcms.utils.scl.model.data.SclDataValue;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.template.SclDataTypeTemplates;
import com.ysh.jcms.utils.scl.util.SclDataConverter;
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

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        SclDataTypeTemplates templates = getSclDataTypeTemplates(session);

        CmsGetDataValuesResponse resp = new CmsGetDataValuesResponse().reqId(reqId);

        for (int i = 0; i < req.data.count; i++) {
            CmsDataRefEntry refEntry = req.data.items.get(i);
            String ref = str(refEntry.reference);
            if (ref == null) continue;

            String fcCode = null;
            if (refEntry.fcPresent.value()) {
                int fcVal = refEntry.fc.value();
                if (fcVal >= 0 && fcVal < FunctionalConstraint.values().length) {
                    fcCode = FunctionalConstraint.values()[fcVal].name();
                    if ("XX".equals(fcCode)) fcCode = null;
                }
            }

            SclDataValue sv = server.resolveDataValue(ref, templates, fcCode);
            if (sv != null && sv.val != null && !sv.val.isEmpty()) {
                resp.value.add(SclDataConverter.toCmsData(sv));
            } else {
                CmsData err = new CmsData();
                err.choice(CmsData.CHOICE_VISIBLE_STRING);
                err.alt_visible_string.value("(unavailable)");
                resp.value.add(err);
            }
        }
        resp.moreFollows(false);
        log.info("GetDataValues: returning {} values", resp.value.items.size());
        return ok(resp, reqId);
    }
}
