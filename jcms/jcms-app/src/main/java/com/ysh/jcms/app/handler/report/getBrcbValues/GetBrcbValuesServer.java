package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsRcbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.state.RcbStateManager;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetBrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetBrcbValuesServer.class);

    public GetBrcbValuesServer() {
        super(ServiceName.GET_BRCB_VALUES, CmsGetBrcbValuesRequest.class, CmsGetBrcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetBrcbValuesRequest req = (CmsGetBrcbValuesRequest) rawReq;
        log.info("GetBRCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclIED ied = requireIed(session, reqId);

        CmsGetBrcbValuesResponse resp = new CmsGetBrcbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsRcbValueChoice choice = new CmsRcbValueChoice();
            CmsBrcb brcb = resolveBrcb(ied, ref);
            if (brcb != null) {
                choice.choice(CmsRcbValueChoice.VALUE);
                choice.altValue = brcb;
            } else {
                choice.choice(CmsRcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.brcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetBRCBValues: returning {} entries", resp.brcb.items.size());
        return ok(resp, reqId);
    }

    /** 按 ref 查找 LD/LN 下的 buffered ReportControl。 */
    static CmsBrcb resolveBrcb(SclIED ied, String ref) {
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

        // Find the SCL report control
        SclReportControl rc = null;
        for (SclReportControl c : ln.reportControls()) {
            if ("true".equals(c.buffered()) && c.name().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null)
            return null;

        // Build from SCL defaults
        CmsBrcb brcb = new CmsBrcb();
        applySclDefaults(brcb, rc);

        // Overlay runtime state if present
        CmsBrcb runtime = RcbStateManager.get(ref);
        if (runtime != null) {
            applyRuntimeState(brcb, runtime);
        }

        return brcb;
    }

    /** Apply SCL template defaults to a fresh CmsBrcb. */
    private static void applySclDefaults(CmsBrcb brcb, SclReportControl rc) {
        if (rc.rptID() != null)
            brcb.rptID(rc.rptID());
        if (rc.datSet() != null)
            brcb.datSet(rc.datSet());
        if (rc.confRev() != null) {
            try {
                brcb.confRev(Long.parseLong(rc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.bufTime() != null) {
            try {
                brcb.bufTm(Long.parseLong(rc.bufTime()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (rc.intgPd() != null) {
            try {
                brcb.intgPd(Long.parseLong(rc.intgPd()));
            } catch (NumberFormatException ignored) {
            }
        }
        brcb.rptEna(false);
        brcb.sqNum(0);
        brcb.gi(false);
        brcb.purgeBuf(false);
        brcb.entryID(new byte[0]);
        brcb.resvTms_present(false);
        brcb.owner_present(false);
    }

    /** Overlay runtime-modified BRCB fields onto the base instance. */
    private static void applyRuntimeState(CmsBrcb brcb, CmsBrcb runtime) {
        if (runtime.rptID != null && runtime.rptID.len > 0)
            brcb.rptID(runtime.rptID.value());
        if (runtime.datSet != null && runtime.datSet.len > 0)
            brcb.datSet(runtime.datSet.value());
        if (runtime.optFlds != null)
            brcb.optFlds = runtime.optFlds;
        if (runtime.trgOps != null)
            brcb.trgOps = runtime.trgOps;
        if (runtime.timeOfEntry != null)
            brcb.timeOfEntry = runtime.timeOfEntry;
        if (runtime.bufTm != null)
            brcb.bufTm(runtime.bufTm.value());
        if (runtime.sqNum != null)
            brcb.sqNum(runtime.sqNum.value());
        if (runtime.intgPd != null)
            brcb.intgPd(runtime.intgPd.value());
        if (runtime.entryID != null && runtime.entryID.len > 0)
            brcb.entryID(runtime.entryID.value());
        brcb.rptEna(runtime.rptEna.value());
        brcb.gi(runtime.gi.value());
        brcb.purgeBuf(runtime.purgeBuf.value());
        if (runtime.resvTms_present != null && runtime.resvTms_present.value()) {
            brcb.resvTms_present(true);
            brcb.resvTms(runtime.resvTms.value());
        }
        if (runtime.owner_present != null && runtime.owner_present.value()) {
            brcb.owner_present(true);
            if (runtime.owner != null && runtime.owner.len > 0)
                brcb.owner(runtime.owner.value());
        }
    }

    private static SclLN findLn(SclIED ied, String ldName, String lnName) {
        SclLDevice ld = ied.lDevice(ldName);
        return ld != null ? ld.findLnByFullName(lnName) : null;
    }
}
