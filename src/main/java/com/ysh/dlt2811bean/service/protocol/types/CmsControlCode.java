package com.ysh.dlt2811bean.service.protocol.types;

import com.ysh.dlt2811bean.datatypes.type.AbstractCmsCodedEnum;
import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * APCH Control Code — 8-bit control field.
 * 6.1.1, PICTURE 2
 * <p>Encoded as an 8-bit fixed-size CODED ENUM (BIT STRING).
 * 
 * <p><b>Control Code Structure:</b></p>
 * <pre>
 * ┌──────┬──────┬───────────────────────────────────────┐
 * │ Bits │ Type │ Meaning                               │
 * ├──────┼──────┼───────────────────────────────────────┤
 * │    7 │ flag │ Next (0=last, 1=more fragments)       │
 * │    6 │ flag │ Resp (0=request, 1=response)          │
 * │    5 │ flag │ Err  (0=positive, 1=negative)         │
 * │    4 │ flag │ bak (reserved, shall be 0)            │
 * │  0~3 │ 4bit │ PI (Protocol Identifier, fixed 0x01)  │
 * └──────┴──────┴───────────────────────────────────────┘
 * </pre>
 *
 * <p>Usage:
 * <pre>{@code
 * CmsControlCode cc = new CmsControlCode()
 *     .setNext(true)
 *     .setResp(true)
 *     .setErr(false);
 * PerOutputStream pos = new PerOutputStream();
 * cc.encode(pos);
 *
 * CmsControlCode decoded = new CmsControlCode().decode(pis);
 * boolean isResponse = decoded.isResp();
 * }</pre>
 */
public class CmsControlCode extends AbstractCmsCodedEnum<CmsControlCode> {

    /** Bit 7 — Next fragment indicator. */
    public static final int NEXT = 7;
    /** Bit 6 — Response flag. */
    public static final int RESP = 6;
    /** Bit 5 — Error flag. */
    public static final int ERR = 5;
    /** Bit 4 — Reserved (bak). */
    public static final int BAK = 4;
    /** Bits 0~3 — Protocol Identifier (4-bit). */
    public static final int PI = 0;
    public static final int PI_WIDTH = 4;

    /** Default PI value (DL/T 2811 protocol). */
    public static final int PI_DEFAULT = 0x01;

    public CmsControlCode() {
        super("CmsControlCode", PI_DEFAULT, 8);
    }

    // ==================== High-level Getters/Setters ====================

    public boolean isNext() {
        return testBit(NEXT);
    }

    public CmsControlCode setNext(boolean next) {
        return setBit(NEXT, next);
    }

    public boolean isResp() {
        return testBit(RESP);
    }

    public CmsControlCode setResp(boolean resp) {
        return setBit(RESP, resp);
    }

    public boolean isErr() {
        return testBit(ERR);
    }

    public CmsControlCode setErr(boolean err) {
        return setBit(ERR, err);
    }

    public int getPi() {
        return (int) getBits(PI, PI_WIDTH);
    }

    public CmsControlCode setPi(int pi) {
        if (pi != PI_DEFAULT) {
            throw new IllegalArgumentException("PI must be " + PI_DEFAULT);
        }
        setBits(PI, PI_WIDTH, pi);
        return this;
    }

    // ==================== Static Convenience Methods ====================

    private static final CmsControlCode SHARED = new CmsControlCode();

    public static void write(PerOutputStream pos, boolean next, boolean resp, boolean err) {
        SHARED.setNext(next);
        SHARED.setResp(resp);
        SHARED.setErr(err);
        SHARED.encode(pos);
    }

    public static CmsControlCode read(PerInputStream pis) throws Exception {
        return new CmsControlCode().decode(pis);
    }
}
