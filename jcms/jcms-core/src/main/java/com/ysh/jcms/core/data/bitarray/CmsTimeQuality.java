package com.ysh.jcms.core.data.bitarray;

import com.ysh.jcms.core.data.core.CmsBits;
import com.ysh.jcms.data.InnerTimeQuality;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <pre>
 * {@code
 * TimeQuality ::= BIT STRING {
 *     leap-second-known      (0),
 *     clock-failure          (1),
 *     clock-not-synchronized (2)
 * } (SIZE(8)) — 7.2.1
 * }
 * </pre>
 */
@Getter
@Setter
@Accessors(chain = true, fluent = true)
public class CmsTimeQuality extends CmsBits {

    @Bit(0)
    public boolean leap_seconds_known;
    @Bit(1)
    public boolean clock_failure;
    @Bit(2)
    public boolean clock_not_synchronized;
    @Bit(value = 3, length = 5)
    public int precision;

    public CmsTimeQuality() {
        super(new InnerTimeQuality());
    }
}
