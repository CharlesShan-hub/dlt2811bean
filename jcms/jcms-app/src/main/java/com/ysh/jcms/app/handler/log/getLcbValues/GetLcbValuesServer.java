package com.ysh.jcms.app.handler.log.getLcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.bitarray.CmsLcbOptFlds;
import com.ysh.jcms.data.bitarray.CmsTriggerConditions;
import com.ysh.jcms.data.choice.CmsLcbValueChoice;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.block.CmsLcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.data.scalar.CmsObjectReference;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesError;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesRequest;
import com.ysh.jcms.pdu.log.CmsGetLcbValuesResponse;
import com.ysh.jcms.utils.scl.model.control.SclLogControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

public class GetLcbValuesServer extends BaseServerHandler {


    public GetLcbValuesServer() {
        super(ServiceName.GET_LCB_VALUES, CmsGetLcbValuesRequest.class, CmsGetLcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetLcbValuesRequest req = (CmsGetLcbValuesRequest) rawReq;
        log.info("GetLCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);

        CmsGetLcbValuesResponse resp = new CmsGetLcbValuesResponse();

        for (CmsObjectReference refObj : req.reference) {
            String ref = str(refObj);
            CmsLcbValueChoice choice;
            CmsLcb lcb = resolveLcb(ied, ref);
            if (lcb != null) {
                choice = new CmsLcbValueChoice().altValue(lcb);
            } else {
                choice = new CmsLcbValueChoice().altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.lcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetLCBValues: returning {} entries", resp.lcb.size());
        return ok(resp, reqId);
    }

    static CmsLcb resolveLcb(SclIED ied, String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx)
            return null;

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = findLn(ied, ldName, lnName);
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
                CmsLcbOptFlds f = new CmsLcbOptFlds().bit0(v != 0);
                lcb.optFlds(f);
            } catch (NumberFormatException ignored) {
            }
        }
        if (lc.trgOps() != null) {
            // TriggerConditions requires explicit field-by-field setup from SCL
            // For now, set integrity only if trgOps is present in SCL
            lcb.trgOps(new CmsTriggerConditions().integrity(true));
        }
        return lcb;
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}
