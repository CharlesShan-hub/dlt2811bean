package com.ysh.jcms.app.handler.report.getUrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesError;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsGetUrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsRcbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclRcbStateManager;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetUrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetUrcbValuesServer.class);

    public GetUrcbValuesServer() {
        super(ServiceName.GET_URCB_VALUES, CmsGetUrcbValuesRequest.class, CmsGetUrcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetUrcbValuesRequest req = (CmsGetUrcbValuesRequest) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetUrcbValuesRequest req = (CmsGetUrcbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetURCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclServer server = getSclServer(session);
        if (server == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetUrcbValuesResponse resp = new CmsGetUrcbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsRcbValueChoice choice = new CmsRcbValueChoice();
            CmsBrcb urcb = resolveUrcb(server, ref);
            if (urcb != null) {
                choice.choice(CmsRcbValueChoice.VALUE);
                choice.altValue = urcb;
            } else {
                choice.choice(CmsRcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.urcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetURCBValues: returning {} entries", resp.urcb.items.size());
        return ok(resp, reqId);
    }

    static CmsBrcb resolveUrcb(SclServer server, String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) return null;

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) return null;

        SclReportControl rc = null;
        for (SclReportControl c : ln.getReportControls()) {
            if (!c.isBuffered() && c.getName().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null) return null;

        // Build from SCL defaults
        CmsBrcb urcb = new CmsBrcb();

        if (rc.getRptID() != null) urcb.rptID(rc.getRptID());
        if (rc.getDatSet() != null) urcb.datSet(rc.getDatSet());
        if (rc.getConfRev() != null) {
            try { urcb.confRev(Long.parseLong(rc.getConfRev())); } catch (NumberFormatException ignored) {}
        }
        if (rc.getBufTime() != null) {
            try { urcb.bufTm(Long.parseLong(rc.getBufTime())); } catch (NumberFormatException ignored) {}
        }
        if (rc.getIntgPd() != null) {
            try { urcb.intgPd(Long.parseLong(rc.getIntgPd())); } catch (NumberFormatException ignored) {}
        }
        urcb.rptEna(false);
        urcb.sqNum(0);
        urcb.gi(false);
        urcb.entryID(new byte[0]);
        urcb.resvTms_present(false);
        urcb.owner_present(false);

        // Overlay runtime state if present
        CmsBrcb runtime = SclRcbStateManager.get(ref);
        if (runtime != null) {
            if (runtime.rptID != null && runtime.rptID.len > 0) {
                urcb.rptID(runtime.rptID.value());
            }
            urcb.rptEna(runtime.rptEna.value());
            if (runtime.datSet != null && runtime.datSet.len > 0) {
                urcb.datSet(runtime.datSet.value());
            }
            if (runtime.optFlds != null) {
                urcb.optFlds = runtime.optFlds;
            }
            if (runtime.bufTm != null) {
                urcb.bufTm(runtime.bufTm.value());
            }
            if (runtime.sqNum != null) {
                urcb.sqNum(runtime.sqNum.value());
            }
            if (runtime.trgOps != null) {
                urcb.trgOps = runtime.trgOps;
            }
            if (runtime.intgPd != null) {
                urcb.intgPd(runtime.intgPd.value());
            }
            urcb.gi(runtime.gi.value());
            if (runtime.entryID != null && runtime.entryID.len > 0) {
                urcb.entryID(runtime.entryID.value());
            }
            if (runtime.timeOfEntry != null) {
                urcb.timeOfEntry = runtime.timeOfEntry;
            }
            if (runtime.resvTms_present != null && runtime.resvTms_present.value()) {
                urcb.resvTms_present(true);
                urcb.resvTms(runtime.resvTms.value());
            }
            if (runtime.owner_present != null && runtime.owner_present.value()) {
                urcb.owner_present(true);
                if (runtime.owner != null && runtime.owner.len > 0) {
                    urcb.owner(runtime.owner.value());
                }
            }
        }

        return urcb;
    }
}
