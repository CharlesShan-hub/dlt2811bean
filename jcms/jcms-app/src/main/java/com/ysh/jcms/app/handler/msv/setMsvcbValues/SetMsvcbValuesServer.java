package com.ysh.jcms.app.handler.msv.setMsvcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.msv.MsvcbCache;
import com.ysh.jcms.app.handler.msv.getMsvcbValues.GetMsvcbValuesServer;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsMsvcb;
import com.ysh.jcms.svc.msv.CmsSetMsvcbEntry;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesError;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesRequest;
import com.ysh.jcms.svc.msv.CmsSetMsvcbValuesResponse;
import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SetMSVCBValues server handler.
 *
 * <p>
 * Updates the MSV control block configuration in memory. Persistence to SCL is
 * not supported — changes are lost on restart.
 */
public class SetMsvcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetMsvcbValuesServer.class);

    public SetMsvcbValuesServer() {
        super(ServiceName.SET_MSVCB_VALUES, CmsSetMsvcbValuesRequest.class, CmsSetMsvcbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsSetMsvcbValuesRequest req = (CmsSetMsvcbValuesRequest) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsSetMsvcbValuesRequest req = (CmsSetMsvcbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("SetMSVCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.msvcb.count);

        SclDocument doc = getScl2Document(session);

        for (int i = 0; i < req.msvcb.count; i++) {
            CmsSetMsvcbEntry entry = req.msvcb.items.get(i);
            String ref = str(entry.reference);
            log.debug("SetMSVCBValues: processing entry[{}] ref={}", i, ref);

            // Get baseline from cache or SCL
            CmsMsvcb baseline = MsvcbCache.get(ref);
            if (baseline == null && doc != null) {
                baseline = GetMsvcbValuesServer.resolveMsvcb(doc, ref);
            }

            if (baseline == null) {
                log.warn("SetMSVCBValues: MSVCB '{}' not found (SCL), creating empty baseline", ref);
                baseline = new CmsMsvcb();
            }

            // Apply optional fields from the request
            if (entry.svEnaPresent.value()) {
                baseline.svEna(entry.svEna.value());
                log.debug("SetMSVCBValues:   svEna={}", entry.svEna.value());
            }
            if (entry.msvIdPresent.value()) {
                baseline.msvID(entry.msvId.value());
                log.debug("SetMSVCBValues:   msvID={}", str(entry.msvId));
            }
            if (entry.datSetPresent.value()) {
                baseline.datSet(entry.datSet.value());
                log.debug("SetMSVCBValues:   datSet={}", str(entry.datSet));
            }
            if (entry.smpModPresent.value()) {
                baseline.smpMod_present(true);
                baseline.smpMod(entry.smpMod.value());
                log.debug("SetMSVCBValues:   smpMod={}", entry.smpMod.value());
            }
            if (entry.smpRatePresent.value()) {
                baseline.smpRate(entry.smpRate.value());
                log.debug("SetMSVCBValues:   smpRate={}", entry.smpRate.value());
            }
            if (entry.optFldsPresent.value()) {
                baseline.optFlds(entry.optFlds);
                log.debug("SetMSVCBValues:   optFlds updated");
            }

            // Store updated MSVCB in cache
            MsvcbCache.put(ref, baseline);
            log.info("SetMSVCBValues: updated '{}' in cache", ref);
        }

        CmsSetMsvcbValuesResponse resp = new CmsSetMsvcbValuesResponse().reqId(reqId);
        log.info("SetMSVCBValues: acknowledged {} entries", req.msvcb.count);
        return ok(resp, reqId);
    }
}
