package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsLcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.log.CmsGetLcbValuesError;
import com.ysh.jcms.svc.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.svc.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.svc.log.CmsLcbValueChoice;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
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
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetLcbValuesRequest req = (CmsGetLcbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetLCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclDocument doc = getScl2Document(session);
        if (doc == null)
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetLcbValuesResponse resp = new CmsGetLcbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsLcbValueChoice choice = new CmsLcbValueChoice();
            CmsLcb lcb = resolveLcb(doc, ref);
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

    static CmsLcb resolveLcb(SclDocument doc, String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return null;

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = findLn(doc, ldName, lnName);
        if (ln == null)
            return null;

        SclLogControl lc = null;
        for (SclLogControl c : ln.logControls()) {
            if (c.name().equals(cbName)) {
                lc = c;
                break;
            }
        }
        if (lc == null)
            return null;

        CmsLcb lcb = new CmsLcb();
        if (lc.logEna() != null)
            lcb.logEna("true".equalsIgnoreCase(lc.logEna()) || "1".equals(lc.logEna()));
        if (lc.datSet() != null)
            lcb.datSet(lc.datSet());
        if (lc.intgPd() != null) {
            try {
                lcb.intgPd(Long.parseLong(lc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (lc.logName() != null)
            lcb.logRef(lc.logName());
        if (lc.optFields() != null) {
            try {
                long v = Long.parseLong(lc.optFields());
                com.ysh.jcms.data.block.CmsLcbOptFlds f = new com.ysh.jcms.data.block.CmsLcbOptFlds().value(v != 0);
                lcb.optFlds_present(true).optFlds(f);
            } catch (NumberFormatException ignored) {
            }
        }
        if (lc.trgOps() != null) {
            // TriggerConditions requires explicit field-by-field setup from SCL
            // For now, set integrity only if trgOps is present in SCL
            lcb.trgOps(new com.ysh.jcms.data.block.CmsTriggerConditions().integrity(true));
        }
        return lcb;
    }

    /** 跨 IED/AccessPoint 查找指定 LD 下的 LN。 */
    private static SclLN findLn(SclDocument doc, String ldName, String lnName) {
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null)
            return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null) {
                    return ld.findLnByFullName(lnName);
                }
            }
        }
        return null;
    }
}
