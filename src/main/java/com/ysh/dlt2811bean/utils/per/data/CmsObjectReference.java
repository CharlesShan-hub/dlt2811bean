package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

import static com.ysh.dlt2811bean.utils.per.data.CmsVisibleString.Mode;

/**
 * DL/T 2811 ObjectReference type (§7.3.2).
 *
 * <p>Object reference is a constrained VisibleString with max length 129,
 * format: {@code LDName/LNName[.Name[....]]}
 *
 * <p>Constraints:
 * <ul>
 *   <li>No FC (functional constraint) in the reference</li>
 *   <li>No '$' character</li>
 *   <li>For non-persistent datasets: {@code @DataSetName} format</li>
 * </ul>
 *
 * <p>This class provides semantic wrappers around {@link CmsVisibleString}.
 */
public final class CmsObjectReference {

    /** Maximum length per 2811 standard (§7.3.2). */
    public static final int MAX_LENGTH = 129;

    private CmsObjectReference() {
        // utility class
    }

    /**
     * Encodes an object reference (VisibleString, SIZE(0..129)).
     *
     * @param pos   output stream
     * @param value reference string, max 129 chars
     * @throws IllegalArgumentException if value exceeds 129 characters
     */
    public static void encode(PerOutputStream pos, String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ObjectReference exceeds max length " + MAX_LENGTH + ": " + value.length());
        }
        CmsVisibleString.encode(pos, value != null ? value : "", Mode.VARIABLE, MAX_LENGTH);
    }

    /**
     * Decodes an object reference (VisibleString, SIZE(0..129)).
     *
     * @param pis input stream
     * @return reference string, never null (empty string if encoded length was 0)
     */
    public static String decode(PerInputStream pis) throws PerDecodeException {
        return CmsVisibleString.decode(pis, Mode.VARIABLE, MAX_LENGTH).getValue();
    }
}
