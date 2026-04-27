package com.ysh.dlt2811bean.service.svc.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;
import com.ysh.dlt2811bean.per.types.PerEnumerated;
import com.ysh.dlt2811bean.service.protocol.types.AbstractCmsI;
import com.ysh.dlt2811bean.service.protocol.types.CmsApdu;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;

/**
 * CMS Service Code 02 — Abort.
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ Reason       ENUMERATED (0..4)                  │  abort reason
 * └─────────────────────────────────────────────────┘
 * </pre>
 *
 * <p>Abort reason enumeration values:
 * <pre>
 * 0 — normal (normal abort)
 * 1 — urgent (urgent abort)
 * 2 — unsup-protocol (unsupported protocol version)
 * 3 — auth-failure (authentication failed)
 * 4 — other (other reason)
 * </pre>
 *
 * <p>Reference: GB/T 45906.3 Table 19 / corresponds to ACSE A-ABORT
 */
@Getter
@Setter
@Accessors(fluent = true)
public class CmsAbort extends AbstractCmsI {

    /** Max enum index for abort reason (0..4, 5 values) */
    static final int REASON_MAX = 4;

    public CmsAbort() {
        super(ServiceCode.ABORT);
    }

    // ==================== Constants ====================

    public static final int REASON_NORMAL = 0;
    public static final int REASON_URGENT = 1;
    public static final int REASON_UNSUP_PROTOCOL = 2;
    public static final int REASON_AUTH_FAILURE = 3;
    public static final int REASON_OTHER = 4;

    // ==================== Fields ====================

    private int reason;

    // ==================== AbstractCmsI Hooks ====================

    @Override
    protected void encodeBody(PerOutputStream pos) {
        PerEnumerated.encode(pos, reason, REASON_MAX);
    }

    @Override
    protected void decodeBody(PerInputStream pis) throws Exception {
        this.reason = PerEnumerated.decode(pis, REASON_MAX);
    }

    // ==================== Static Convenience Methods ====================

    /**
     * Read an Abort APDU from a PER input stream.
     *
     * @param pis PER input stream
     * @return decoded Abort service
     */
    public static CmsAbort read(PerInputStream pis) throws Exception {
        return (CmsAbort) new CmsAbort().decode(pis);
    }

    @Override
    public CmsApdu copy() {
        CmsAbort copy = new CmsAbort();
        copy.reason = this.reason;
        return copy;
    }

    @Override
    public String toString() {
        return "CmsAbort{reason=" + reason + "}";
    }
}
