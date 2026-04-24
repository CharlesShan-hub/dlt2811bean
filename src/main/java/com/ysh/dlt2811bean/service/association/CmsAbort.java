package com.ysh.dlt2811bean.service.association;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerEnumerated;
import com.ysh.dlt2811bean.service.CmsService;

/**
 * CMS Service Code 02 — Abort.
 *
 * <p>ASDU field layout (PER encoded, in order):
 * <pre>
 * ┌─────────────────────────────────────────────────┐
 * │ Reason          ENUMERATED (0..4)               │  abort reason
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
@Accessors(chain = true)
public class CmsAbort extends CmsService {

    /** Max enum index for abort reason (0..4, 5 values) */
    static final int REASON_MAX = 4;

    public CmsAbort() {
        super(2);
    }

    // ==================== Constants ====================

    public static final int REASON_NORMAL = 0;
    public static final int REASON_URGENT = 1;
    public static final int REASON_UNSUP_PROTOCOL = 2;
    public static final int REASON_AUTH_FAILURE = 3;
    public static final int REASON_OTHER = 4;

    // ==================== Fields ====================

    private int reason;

    // ==================== Encode ====================

    @Override
    protected byte[] encodeAsdu() {
        PerOutputStream pos = new PerOutputStream();
        PerEnumerated.encode(pos, reason, REASON_MAX);
        return pos.toByteArray();
    }

    // ==================== Decode ====================

    @Override
    protected void decodeAsdu(PerInputStream pis) throws PerDecodeException {
        this.reason = PerEnumerated.decode(pis, REASON_MAX);
    }

    @Override
    public String toString() {
        return "CmsAbort{reason=" + reason + "}";
    }
}
