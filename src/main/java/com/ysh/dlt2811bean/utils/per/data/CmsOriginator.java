package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

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

    // ==================== orCat constants (ENUMERATED 0..8) ====================

    public static final int NOT_SUPPORTED = 0;
    public static final int BAY_CONTROL = 1;
    public static final int STATION_CONTROL = 2;
    public static final int REMOTE_CONTROL = 3;
    public static final int AUTOMATIC_BAY = 4;
    public static final int AUTOMATIC_STATION = 5;
    public static final int AUTOMATIC_REMOTE = 6;
    public static final int MAINTENANCE = 7;
    public static final int PROCESS = 8;

    /** Maximum orCat value for ENUMERATED encoding. */
    private static final int MAX_OR_CAT = 8;

    @Getter
    private int orCat;

    @Getter
    private byte[] orIdent;

    public CmsOriginator() {
        this.orCat = NOT_SUPPORTED;
        this.orIdent = new byte[0];
    }

    // ==================== orCat semantic setters ====================

    public CmsOriginator setOrCat(int value) {
        if (value < 0 || value > MAX_OR_CAT) {
            throw new IllegalArgumentException("orCat out of range (0..8): " + value);
        }
        this.orCat = value;
        return this;
    }

    public CmsOriginator setOrIdent(byte[] value) {
        this.orIdent = value != null ? value : new byte[0];
        return this;
    }

    // ==================== orCat semantic getters ====================

    /**
     * Check if orCat matches the given value.
     * Use with constants: {@code orig.is(CmsOriginator.BAY_CONTROL)}
     */
    public boolean is(int value) {
        if (value < 0 || value > MAX_OR_CAT) throw new IllegalArgumentException("orCat out of range (0..8): " + value);
        return orCat == value;
    }

    @Override
    public String toString() {
        String catName;
        switch (orCat) {
            case NOT_SUPPORTED: catName = "not-supported"; break;
            case BAY_CONTROL: catName = "bay-control"; break;
            case STATION_CONTROL: catName = "station-control"; break;
            case REMOTE_CONTROL: catName = "remote-control"; break;
            case AUTOMATIC_BAY: catName = "automatic-bay"; break;
            case AUTOMATIC_STATION: catName = "automatic-station"; break;
            case AUTOMATIC_REMOTE: catName = "automatic-remote"; break;
            case MAINTENANCE: catName = "maintenance"; break;
            case PROCESS: catName = "process"; break;
            default: catName = "unknown-" + orCat;
        }
        return String.format("Originator[cat=%d(%s), identLen=%d]", orCat, catName,
                orIdent != null ? orIdent.length : 0);
    }

    /**
     * Encodes Originator as SEQUENCE { orCat ENUMERATED(0..8), orIdent OCTET STRING(0..64) }.
     * <p>In APER, IMPLICIT-tagged mandatory fields are encoded in declaration order.
     */
    public static void encode(PerOutputStream pos, CmsOriginator value) {
        CmsEnumerated.encode(pos, value.orCat, MAX_OR_CAT);
        CmsOctetString.encode(pos, value.orIdent != null ? value.orIdent : new byte[0], Mode.VARIABLE, 64);
    }

    /**
     * Decodes Originator from SEQUENCE { orCat ENUMERATED(0..8), orIdent OCTET STRING(0..64) }.
     */
    public static CmsOriginator decode(PerInputStream pis) throws PerDecodeException {
        CmsOriginator orig = new CmsOriginator();
        orig.orCat = CmsEnumerated.decode(pis, MAX_OR_CAT).getValue();
        orig.orIdent = CmsOctetString.decode(pis, Mode.VARIABLE, 64).getValue();
        return orig;
    }
}
