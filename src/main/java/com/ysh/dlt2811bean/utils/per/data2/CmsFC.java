package com.ysh.dlt2811bean.utils.per.data2;

import com.ysh.dlt2811bean.utils.per.exception.PerDecodeException;
import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;
import lombok.Getter;

import java.util.Set;

/**
 * DL/T 2811 FunctionalConstraint (§7.4).
 *
 * <pre>
 * FunctionalConstraint ::= VisibleString (SIZE(2))
 *
 * Valid values (Table 28): ST, MX, SP, SV, CF, DC, SG, SE, SR, OR, BL, EX, XX
 * </pre>
 *
 * <pre>
 * // Encode
 * CmsFC.encode(pos, "ST");
 * CmsFC.encode(pos, new CmsFC("MX"));
 *
 * // Decode
 * CmsFC fc = CmsFC.decode(pis);
 *
 * // Validate
 * CmsFC.isValid("ST");   // true
 * CmsFC.isValid("GG");   // false
 * </pre>
 */
@Getter
public final class CmsFC {

    /** Table 28: all valid functional constraint values. */
    public static final Set<String> VALID_FC = Set.of(
            "ST", "MX", "SP", "SV", "CF", "DC", "SG", "SE", "SR", "OR", "BL", "EX", "XX"
    );

    /** Fixed size per ASN.1 definition. */
    public static final int SIZE = 2;

    private String value;

    public CmsFC() {
        this.value = "ST";
    }

    public CmsFC(String value) {
        this.value = value;
        validate();
    }

    public CmsFC setValue(String value) {
        this.value = value;
        validate();
        return this;
    }

    // ==================== Validation ====================

    /**
     * Check if the given string is a valid functional constraint.
     */
    public static boolean isValid(String fc) {
        if (fc == null || fc.length() != SIZE) {
            return false;
        }
        return VALID_FC.contains(fc);
    }

    /**
     * Validate this FC value. Throws if invalid.
     */
    public void validate() {
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid FunctionalConstraint: '" + value
                    + "', must be one of: ST, MX, SP, SV, CF, DC, SG, SE, SR, OR, BL, EX, XX");
        }
    }

    // ==================== Encode / Decode ====================

    /**
     * Encode FC as VisibleString SIZE(2).
     */
    public static void encode(PerOutputStream pos, CmsFC fc) {
        if (fc == null) {
            fc = new CmsFC();
        }
        fc.validate();
        CmsVisibleString.encode(pos, fc.value, CmsVisibleString.Mode.FIXED, SIZE);
    }

    /**
     * Encode FC from plain string.
     */
    public static void encode(PerOutputStream pos, String fc) {
        encode(pos, new CmsFC(fc));
    }

    /**
     * Decode FC as VisibleString SIZE(2).
     */
    public static CmsFC decode(PerInputStream pis) throws PerDecodeException {
        String val = CmsVisibleString.decode(pis, CmsVisibleString.Mode.FIXED, SIZE).toString();
        return new CmsFC(val);
    }

    @Override
    public String toString() {
        return value != null ? value : "null";
    }
}
