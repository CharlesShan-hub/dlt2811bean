package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import com.ysh.dlt2811bean.utils.per.types.PerVisibleString;
import lombok.Getter;

/**
 * DL/T 2811 ObjectName type (§7.3.1).
 *
 * <pre>
 * ObjectName ::= VisibleString(SIZE(0..64))
 * </pre>
 *
 * <p>Variable-length visible string, max 64 characters.
 * Used to identify objects within a server (e.g. logical device name,
 * logical node name, data object name).
 *
 * <pre>
 * // Construct
 * CmsObjectName name = new CmsObjectName("LD1");
 * CmsObjectName name = new CmsObjectName();   // empty
 *
 * // Encode / Decode
 * CmsObjectName.encode(pos, name);
 * CmsObjectName r = CmsObjectName.decode(pis);
 *
 * // Access
 * r.getValue();  // → "LD1"
 * </pre>
 */
@Getter
public final class CmsObjectName {

    /** Maximum length defined by §7.3.1. */
    public static final int MAX_LENGTH = 64;

    /** The name string (never null). */
    private final String value;

    /** Constructs an empty ObjectName. */
    public CmsObjectName() {
        this.value = "";
    }

    /**
     * Constructs an ObjectName with the given string.
     *
     * @param value name string, max {@value #MAX_LENGTH} characters
     * @throws IllegalArgumentException if value exceeds max length
     */
    public CmsObjectName(String value) {
        if (value == null) value = "";
        if (value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "ObjectName length " + value.length() + " exceeds max " + MAX_LENGTH);
        }
        this.value = value;
    }

    // ==================== Encode / Decode ====================

    /**
     * Encodes an ObjectName into the output stream.
     * Encoding: VisibleString constrained to SIZE(0..64).
     */
    public static void encode(PerOutputStream pos, CmsObjectName name) {
        PerVisibleString.encodeConstrained(pos, name.value, 0, MAX_LENGTH);
    }

    /**
     * Decodes an ObjectName from the input stream.
     *
     * @throws PerDecodeException if the stream is truncated
     * @throws IllegalArgumentException if decoded length exceeds {@value #MAX_LENGTH}
     */
    public static CmsObjectName decode(PerInputStream pis) throws PerDecodeException {
        String s = PerVisibleString.decodeConstrained(pis, 0, MAX_LENGTH);
        return new CmsObjectName(s);
    }

    @Override
    public String toString() {
        return "ObjectName[\"" + value + "\"]";
    }
}
