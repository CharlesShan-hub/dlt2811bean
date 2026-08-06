package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.data.CmsDataDefResultEntry;
import com.ysh.jcms.data.sequence.data.CmsDataRefEntry;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionError;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionRequest;
import com.ysh.jcms.pdu.data.CmsGetDataDefinitionResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.convert.DataDefinitionEntry;
import com.ysh.jcms.utils.scl.convert.DataDefinitionResolver;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.navigate.Navigator;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetDataDefinitionServer extends BaseServerHandler {

    public GetDataDefinitionServer() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinitionRequest.class, CmsGetDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetDataDefinitionRequest req = (CmsGetDataDefinitionRequest) rawReq;
        log.info("GetDataDefinition from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.size());

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        CmsGetDataDefinitionResponse resp = new CmsGetDataDefinitionResponse();

        int ps = pageSize();
        log.info(">>>> ps={} resp.data.size={} req.data.size={}", ps, resp.data.size(), req.data.size());
        for (CmsDataRefEntry refEntry : req.data) {
            if (resp.data.size() >= ps)
                break;
            String ref = str(refEntry.reference);
            if (ref == null)
                continue;

            String fcCode = fcCode(refEntry.isPresent("fc") ? refEntry.fc.value() : -1);

            Navigator nav = Navigator.go(doc, ied, ref);
            if (!nav.isValid()) {
                log.debug("skip ref='{}': nav invalid", ref);
                continue;
            }

            DataDefinitionEntry sclEntry = DataDefinitionResolver.resolve(nav, fcCode);
            if (sclEntry != null) {
                CmsDataDefResultEntry result = new CmsDataDefResultEntry().definition(sclEntry.definition());
                if (sclEntry.cdcType() != null && !sclEntry.cdcType().isEmpty())
                    result.cdcType(sclEntry.cdcType());
                resp.data.add(result);
            } else {
                log.debug("SKIP def for ref='{}': resolve returned null", ref);
            }
        }
        resp.moreFollows(false);
        log.info("GetDataDefinition: returning {} definitions", resp.data.size());
        return ok(resp, reqId);
    }
}
