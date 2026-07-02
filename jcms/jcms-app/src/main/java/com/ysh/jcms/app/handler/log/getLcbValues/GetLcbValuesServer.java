package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsGetLcbValuesError;
import com.ysh.jcms.svc.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.svc.log.CmsLcbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetLcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetLcbValuesServer.class);

    public GetLcbValuesServer() {
        super(ServiceName.GET_LCB_VALUES, CmsGetLcbValuesRequest.class, CmsGetLcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetLcbValuesRequest req = (CmsGetLcbValuesRequest) decoded;
        req.reference.allocSize = pageSize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLcbValuesRequest req = (CmsGetLcbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetLCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetLcbValuesResponse resp = new CmsGetLcbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsLcbValueChoice choice = new CmsLcbValueChoice();
            CmsLcb lcb = resolveLcb(server, ref);
            if (lcb != null) {
                choice.choice(CmsLcbValueChoice.VALUE);
                choice.altValue = lcb;
            } else {
                choice.choice(CmsLcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.lcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetLCBValues: returning {} entries", resp.lcb.items.size());
        return ok(resp, reqId);
    }

    static CmsLcb resolveLcb(SclServer server, String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) return null;

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) return null;

        SclLogControl lc = null;
        for (SclLogControl c : ln.getLogControls()) {
            if (c.getName().equals(cbName)) {
                lc = c;
                break;
            }
        }
        if (lc == null) return null;

        CmsLcb lcb = new CmsLcb();
        if (lc.getLogEna() != null) lcb.logEna("true".equalsIgnoreCase(lc.getLogEna()) || "1".equals(lc.getLogEna()));
        if (lc.getDatSet() != null) lcb.datSet(lc.getDatSet());
        if (lc.getIntgPd() != null) {
            try { lcb.intgPd(Long.parseLong(lc.getIntgPd())); } catch (NumberFormatException ignored) {}
        }
        if (lc.getLogName() != null) lcb.logRef(lc.getLogName());
        if (lc.getOptFields() != null) {
            try {
                long v = Long.parseLong(lc.getOptFields());
                com.ysh.jcms.data.block.CmsLcbOptFlds f = new com.ysh.jcms.data.block.CmsLcbOptFlds().value(v != 0);
                lcb.optFlds_present(true).optFlds(f);
            } catch (NumberFormatException ignored) {}
        }
        if (lc.getTrgOps() != null) {
            // TriggerConditions requires explicit field-by-field setup from SCL
            // For now, set integrity only if trgOps is present in SCL
            lcb.trgOps(new com.ysh.jcms.data.block.CmsTriggerConditions()
                .integrity(true));
        }
        return lcb;
    }
}
