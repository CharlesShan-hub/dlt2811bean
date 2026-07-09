package com.ysh.jcms.app.handler.goose.getGoCbValues;

import com.ysh.jcms.app.handler.goose.GoCbCache;
import com.ysh.jcms.app.handler.BaseServerHandler;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.block.CmsGoCb;
import com.ysh.jcms.data.common.CmsServiceError;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesError;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesRequest;
import com.ysh.jcms.svc.goose.CmsGetGoCbValuesResponse;
import com.ysh.jcms.svc.goose.CmsGocbValueChoice;
import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.model.control.SclGSEControl;
import com.ysh.jcms.utils.scl2.model.ied.SclLN;
import com.ysh.jcms.utils.scl2.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl2.model.ied.SclServer;
import com.ysh.jcms.utils.scl2.model.ied.SclIED;
import com.ysh.jcms.utils.scl2.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.transport.ServiceName;
import com.ysh.jcms.utils.transport.frame.Frame;
import com.ysh.jcms.utils.transport.session.Session;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetGoCbValuesServer extends BaseServerHandler {

    private static final Logger log = LoggerFactory.getLogger(GetGoCbValuesServer.class);

    public GetGoCbValuesServer() {
        super(ServiceName.GET_GOCB_VALUES, CmsGetGoCbValuesRequest.class, CmsGetGoCbValuesError.class);
    }

    @Override
    protected void prepareDecode(CmsType decoded) {
        CmsGetGoCbValuesRequest req = (CmsGetGoCbValuesRequest) decoded;
    }

    @Override
    protected Frame onDecodeSuccess(Session session, CmsType rawReq) {
        CmsGetGoCbValuesRequest req = (CmsGetGoCbValuesRequest) rawReq;
        int reqId = req.reqId.value();
        log.info("GetGoCBValues from {}: reqId={}, {} refs", session.getSessionId(), reqId, req.reference.count);

        SclDocument doc = getScl2Document(session);
        if (doc == null) return onDecodeError(reqId, CmsServiceError.INSTANCE_NOT_AVAILABLE);

        CmsGetGoCbValuesResponse resp = new CmsGetGoCbValuesResponse().reqId(reqId);

        for (int i = 0; i < req.reference.count; i++) {
            String ref = str(req.reference.items.get(i));
            CmsGocbValueChoice choice = new CmsGocbValueChoice();
            CmsGoCb gocb = resolveGocb(doc, ref);
            if (gocb != null) {
                choice.choice(CmsGocbValueChoice.VALUE);
                choice.altValue = gocb;
            } else {
                choice.choice(CmsGocbValueChoice.ERROR);
                choice.altError.value(CmsServiceError.INSTANCE_NOT_AVAILABLE);
            }
            resp.gocb.add(choice);
        }
        resp.moreFollows(false);
        log.info("GetGoCBValues: returning {} entries", resp.gocb.items.size());
        return ok(resp, reqId);
    }

    public static CmsGoCb resolveGocb(SclDocument doc, String ref) {
        // Check in-memory cache first (written by SetGoCBValues)
        CmsGoCb cached = GoCbCache.get(ref);
        if (cached != null) {
            log.debug("resolveGocb: cache hit for '{}'", ref);
            return cached;
        }

        int slashIdx = ref.indexOf('/');
        int dotIdx = ref.indexOf('.');
        if (slashIdx < 0 || dotIdx < 0 || dotIdx <= slashIdx) {
            log.warn("resolveGocb: invalid ref format '{}'", ref);
            return null;
        }

        String ldName = ref.substring(0, slashIdx);
        String lnPart = ref.substring(slashIdx + 1, dotIdx);
        String cbName = ref.substring(dotIdx + 1);
        log.debug("resolveGocb: ldName={}, lnPart={}, cbName={}", ldName, lnPart, cbName);

        SclLDevice device = findLd(doc, ldName);
        if (device == null) {
            log.warn("resolveGocb: LD '{}' not found", ldName);
            return null;
        }

        // Try findLnByFullName first (exact match on getFullName())
        SclLN ln = device.findLnByFullName(lnPart);
        if (ln != null) {
            SclGSEControl gc = ln.findGseControlByName(cbName);
            if (gc != null) return buildGocb(gc);
            log.warn("resolveGocb: GSEControl '{}' not in LN '{}' (exact match)", cbName, ln.getFullName());
        }

        // Fallback: search all LNs in this LD where getFullName() starts with lnPart
        // (handles cases where lnPart="CTRL" but fullName="CTRL1")
        List<String> candidates = new java.util.ArrayList<>();
        for (SclLN candidate : device.lns()) {
            String fullName = candidate.getFullName();
            if (fullName.startsWith(lnPart)) {
                candidates.add(fullName);
                SclGSEControl gc = candidate.findGseControlByName(cbName);
                if (gc != null) {
                    log.debug("resolveGocb: found GSEControl in LN '{}' (prefix match)", fullName);
                    return buildGocb(gc);
                }
            }
        }
        log.warn("resolveGocb: GSEControl '{}' not found in any LN matching '{}' under LD '{}'. " +
            "Checked LNs: {}, candidate prefix matches: {}",
            cbName, lnPart, ldName,
            device.lns().stream().map(SclLN::getFullName).collect(Collectors.toList()), candidates);
        return null;
    }

    private static CmsGoCb buildGocb(SclGSEControl gc) {
        CmsGoCb gocb = new CmsGoCb();
        if (gc.appID() != null) gocb.goID(gc.appID());
        if (gc.datSet() != null) gocb.datSet(gc.datSet());
        if (gc.confRev() != null) {
            try { gocb.confRev(Long.parseLong(gc.confRev())); } catch (NumberFormatException ignored) {}
        }
        return gocb;
    }

    /** 跨 IED/AccessPoint 查找指定 LD 的 LDevice。 */
    private static SclLDevice findLd(SclDocument doc, String ldName) {
        SclIED ied = doc.findIedByLdInst(ldName);
        if (ied == null) return null;
        for (SclAccessPoint ap : ied.accessPoints()) {
            SclServer srv = ap.server();
            if (srv != null) {
                SclLDevice ld = srv.findLDeviceByInst(ldName);
                if (ld != null) return ld;
            }
        }
        return null;
    }
}
