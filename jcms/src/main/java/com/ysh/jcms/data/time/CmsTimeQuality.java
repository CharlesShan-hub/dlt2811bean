package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32;
import java.util.Arrays;
import java.util.List;

/**
 * TimeQuality ::= BIT STRING { leap-second-known, clock-failure, clock-not-synchronized } (SIZE(8))
 * PER: fixed 8-bit BIT STRING (align + 1 byte)
 *
 * All-pointer container:
 *   [0] leap_seconds_known      → CmsBoolean*
 *   [8] clock_failure           → CmsBoolean*
 *   [16] clock_not_synchronized → CmsBoolean*
 *   [24] precision              → CmsInt32*  (bits 3-7, 0..31)
 */
public class CmsTimeQuality extends CmsType {

    public CmsBoolean leap_seconds_known;
    public CmsBoolean clock_failure;
    public CmsBoolean clock_not_synchronized;
    public CmsInt32 precision;

    public CmsTimeQuality() {
        this.leap_seconds_known = new CmsBoolean();
        this.clock_failure = new CmsBoolean();
        this.clock_not_synchronized = new CmsBoolean();
        this.precision = new CmsInt32();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(leap_seconds_known, clock_failure, clock_not_synchronized, precision);
    }
}
