package com.ysh.jcms.app.handler.data.getDataDefinition;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDataDefinitionServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetDataDefinitionServer.class);

    public GetDataDefinitionServer() {
        super(ServiceName.GET_DATA_DEFINITION, CmsGetDataDefinitionRequest.class, CmsGetDataDefinitionError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetDataDefinitionRequest req = (CmsGetDataDefinitionRequest) rawReq;
        log.info("GetDataDefinition from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.data.count);

        SclDocument doc = requireScl(session, reqId);
        SclIED ied = requireIed(session, reqId);

        CmsGetDataDefinitionResponse resp = new CmsGetDataDefinitionResponse().reqId(reqId);

        int ps = pageSize();
        log.info(">>>> ps={} resp.data.count={} req.data.count={}", ps, resp.data.count, req.data.count);
        for (int i = 0; i < req.data.count && resp.data.count < ps; i++) {
            CmsDataRefEntry refEntry = req.data.items.get(i);
            String ref = str(refEntry.reference);
            if (ref == null)
                continue;

            String fcCode = fcCode(refEntry.fcPresent.value() ? refEntry.fc.value() : -1);

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
        log.info("GetDataDefinition: returning {} definitions", resp.data.items.size());
        return ok(resp, reqId);
    }
}
