package com.ysh.jcms.app.handler.report.getBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsBrcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsGetBrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsRcbValueChoice;
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
        // ref format: LD/LN.brcbName
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("resolveBrcb: invalid ref format {}", ref);
            return null;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        log.warn("resolveBrcb: trying findLnByRef({})", ldName + "/" + lnName);
        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) {
            log.warn("resolveBrcb: cannot find LN {} in LD {}, listing available LNs", lnName, ldName);
            return null;
        }

        log.warn("resolveBrcb: found LN, reportControls count={}", ln.getReportControls().size());
        for (SclReportControl rc : ln.getReportControls()) {
            log.warn("resolveBrcb:   rc={} buffered={} isBuffered={}", rc.getName(), rc.getBuffered(), rc.isBuffered());
            if (!rc.isBuffered()) continue;
            if (!rc.getName().equals(cbName)) continue;

            CmsBrcb brcb = new CmsBrcb();

            if (rc.getRptID() != null) brcb.rptID(rc.getRptID());
            brcb.rptEna(false);
            if (rc.getDatSet() != null) brcb.datSet(rc.getDatSet());
            if (rc.getConfRev() != null) {
                try { brcb.confRev(Long.parseLong(rc.getConfRev())); } catch (NumberFormatException ignored) {}
            }
            // optFlds: individual boolean sub-fields, leave at default (all false)
            // bufTm
            if (rc.getBufTime() != null) {
                try { brcb.bufTm(Long.parseLong(rc.getBufTime())); } catch (NumberFormatException ignored) {}
            }
            brcb.sqNum(0);
            // trgOps: individual boolean sub-fields, leave at default (all false)
            if (rc.getIntgPd() != null) {
                try { brcb.intgPd(Long.parseLong(rc.getIntgPd())); } catch (NumberFormatException ignored) {}
            }
            brcb.gi(false);
            brcb.purgeBuf(false);
            brcb.entryID(new byte[0]);
            // timeOfEntry: default (epoch)
            brcb.resvTms_present(false);
            brcb.owner_present(false);

            return brcb;
        }
        return null;
    }
}
