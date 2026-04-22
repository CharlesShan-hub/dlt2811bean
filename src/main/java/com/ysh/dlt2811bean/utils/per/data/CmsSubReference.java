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
 * SubReference is a relative path and must not contain '/' (which belongs to ObjectReference).
 *
 * <p>Constraints enforced by {@link #validate(String)}:
 * <ul>
 *   <li>No '/' character — sub-reference is relative, not a full object reference</li>
 * </ul>
 */
public final class CmsSubReference {

    /** Maximum length per §7.3.3. */
    public static final int MAX_LENGTH = 129;

    private CmsSubReference() {}

    /**
     * Validates a sub-reference string against §7.3.3 constraints.
     *
     * @param value the sub-reference string to validate
     * @throws IllegalArgumentException if '/' is present
     */
    public static void validate(String value) {
        if (value != null && value.indexOf('/') >= 0) {
            throw new IllegalArgumentException(
                    "SubReference must not contain '/': " + value);
        }
    }

    /**
     * Encodes a sub-reference (VisibleString, SIZE(0..129)).
     *
     * @param pos   output stream
     * @param value sub-reference string, max 129 chars
     * @throws IllegalArgumentException if value exceeds 129 characters or violates constraints
     */
    public static void encode(PerOutputStream pos, String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("SubReference exceeds " + MAX_LENGTH + " chars");
        }
        validate(value);
        CmsVisibleString.encode(pos, value != null ? value : "", Mode.VARIABLE, MAX_LENGTH);
    }

    /**
     * Decodes a sub-reference (VisibleString, SIZE(0..129)).
     *
     * @param pis input stream
     * @return sub-reference string, never null
     */
    public static String decode(PerInputStream pis) throws PerDecodeException {
        return CmsVisibleString.decode(pis, Mode.VARIABLE, MAX_LENGTH).getValue();
    }
}
