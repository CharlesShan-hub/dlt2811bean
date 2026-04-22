package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import static com.ysh.dlt2811bean.utils.per.data.CmsOctetString.Mode;

/**
 * DL/T 2811 entry identifier type (§7.3.8).
 *
 * <pre>
 * ┌──────────┬─────────────────┬────────────────────┬───────────┐
 * │ 2811     │ Range           │ Constraints        │ Java type │
 * ├──────────┼─────────────────┼────────────────────┼───────────┤
 * │ EntryID  │ OCTET STRING    │ SIZE(8)            │ byte[8]   │
 * └──────────┴─────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <p>Fixed-size 8-byte opaque identifier. No internal structure defined by the standard.
 */
public final class CmsEntryID {

    /** Fixed size per §7.3.8. */
    public static final int SIZE = 8;

    private CmsEntryID() {}

    /**
     * Encodes an 8-byte entry identifier.
     *
     * @param data exactly 8 bytes
     * @throws IllegalArgumentException if data length is not {@value #SIZE}
     */
    public static void encode(PerOutputStream pos, byte[] data) {
        if (data != null && data.length != SIZE) {
            throw new IllegalArgumentException(
                    "EntryID length must be " + SIZE + ", got " + data.length);
        }
        CmsOctetString.encode(pos, data != null ? data : new byte[SIZE], Mode.FIXED, SIZE);
    }

    public static byte[] decode(PerInputStream pis) throws PerDecodeException {
        return CmsOctetString.decode(pis, Mode.FIXED, SIZE).getValue();
    }
}
