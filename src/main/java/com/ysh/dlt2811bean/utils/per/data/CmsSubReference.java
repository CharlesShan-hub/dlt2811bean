package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import static com.ysh.dlt2811bean.utils.per.data.CmsVisibleString.Mode;

/**
 * DL/T 2811 sub-reference type (§7.3.3).
 *
 * <pre>
 * ┌──────────┬───────────────────┬────────────────────┬───────────┐
 * │ 2811     │ Range             │ Constraints        │ Java type │
 * ├──────────┼───────────────────┼────────────────────┼───────────┤
 * │ SubRef   │ VisibleString     │ SIZE(0..129)       │ String    │
 * └──────────┴───────────────────┴────────────────────┴───────────┘
 * </pre>
 *
 * <p>Relative reference under a parent node, e.g. "LN.DO.DA.BDA" or "DA.BDA".
 * Encoding is identical to {@link CmsObjectReference} — VisibleString with max 129 chars.
 */
public final class CmsSubReference {

    private static final int MAX_LENGTH = 129;

    private CmsSubReference() {}

    public static void encode(PerOutputStream pos, String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("subReference exceeds " + MAX_LENGTH + " chars");
        }
        CmsVisibleString.encode(pos, value != null ? value : "", Mode.VARIABLE, MAX_LENGTH);
    }

    public static String decode(PerInputStream pis) throws PerDecodeException {
        return CmsVisibleString.decode(pis, Mode.VARIABLE, MAX_LENGTH).getValue();
    }
}
