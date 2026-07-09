package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.data.CmsDataDefResultEntry;
import com.ysh.jcms.svc.data.CmsDataRefEntry;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionError;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionRequest;
import com.ysh.jcms.svc.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.convert.DataDefinitionEntry;
import com.ysh.jcms.utils.scl2.convert.DataDefinitionResolver;
import com.ysh.jcms.utils.scl2.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataDefinitionServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDefinitionServer.class);

    public GetDataDefinitionServer() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinitionRequest.class, CmsGetDataDefinitionError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetDataDefinitionRequest req = (CmsGetDataDefinitionRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetDataDefinition from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetDataDefinitionResponse resp = new CmsGetDataDefinitionResponse().reqId(reqId);

        int ps = pageSize();
        log.info(">>>> ps={} resp.data.count={} req.data.count={}", ps, resp.data.count, req.data.count);
        for (int i = 0; i < req.data.count && resp.data.count < ps; i++) {
            CmsDataRefEntry refEntry = req.data.items.get(i);
            String ref = str(refEntry.reference);
            if (ref == null) continue;

            String fcCode = null;
            if (refEntry.fcPresent.value()) {
                int fcVal = refEntry.fc.value();
                if (fcVal >= 0 && fcVal < com.ysh.jcms.info.FunctionalConstraint.values().length) {
                    fcCode = com.ysh.jcms.info.FunctionalConstraint.values()[fcVal].name();
                    if ("XX".equals(fcCode)) fcCode = null;
                }
            }

            Navigator nav = Navigator.go(doc, ref);
            if (!nav.isValid()) continue;

            DataDefinitionEntry sclEntry = DataDefinitionResolver.resolve(nav, fcCode);
            if (sclEntry != null) {
                CmsDataDefResultEntry result = new CmsDataDefResultEntry()
                    .definition(sclEntry.definition());
                if (sclEntry.cdcType() != null && !sclEntry.cdcType().isEmpty())
                    result.cdcType(sclEntry.cdcType());
                resp.data.add(result);
            }
        }
        resp.moreFollows(false);
        log.info("GetDataDefinition: returning {} definitions", resp.data.count);
        return ok(resp, reqId);
    }
}
