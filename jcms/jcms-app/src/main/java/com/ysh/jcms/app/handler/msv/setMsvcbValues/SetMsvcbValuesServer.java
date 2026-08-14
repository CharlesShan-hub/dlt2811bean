package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.base.BaseServerHandler;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.scl.state.CbStateManager;
import com.ysh.jcms.core.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.core.data.sequence.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesRequest;
import com.ysh.jcms.core.pdu.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * SetMSVCBValues server handler.
 *
 * <p>
 * Updates the MSV control block configuration in memory. Persistence to SCL is
 * not supported — changes are lost on restart.
 */
public class SetMsvcbValuesServer extends BaseServerHandler<CmsSetMsvcbValuesRequest, CmsSetMsvcbValuesError> {

    public SetMsvcbValuesServer() {
        super(CmsServiceInfo.SET_MSVCB_VALUES, CmsSetMsvcbValuesRequest.class, CmsSetMsvcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetMsvcbValuesRequest req, int reqId) {
        log.info("SetMSVCBValues from {}: reqId={}, {} entries", session.sessionId(), reqId, req.msvcb.size());

        SclIED ied = getSclIed(session);
        SclAccessPoint ap = getSclAccessPoint(session);

        int idx = 0;
        for (CmsSetMsvcbEntry entry : req.msvcb) {
            String ref = str(entry.reference);
            log.debug("SetMSVCBValues: processing entry[{}] ref={}", idx, ref);
            idx++;

            // Get baseline from cache or SCL
            CmsMsvcb baseline = CbStateManager.MSVCB.get(ref);
            if (baseline == null && ied != null && ap != null) {
                baseline = SclControlBlockService.resolveMsvcb(ied, ap, ref);
            }

            if (baseline == null) {
                log.warn("SetMSVCBValues: MSVCB '{}' not found (SCL), creating empty baseline", ref);
                baseline = new CmsMsvcb();
            }

            // Apply optional fields from the request
            if (entry.isPresent("svEna")) {
                baseline.svEna(entry.svEna.value());
                log.debug("SetMSVCBValues:   svEna={}", entry.svEna.value());
            }
            if (entry.isPresent("msvID")) {
                baseline.msvID(entry.msvID.value());
                log.debug("SetMSVCBValues:   msvID={}", entry.msvID.value());
            }
            if (entry.isPresent("datSet")) {
                baseline.datSet(entry.datSet.value());
                log.debug("SetMSVCBValues:   datSet={}", entry.datSet.value());
            }
            if (entry.isPresent("smpMod")) {
                baseline.smpMod(entry.smpMod.value());
                log.debug("SetMSVCBValues:   smpMod={}", entry.smpMod.value());
            }
            if (entry.isPresent("smpRate")) {
                baseline.smpRate(entry.smpRate.value());
                log.debug("SetMSVCBValues:   smpRate={}", entry.smpRate.value());
            }
            if (entry.isPresent("optFlds")) {
                baseline.optFlds(entry.optFlds);
                log.debug("SetMSVCBValues:   optFlds updated");
            }

            // Store updated MSVCB in cache
            CbStateManager.MSVCB.put(ref, baseline);
            log.info("SetMSVCBValues: updated '{}' in cache", ref);
        }

        CmsSetMsvcbValuesResponse resp = new CmsSetMsvcbValuesResponse();
        log.info("SetMSVCBValues: acknowledged {} entries", req.msvcb.size());
        return ok(resp, reqId);
    }
}
