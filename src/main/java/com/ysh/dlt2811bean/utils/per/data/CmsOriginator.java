package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerInteger;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import static com.ysh.dlt2811bean.utils.per.data.CmsOctetString.Mode;

/**
 * DL/T 2811 originator type (§7.5.2).
 *
 * <p>ASN.1 definition from DL/T 2811:
 * <pre>
 * Originator ::= SEQUENCE {
 *     orCat   [0] IMPLICIT INTEGER (0..8),
 *     orIdent [1] IMPLICIT OCTET STRING (SIZE(0..64))
 * }
 * </pre>
 *
 * <p>In APER, SEQUENCE members with IMPLICIT tagging are encoded in declaration
 * order without tag numbers — the order itself is implicit.
 * Both fields are mandatory (no OPTIONAL marker).
 *
 * <pre>
 * ┌──────────┬───────────────────┬────────────────────┬───────────┐
 * │ Field    │ ASN.1 Type        │ Constraints        │ Java type │
 * ├──────────┼───────────────────┼────────────────────┼───────────┤
 * │ orCat    │ INTEGER           │ (0..8)             │ int       │
 * │ orIdent  │ OCTET STRING      │ SIZE(0..64)        │ byte[]    │
 * └──────────┴───────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <p>orCat values (OriginatorCategory):
 * <pre>
 * ┌─────┬──────────────────┐
 * │ Val │ Meaning          │
 * ├─────┼──────────────────┤
 * │  0  │ not-supported    │
 * │  1  │ bay-control      │
 * │  2  │ station-control  │
 * │  3  │ remote-control   │
 * │  4  │ automatic-bay    │
 * │  5  │ automatic-station│
 * │  6  │ automatic-remote │
 * │  7  │ maintenance      │
 * │  8  │ process          │
 * └─────┴──────────────────┘
 * </pre>
 *
 * <pre>
 * // Create
 * CmsOriginator orig = new CmsOriginator();
 * orig.setOrCat(CmsOriginator.BAY_CONTROL)
 *     .setOrIdent(new byte[]{0x01, 0x02});
 *
 * // Encode / Decode
 * CmsOriginator.encode(pos, orig);
 * CmsOriginator r = CmsOriginator.decode(pis);
 * </pre>
 */
public final class CmsOriginator {

    // ==================== orCat constants ====================

    public static final int NOT_SUPPORTED = 0;
    public static final int BAY_CONTROL = 1;
    public static final int STATION_CONTROL = 2;
    public static final int REMOTE_CONTROL = 3;
    public static final int AUTOMATIC_BAY = 4;
    public static final int AUTOMATIC_STATION = 5;
    public static final int AUTOMATIC_REMOTE = 6;
    public static final int MAINTENANCE = 7;
    public static final int PROCESS = 8;

    /** Maximum valid orCat value (8). */
    public static final int MAX_OR_CAT = 8;

    @Getter
    @Setter
    @Accessors(chain = true)
    private int orCat;

    @Getter
    @Setter
    @Accessors(chain = true)
    private byte[] orIdent;

    public CmsOriginator() {
        this.orCat = NOT_SUPPORTED;
        this.orIdent = new byte[0];
    }

    /** Returns the symbolic name for known orCat values, or "unknown-" + value. */
    public String getOrCatName() {
        switch (orCat) {
            case NOT_SUPPORTED: return "not-supported";
            case BAY_CONTROL: return "bay-control";
            case STATION_CONTROL: return "station-control";
            case REMOTE_CONTROL: return "remote-control";
            case AUTOMATIC_BAY: return "automatic-bay";
            case AUTOMATIC_STATION: return "automatic-station";
            case AUTOMATIC_REMOTE: return "automatic-remote";
            case MAINTENANCE: return "maintenance";
            case PROCESS: return "process";
            default: return "unknown-" + orCat;
        }
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes Originator as SEQUENCE { orCat INTEGER(0..8), orIdent OCTET STRING(0..64) }.
     * <p>In APER, IMPLICIT-tagged mandatory fields are encoded in declaration order.
     */
    public static void encode(PerOutputStream pos, CmsOriginator value) {
        PerInteger.encode(pos, value.orCat, 0, MAX_OR_CAT);
        CmsOctetString.encode(pos, value.orIdent != null ? value.orIdent : new byte[0], Mode.VARIABLE, 64);
    }

    /**
     * Decodes Originator from SEQUENCE { orCat INTEGER(0..8), orIdent OCTET STRING(0..64) }.
     */
    public static CmsOriginator decode(PerInputStream pis) throws PerDecodeException {
        CmsOriginator orig = new CmsOriginator();
        orig.orCat = (int) PerInteger.decode(pis, 0, MAX_OR_CAT);
        orig.orIdent = CmsOctetString.decode(pis, Mode.VARIABLE, 64).getValue();
        return orig;
    }

    @Override
    public String toString() {
        return String.format("Originator[cat=%d(%s), identLen=%d]", orCat, getOrCatName(),
                orIdent != null ? orIdent.length : 0);
    }
}
