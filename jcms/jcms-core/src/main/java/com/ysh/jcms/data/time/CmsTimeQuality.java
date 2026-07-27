package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsBits;
import com.ysh.jcms.data.InnerTimeQuality;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * TimeQuality ::= BIT STRING { leap-second-known, clock-failure,
 * clock-not-synchronized } (SIZE(8))
 */
@Getter @Setter @Accessors(chain = true, fluent = true)
public class CmsTimeQuality extends CmsBits {

    @Bit(0) public boolean leap_seconds_known;
    @Bit(1) public boolean clock_failure;
    @Bit(2) public boolean clock_not_synchronized;
    @Bit(value = 3, length = 5) public int precision;

    public CmsTimeQuality() { super(new InnerTimeQuality()); }
}
