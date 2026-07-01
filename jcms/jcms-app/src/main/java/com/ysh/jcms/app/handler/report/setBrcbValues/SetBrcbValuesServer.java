package com.ysh.jcms.app.handler.report.setBrcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesError;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesRequest;
import com.ysh.jcms.svc.report.CmsSetBrcbValuesResponse;
import com.ysh.jcms.svc.report.CmsSetBrcbEntry;
import com.ysh.jcms.svc.report.CmsSetBrcbResult;
import com.ysh.jcms.utils.scl.model.control.SclReportControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SetBrcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetBrcbValuesServer.class);

    public SetBrcbValuesServer() {
        super(ServiceName.SET_BRCB_VALUES, CmsSetBrcbValuesRequest.class, CmsSetBrcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetBrcbValuesRequest req = (CmsSetBrcbValuesRequest) decoded;
        req.brcb.allocSize = pageSize();
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetBrcbValuesRequest req = (CmsSetBrcbValuesRequest) rawReq;
        int reqId = req.reqId.value();

        log.info("SetBRCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.brcb.count);

        SclServer server = getSclServer(session);
        if (server == null) {
            return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);
        }

        List<CmsSetBrcbResult> results = new ArrayList<>();
        boolean hasError = false;

        for (int i = 0; i < req.brcb.count; i++) {
            CmsSetBrcbEntry entry = req.brcb.items.get(i);
            String ref = new String(entry.reference.value(), StandardCharsets.UTF_8);

            CmsSetBrcbResult result = processEntry(server, entry, ref);
            results.add(result);

            if (result.errorPresent.value()) {
                hasError = true;
            }
        }

        if (hasError) {
            log.warn("SetBRCBValues: {} entries had errors", results.stream().filter(r -> r.errorPresent.value()).count());
            CmsSetBrcbValuesError errResp = new CmsSetBrcbValuesError().reqId(reqId);
            for (CmsSetBrcbResult r : results) {
                errResp.result.add(r);
            }
            try {
                return buildFailure(errResp.encode(), CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT, reqId);
            } catch (Exception e) {
                log.error("Failed to encode SetBRCBValuesError", e);
                return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
            }
        }

        // All entries succeeded
        try {
            CmsSetBrcbValuesResponse resp = new CmsSetBrcbValuesResponse().reqId(reqId);
            return buildSuccess(resp.encode(), reqId);
        } catch (Exception e) {
            log.error("Failed to encode SetBRCBValuesResponse", e);
            return onDecodeError(reqId, CmsServiceError.FAILED_DUE_TO_SERVER_CONSTRAINT);
        }
    }

    private CmsSetBrcbResult processEntry(SclServer server, CmsSetBrcbEntry entry, String ref) {
        CmsSetBrcbResult result = new CmsSetBrcbResult();

        // Resolve the BRCB
        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("SetBRCBValues: invalid ref format {}", ref);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnName = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);

        SclLN ln = server.findLnByRef(ldName + "/" + lnName);
        if (ln == null) {
            log.warn("SetBRCBValues: cannot find LN {} in LD {}", lnName, ldName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        SclReportControl rc = null;
        for (SclReportControl c : ln.getReportControls()) {
            if (c.isBuffered() && c.getName().equals(cbName)) {
                rc = c;
                break;
            }
        }

        if (rc == null) {
            log.warn("SetBRCBValues: cannot find buffered RC {} in LN {}", cbName, lnName);
            result.errorPresent(true).error(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            return result;
        }

        // BRCB found — apply OPTIONAL fields (log only, no runtime state)
        log.info("SetBRCBValues: applying fields to ref={}", ref);

        if (entry.rptIdPresent.value()) {
            String val = new String(entry.rptId.value(), StandardCharsets.UTF_8);
            log.info("  rptID = {}", val);
        }
        if (entry.rptEnaPresent.value()) {
            log.info("  rptEna = {}", entry.rptEna.value());
        }
        if (entry.datSetPresent.value()) {
            String val = new String(entry.datSet.value(), StandardCharsets.UTF_8);
            log.info("  datSet = {}", val);
        }
        if (entry.optFldsPresent.value()) {
            log.info("  optFlds = present");
        }
        if (entry.bufTmPresent.value()) {
            log.info("  bufTm = {}", entry.bufTm.value());
        }
        if (entry.trgOpsPresent.value()) {
            log.info("  trgOps = present");
        }
        if (entry.intgPdPresent.value()) {
            log.info("  intgPd = {}", entry.intgPd.value());
        }
        if (entry.giPresent.value()) {
            log.info("  gi = {}", entry.gi.value());
        }
        if (entry.purgeBufPresent.value()) {
            log.info("  purgeBuf = {}", entry.purgeBuf.value());
        }
        if (entry.entryIdPresent.value()) {
            log.info("  entryID = present");
        }
        if (entry.resvTmsPresent.value()) {
            log.info("  resvTms = {}", entry.resvTms.value());
        }

        // No per-field errors — result stays clean (no errorPresent)
        return result;
    }
}
