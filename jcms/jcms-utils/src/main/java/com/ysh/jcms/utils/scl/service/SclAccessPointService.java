package com.ysh.jcms.utils.scl.service;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Access point resolution service —— resolves IED + AccessPoint from sapRef.
 * <p>
 * Used by the Associate service to locate the access point requested by the client when establishing a connection.
 */
public final class SclAccessPointService {

    private static final Logger log = LoggerFactory.getLogger(SclAccessPointService.class);

    /** Resolution result: IED + AccessPoint. */
    public static final class ResolvedAp {
        public final SclIED ied;
        public final SclAccessPoint ap;

        ResolvedAp(SclIED ied, SclAccessPoint ap) {
            this.ied = ied;
            this.ap = ap;
        }
    }

    private SclAccessPointService() {
    }

    /**
     * Resolves an access point by sapRef.
     *
     * @param scl
     *            SCL document
     * @param sapRef
     *            access point reference, format {@code IEDName[/AccessPointName]}, the default AP name is {@code S1}
     * @return resolution result, or {@code null} if the IED or AP does not exist
     */
    public static ResolvedAp resolve(SclDocument scl, String sapRef) {
        if (scl == null || sapRef == null)
            return null;
        int slashIdx = sapRef.indexOf('/');
        String iedName = slashIdx >= 0 ? sapRef.substring(0, slashIdx) : sapRef;
        String apName = slashIdx >= 0 ? sapRef.substring(slashIdx + 1) : "S1";

        SclIED ied = scl.ied(iedName);
        if (ied == null) {
            log.warn("SclAccessPointService: IED '{}' not found", iedName);
            return null;
        }
        SclAccessPoint ap = ied.findAccessPointByName(apName);
        if (ap == null) {
            log.warn("SclAccessPointService: access point '{}' not found on IED '{}'", apName, iedName);
            return null;
        }
        return new ResolvedAp(ied, ap);
    }

    /** Takes the first IED with an access point and its first AP; returns {@code null} if no access point is available. */
    public static ResolvedAp resolveDefault(SclDocument scl) {
        if (scl == null)
            return null;
        for (SclIED ied : scl.ieds()) {
            if (!ied.accessPoints().isEmpty()) {
                return new ResolvedAp(ied, ied.accessPoints().get(0));
            }
        }
        return null;
    }
}
