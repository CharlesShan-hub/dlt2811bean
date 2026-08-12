package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.utils.scl.service.SclControlBlockService;
import com.ysh.jcms.utils.scl.state.GoCbCache;
import com.ysh.jcms.core.data.sequence.block.CmsGoCb;
import com.ysh.jcms.core.data.sequence.goose.CmsSetGoCbEntry;
import com.ysh.jcms.core.pdu.goose.CmsSetGoCbValuesError;
import com.ysh.jcms.core.pdu.goose.CmsSetGoCbValuesRequest;
import com.ysh.jcms.core.pdu.goose.CmsSetGoCbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.core.info.CmsServiceInfo;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

/**
 * SetGoCBValues server handler.
 *
 * <p>
 * Updates the GOOSE control block configuration in memory. Persistence to SCL
 * is not supported — changes are lost on restart.
 */
public class SetGoCbValuesServer extends BaseServerHandler<CmsSetGoCbValuesRequest, CmsSetGoCbValuesError> {

    public SetGoCbValuesServer() {
        super(CmsServiceInfo.SET_GOCB_VALUES, CmsSetGoCbValuesRequest.class, CmsSetGoCbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsSetGoCbValuesRequest req, int reqId) {
        log.info("SetGoCBValues from {}: reqId={}, {} entries", session.sessionId(), reqId, req.gocb.size());

        SclIED ied = getSclIed(session);
        SclAccessPoint ap = getSclAccessPoint(session);

        for (int i = 0; i < req.gocb.size(); i++) {
            CmsSetGoCbEntry entry = req.gocb.get(i);
            String ref = str(entry.reference);
            log.debug("SetGoCBValues: processing entry[{}] ref={}", i, ref);

            // Get baseline from cache or SCL
            CmsGoCb baseline = GoCbCache.get(ref);
            if (baseline == null && ied != null && ap != null) {
                baseline = SclControlBlockService.resolveGocb(ied, ap, ref);
            }

            if (baseline == null) {
                log.warn("SetGoCBValues: GoCB '{}' not found (SCL), creating empty baseline", ref);
                baseline = new CmsGoCb();
            }

            // Apply optional fields from the request
            if (entry.isPresent("goEna")) {
                baseline.goEna(entry.goEna.value());
                log.debug("SetGoCBValues:   goEna={}", entry.goEna.value());
            }
            if (entry.isPresent("goID")) {
                baseline.goID(entry.goID.value());
                log.debug("SetGoCBValues:   goID={}", str(entry.goID));
            }
            if (entry.isPresent("datSet")) {
                baseline.datSet(entry.datSet.value());
                log.debug("SetGoCBValues:   datSet={}", str(entry.datSet));
            }

            // Store updated GoCB in cache
            GoCbCache.put(ref, baseline);
            log.info("SetGoCBValues: updated '{}' in cache", ref);
        }

        CmsSetGoCbValuesResponse resp = new CmsSetGoCbValuesResponse();
        log.info("SetGoCBValues: acknowledged {} entries", req.gocb.size());
        return ok(resp, reqId);
    }
}
