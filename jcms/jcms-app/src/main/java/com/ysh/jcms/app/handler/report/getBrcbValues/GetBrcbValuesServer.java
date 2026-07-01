package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesResponse;
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

import java.nio.charset.StandardCharsets;

public class GetBrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetBrcbValuesServer.class);

    public GetBrcbValuesServer() {
        super(ServiceName.GET_BRCB_VALUES, CmsGetBrcbValuesRequest.class, CmsGetBrcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetBrcbValuesRequest req = (CmsGetBrcbValuesRequest) decoded;
        req.reference.allocSize = pageSize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetBrcbValuesRequest req = (CmsGetBrcbValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("GetBRCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        CmsGetBrcbValuesResponse resp = new CmsGetBrcbValuesResponse().reqId(reqId);
        resp.brcb.allocSize = pageSize();

        for (int i = 0; i < req.reference.count; i++) {
            String ref = new String(req.reference.items.get(i).value(), StandardCharsets.UTF_8);
            log.debug("GetBRCBValues: resolving ref={}", ref);

            CmsRcbValueChoice choice = new CmsRcbValueChoice();
            CmsBrcb brcb = resolveBrcb(server, ref);
            if (brcb != null) {
                choice.choice(CmsRcbValueChoice.VALUE);
                choice.altValue = brcb;
            } else {
                log.warn("GetBRCBValues: cannot resolve ref={}", ref);
                choice.choice(CmsRcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.brcb.add(choice);
        }

        resp.moreFollows(false);

        log.info("GetBRCBValues: returning {} entries", resp.brcb.items.size());

        try {
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode GetBRCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    static CmsBrcb resolveBrcb(SclServer server, String ref) {
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) return null;

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) return null;

        // Find the SCL report control
        SclReportControl rc = null;
        for (SclReportControl c : ln.getReportControls()) {
            if (c.isBuffered() && c.getName().equals(cbName)) {
                rc = c;
                break;
            }
        }
        if (rc == null) return null;

        // Build from SCL defaults
        CmsBrcb brcb = new CmsBrcb();

        if (rc.getRptID() != null) brcb.rptID(rc.getRptID());
        if (rc.getDatSet() != null) brcb.datSet(rc.getDatSet());
        if (rc.getConfRev() != null) {
            try { brcb.confRev(Long.parseLong(rc.getConfRev())); } catch (NumberFormatException ignored) {}
        }
        if (rc.getBufTime() != null) {
            try { brcb.bufTm(Long.parseLong(rc.getBufTime())); } catch (NumberFormatException ignored) {}
        }
        if (rc.getIntgPd() != null) {
            try { brcb.intgPd(Long.parseLong(rc.getIntgPd())); } catch (NumberFormatException ignored) {}
        }
        brcb.rptEna(false);
        brcb.sqNum(0);
        brcb.gi(false);
        brcb.purgeBuf(false);
        brcb.entryID(new byte[0]);
        brcb.resvTms_present(false);
        brcb.owner_present(false);

        // Overlay runtime state if present
        CmsBrcb runtime = SclRcbStateManager.get(ref);
        if (runtime != null) {
            // rptID
            if (runtime.rptID != null && runtime.rptID.len > 0) {
                brcb.rptID(runtime.rptID.value());
            }
            // rptEna
            brcb.rptEna(runtime.rptEna.value());
            // datSet
            if (runtime.datSet != null && runtime.datSet.len > 0) {
                brcb.datSet(runtime.datSet.value());
            }
            // optFlds
            if (runtime.optFlds != null) {
                brcb.optFlds = runtime.optFlds;
            }
            // bufTm
            if (runtime.bufTm != null) {
                brcb.bufTm(runtime.bufTm.value());
            }
            // sqNum
            if (runtime.sqNum != null) {
                brcb.sqNum(runtime.sqNum.value());
            }
            // trgOps
            if (runtime.trgOps != null) {
                brcb.trgOps = runtime.trgOps;
            }
            // intgPd
            if (runtime.intgPd != null) {
                brcb.intgPd(runtime.intgPd.value());
            }
            // gi
            brcb.gi(runtime.gi.value());
            // purgeBuf
            brcb.purgeBuf(runtime.purgeBuf.value());
            // entryID
            if (runtime.entryID != null && runtime.entryID.len > 0) {
                brcb.entryID(runtime.entryID.value());
            }
            // timeOfEntry
            if (runtime.timeOfEntry != null) {
                brcb.timeOfEntry = runtime.timeOfEntry;
            }
            // resvTms
            if (runtime.resvTms_present != null && runtime.resvTms_present.value()) {
                brcb.resvTms_present(true);
                brcb.resvTms(runtime.resvTms.value());
            }
            // owner
            if (runtime.owner_present != null && runtime.owner_present.value()) {
                brcb.owner_present(true);
                if (runtime.owner != null && runtime.owner.len > 0) {
                    brcb.owner(runtime.owner.value());
                }
            }
        }

        return brcb;
    }
}
