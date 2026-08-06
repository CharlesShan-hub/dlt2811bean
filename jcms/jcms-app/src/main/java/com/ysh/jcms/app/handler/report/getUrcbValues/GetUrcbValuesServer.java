package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.data.choice.CmsUrcbValueChoice;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.sequence.block.CmsBrcb;
import com.ysh.jcms.data.sequence.block.CmsUrcb;
import com.ysh.jcms.data.enumerate.CmsServiceError;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesError;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesRequest;
import com.ysh.jcms.pdu.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import com.ysh.jcms.utils.transport.session.Session;

public class GetUrcbValuesServer extends BaseServerHandler {

    public GetUrcbValuesServer() {
        super(ServiceName.GET_URCB_VALUES, CmsGetUrcbValuesRequest.class, CmsGetUrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq, int reqId) {
        CmsGetUrcbValuesRequest req = (CmsGetUrcbValuesRequest) rawReq;
        log.info("GetURCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.size());

        SclIED ied = requireIed(session, reqId);

        CmsGetUrcbValuesResponse resp = new CmsGetUrcbValuesResponse();

        for (int i = 0; i < req.reference.size(); i++) {
            String ref = str(req.reference.get(i));
            CmsUrcbValueChoice choice = new CmsUrcbValueChoice();
            CmsUrcb urcb = resolveUrcb(ied, ref);
            if (urcb != null) {
                choice.altValue(urcb);
            } else {
                choice.altError(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.urcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetURCBValues: returning {} entries", resp.urcb.size());
        return ok(resp, reqId);
    }

    static CmsUrcb resolveUrcb(SclIED ied, String ref) {
        if (!SclRefParser.isValid(ref))
            return null;
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnName = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null)
            return null;

        SclLN ln = findLn(ied, ldName, lnName);
        if (ln == null)
            return null;

        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if (!"true".equals(c.buffered()) && c.name().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null)
            return null;

        // Build from SCL defaults
        CmsUrcb urcb = new CmsUrcb();

        if (rc.rptID() != null)
            urcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            urcb.datSet(rc.datSet());
        if (rc.confRev() != null) {
            try {
                urcb.confRev(Long.parseLong(rc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.bufTime() != null) {
            try {
                urcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.intgPd() != null) {
            try {
                urcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        urcb.rptEna(false);
        urcb.sqNum(0);
        urcb.gi(false);
        urcb.setPresent("owner", false);

        // Overlay runtime state if present
        CmsBrcb runtime = RcbStateManager.get(ref);
        if (runtime != null) {
            if (runtime.rptID.value() != null && !runtime.rptID.value().isEmpty()) {
                urcb.rptID(runtime.rptID.value());
            }
            urcb.rptEna(runtime.rptEna.value());
            if (runtime.datSet.value() != null && !runtime.datSet.value().isEmpty()) {
                urcb.datSet(runtime.datSet.value());
            }
            if (runtime.optFlds != null) {
                urcb.optFlds(runtime.optFlds);
            }
            if (runtime.bufTm != null) {
                urcb.bufTm(runtime.bufTm.value());
            }
            if (runtime.sqNum != null) {
                urcb.sqNum(runtime.sqNum.value());
            }
            if (runtime.trgOps != null) {
                urcb.trgOps(runtime.trgOps);
            }
            if (runtime.intgPd != null) {
                urcb.intgPd(runtime.intgPd.value());
            }
            urcb.gi(runtime.gi.value());
            if (runtime.isPresent("owner")) {
                if (runtime.owner.value() != null && runtime.owner.value().length > 0) {
                    urcb.owner(runtime.owner.value());
                }
            }
        }

        return urcb;
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}
