package com.ysh.jcms.app.handler.goose.setGoCbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.goose.GoCbCache;
import com.ysh.jcms.app.handler.goose.getGoCbValues.GetGoCbValuesServer;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.sequence.block.CmsGoCb;
import com.ysh.jcms.pdu.goose.CmsSetGoCbEntry;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesError;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesRequest;
import com.ysh.jcms.pdu.goose.CmsSetGoCbValuesResponse;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SetGoCBValues server handler.
 *
 * <p>
 * Updates the GOOSE control block configuration in memory. Persistence to SCL
 * is not supported — changes are lost on restart.
 */
public class SetGoCbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(SetGoCbValuesServer.class);

    public SetGoCbValuesServer() {
        super(ServiceName.SET_GOCB_VALUES, CmsSetGoCbValuesRequest.class, CmsSetGoCbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsSetGoCbValuesRequest req = (CmsSetGoCbValuesRequest) rawReq;
        log.info("SetGoCBValues from {}: reqId={}, {} entries", session.getSessionId(), reqId, req.gocb.count);

        SclIED ied = getSclIed(session);

        for (int i = 0; i < req.gocb.count; i++) {
            CmsSetGoCbEntry entry = req.gocb.items.get(i);
            String ref = str(entry.reference);
            log.debug("SetGoCBValues: processing entry[{}] ref={}", i, ref);

            // Get baseline from cache or SCL
            CmsGoCb baseline = GoCbCache.get(ref);
            if (baseline == null && ied != null) {
                baseline = GetGoCbValuesServer.resolveGocb(ied, ref);
            }

            if (baseline == null) {
                log.warn("SetGoCBValues: GoCB '{}' not found (SCL), creating empty baseline", ref);
                baseline = new CmsGoCb();
            }

            // Apply optional fields from the request
            if (entry.goEnaPresent.value()) {
                baseline.goEna(entry.goEna.value());
                log.debug("SetGoCBValues:   goEna={}", entry.goEna.value());
            }
            if (entry.goIdPresent.value()) {
                baseline.goID(entry.goId.value());
                log.debug("SetGoCBValues:   goID={}", str(entry.goId));
            }
            if (entry.datSetPresent.value()) {
                baseline.datSet(entry.datSet.value());
                log.debug("SetGoCBValues:   datSet={}", str(entry.datSet));
            }

            // Store updated GoCB in cache
            GoCbCache.put(ref, baseline);
            log.info("SetGoCBValues: updated '{}' in cache", ref);
        }

        CmsSetGoCbValuesResponse resp = new CmsSetGoCbValuesResponse().reqId(reqId);
        log.info("SetGoCBValues: acknowledged {} entries", req.gocb.count);
        return ok(resp, reqId);
    }
}
