package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * UtcTime ::= OCTET STRING (SIZE(8))  —  7.2.1
 * PER: 8 bytes aligned (fixed OCTET STRING)
 *
 * Byte layout: [0..3] seconds, [4..6] fraction, [7] time_quality
 *
 * All-pointer container:
 *   [0] seconds_since_epoch → CmsInt32U*
 *   [8] fraction_of_second  → CmsInt24U*
 *   [16] time_quality       → CmsTimeQuality*
 */
public class CmsUtcTime extends CmsType {

    public CmsInt32U seconds_since_epoch;
    public CmsInt24U fraction_of_second;
    public CmsTimeQuality time_quality;

    public CmsUtcTime() {
        this.seconds_since_epoch = new CmsInt32U();
        this.fraction_of_second = new CmsInt24U();
        this.time_quality = new CmsTimeQuality();
    }

    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(seconds_since_epoch, fraction_of_second, time_quality);
    }
}
