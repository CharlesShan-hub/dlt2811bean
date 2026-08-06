package com.ysh.jcms.app.handler.msv;

import com.ysh.jcms.data.sequence.block.CmsMsvcb;
import com.ysh.jcms.utils.scl.model.control.SclSampledValueControl;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.ref.SclRef;
import com.ysh.jcms.utils.scl.ref.SclRefParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared MSVCB resolution logic for GetMSVCBValues and SetMSVCBValues services.
 */
public final class GetMsvcbValuesUtil {

    private static final Logger log = LoggerFactory.getLogger(GetMsvcbValuesUtil.class);

    private GetMsvcbValuesUtil() {
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

        if (!SclRefParser.isValid(ref)) {
            log.warn("resolveMsvcb: invalid ref format '{}'", ref);
            return null;
        }
        SclRef sclRef = SclRefParser.parse(ref);
        String ldName = sclRef.ldInst();
        String lnPart = sclRef.lnName();
        String cbName = sclRef.doName();
        if (cbName == null) {
            log.warn("resolveMsvcb: invalid ref format '{}' (no CB name)", ref);
            return null;
        }
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
