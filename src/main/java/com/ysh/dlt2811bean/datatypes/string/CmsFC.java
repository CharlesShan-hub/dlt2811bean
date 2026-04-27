package com.ysh.dlt2811bean.datatypes.string;

import com.ysh.dlt2811bean.per.io.PerInputStream;

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
 * // Construct
 * CmsFC fc = new CmsFC("ST");
 * CmsFC fc = new CmsFC();   // defaults to "ST"
 *
 * // Encode / Decode
 * fc.encode(pos);
 * CmsFC r = new CmsFC().decode(pis);
 *
 * // Validate
 * CmsFC.isValid("ST");   // true
 * CmsFC.isValid("GG");   // false
 * </pre>
 */
public class CmsFC extends CmsVisibleString {

    /** Table 28: all valid functional constraint values. */
    public static final Set<String> VALID_FC = Set.of(
            "ST", "MX", "SP", "SV", "CF", "DC", "SG", "SE", "SR", "OR", "BL", "EX", "XX"
    );

    /** Fixed size per ASN.1 definition. */
    public static final int SIZE = 2;

    public CmsFC() {
        super("FC", "");
        size(SIZE);
        set("ST");
    }

    public CmsFC(String value) {
        super("FC", "");
        size(SIZE);
        set(value);
    }

    @Override
    public CmsFC set(String value) {
        super.set(value);
        if (!isValid(value)) {
            throw new IllegalArgumentException("Invalid FunctionalConstraint: '" + value
                    + "', must be one of: ST, MX, SP, SV, CF, DC, SG, SE, SR, OR, BL, EX, XX");
        }
        return this;
    }

    @Override
    public CmsFC copy() {
        return new CmsFC(get());
    }

    @Override
    public CmsFC decode(PerInputStream pis) throws Exception {
        return (CmsFC) super.decode(pis);
    }

    /** Check if the given string is a valid functional constraint. */
    public static boolean isValid(String fc) {
        if (fc == null || fc.length() != SIZE) {
            return false;
        }
        return VALID_FC.contains(fc);
    }
}
