package com.ysh.jcms.data.bitarray;

import com.ysh.jcms.data.core.CmsBits;
import com.ysh.jcms.data.InnerQuality;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * Quality ::= BIT STRING (SIZE(13)) — 7.3.6
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsQuality extends CmsBits {

    /** Validity values (2-bit field). */
    public static final int GOOD = 0;
    public static final int INVALID = 1;
    public static final int RESERVED = 2;
    public static final int QUESTIONABLE = 3;

    @Bit(value = 0, length = 2) public int validity;
    @Bit(2) public boolean overflow;
    @Bit(3) public boolean outOfRange;
    @Bit(4) public boolean badReference;
    @Bit(5) public boolean oscillatory;
    @Bit(6) public boolean failure;
    @Bit(7) public boolean oldData;
    @Bit(8) public boolean inconsistent;
    @Bit(9) public boolean inaccurate;
    @Bit(10) public boolean substituted;
    @Bit(11) public boolean test;
    @Bit(12) public boolean operatorBlocked;

    public CmsQuality() { super(new InnerQuality()); }
}
