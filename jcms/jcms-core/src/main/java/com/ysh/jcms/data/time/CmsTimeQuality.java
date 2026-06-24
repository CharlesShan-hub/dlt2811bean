package com.ysh.jcms.data.time;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32;
import java.util.Arrays;
import java.util.List;

/**
 * TimeQuality ::= BIT STRING { leap-second-known, clock-failure, clock-not-synchronized } (SIZE(8))
 * PER: fixed 8-bit BIT STRING (align + 1 byte)
 */
public class CmsTimeQuality extends CmsType {

    public CmsBoolean leap_seconds_known;
    public CmsBoolean clock_failure;
    public CmsBoolean clock_not_synchronized;
    public CmsInt32 precision;

    public CmsTimeQuality() { super(Codec.TIME_QUALITY);
        this.leap_seconds_known = new CmsBoolean();
        this.clock_failure = new CmsBoolean();
        this.clock_not_synchronized = new CmsBoolean();
        this.precision = new CmsInt32();
    }
    
    public CmsTimeQuality leap_seconds_known(boolean v) { this.leap_seconds_known.value(v); return this; }
    public CmsTimeQuality clock_failure(boolean v) { this.clock_failure.value(v); return this; }
    public CmsTimeQuality clock_not_synchronized(boolean v) { this.clock_not_synchronized.value(v); return this; }
    public CmsTimeQuality precision(int v) { this.precision.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(leap_seconds_known, clock_failure, clock_not_synchronized, precision);
    }
}