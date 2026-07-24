package com.ysh.jcms.app.handler.msv.getMsvcbValues;

import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.app.handler.msv.MsvcbCache;
import com.ysh.jcms.core.CmsTypeOld;
import com.ysh.jcms.data.block.CmsMsvcb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.msv.CmsGetMsvcbValuesError;
import com.ysh.jcms.svc.msv.CmsGetMsvcbValuesRequest;
import com.ysh.jcms.svc.msv.CmsGetMsvcbValuesResponse;
import com.ysh.jcms.svc.msv.CmsMsvcbValueChoice;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * GetMSVCBValues server handler.
 *
 * <p>
 * Reads MSVCB control block values from SCL (or in-memory cache if previously
 * modified via SetMSVCBValues).
 */
public class GetMsvcbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetMsvcbValuesServer.class);

    public GetMsvcbValuesServer() {
        super(ServiceName.GET_MSVCB_VALUES, CmsGetMsvcbValuesRequest.class, CmsGetMsvcbValuesError.class);
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsTypeOld rawReq, int reqId) {
        CmsGetMsvcbValuesRequest req = (CmsGetMsvcbValuesRequest) rawReq;
        log.info("GetMSVCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclIED ied = requireIed(session, reqId);

        CmsGetMsvcbValuesResponse resp = new CmsGetMsvcbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsMsvcbValueChoice choice = new CmsMsvcbValueChoice();
            CmsMsvcb msvcb = resolveMsvcb(ied, ref);
            if (msvcb != null) {
                choice.choice(CmsMsvcbValueChoice.VALUE);
                choice.altValue = msvcb;
            } else {
                choice.choice(CmsMsvcbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.msvcb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetMSVCBValues: returning {} entries", resp.msvcb.items.size());
        return ok(resp, reqId);
    }

    /**
     * Resolves an MSVCB reference to its current value. Checks in-memory cache
     * first, then falls back to SCL.
     */
    public static CmsMsvcb resolveMsvcb(SclIED ied, String ref) {
        // Check in-memory cache first (written by SetMSVCBValues)
        CmsMsvcb cached = MsvcbCache.get(ref);
        if (cached != null) {
            log.debug("resolveMsvcb: cache hit for '{}'", ref);
            return cached;
        }

        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("resolveMsvcb: invalid ref format '{}'", ref);
            return null;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnPart = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);
        log.debug("resolveMsvcb: ldName={}, lnPart={}, cbName={}", ldName, lnPart, cbName);

        SclLDevice device = findLd(ied, ldName);
        if (device == null) {
            log.warn("resolveMsvcb: LD '{}' not found", ldName);
            return null;
        }

        // Try exact name match first
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclSampledValueControl svc = ln.findSmvControlByName(cbName);
            if (svc != null)
                return buildMsvcb(svc);
            log.warn("resolveMsvcb: SampledValueControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: prefix match (e.g. lnPart="SV" matches LN "SV1")
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                SclSampledValueControl svc = candidate.findSmvControlByName(cbName);
                if (svc != null) {
                    log.debug("resolveMsvcb: found SampledValueControl in LN '{}' (prefix match)", fullName);
                    return buildMsvcb(svc);
                }
            }
        }
        log.warn("resolveMsvcb: SampledValueControl '{}' not found in any LN matching '{}' under LD '{}'", cbName, lnPart, ldName);
        return null;
    }

    private static CmsMsvcb buildMsvcb(SclSampledValueControl svc) {
        CmsMsvcb msvcb = new CmsMsvcb();
        if (svc.svID() != null)
            msvcb.msvID(svc.svID());
        if (svc.datSet() != null)
            msvcb.datSet(svc.datSet());
        if (svc.confRev() != null) {
            try {
                msvcb.confRev(Long.parseLong(svc.confRev()));
            } catch (NumberFormatException ignored) {
            }
        }
        if (svc.smpRate() != null && !svc.smpRate().isEmpty()) {
            try {
                msvcb.smpRate(Integer.parseInt(svc.smpRate()));
            } catch (NumberFormatException ignored) {
            }
        }
        return msvcb;
    }

    private static SclLDevice findLd(SclIED ied, String ldName) {
        return ied.lDevice(ldName);
    }
}
