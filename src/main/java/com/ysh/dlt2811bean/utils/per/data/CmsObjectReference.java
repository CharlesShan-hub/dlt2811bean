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
 * <p>Constraints enforced by {@link #validate(String)}:
 * <ul>
 *   <li>No FC (functional constraint) suffix — e.g. {@code .st$cf} is rejected</li>
 *   <li>No '$' character allowed in the reference</li>
 * </ul>
 *
 * <p>This class provides semantic wrappers around {@link CmsVisibleString}.
 */
public final class CmsObjectReference {

    /** Maximum length per 2811 standard (§7.3.2). */
    public static final int MAX_LENGTH = 129;

    public static void validate(String value) {
        if (value == null) return;

        // (c) no '$'
        if (value.indexOf('$') >= 0) {
            throw new IllegalArgumentException(
                    "ObjectReference must not contain '$': " + value);
        }

        // (b) no FC suffix — check if the part after the last '.' is an FC code
        int dot = value.lastIndexOf('.');
        if (dot >= 0 && dot < value.length() - 1) {
            String lastSegment = value.substring(dot + 1);
            if (CmsFC.isValid(lastSegment) || CmsFC.isValid(lastSegment.toUpperCase())) {
                throw new IllegalArgumentException(
                        "ObjectReference must not contain FC suffix '." + lastSegment.toUpperCase() + "': " + value);
            }
        }
    }

    /**
     * Encodes an object reference (VisibleString, SIZE(0..129)).
     *
     * @param pos   output stream
     * @param value reference string, max 129 chars
     * @throws IllegalArgumentException if value exceeds 129 characters or violates constraints
     */
    public static void encode(PerOutputStream pos, String value) {
        if (value != null && value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ObjectReference exceeds max length " + MAX_LENGTH + ": " + value.length());
        }
        validate(value);
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
