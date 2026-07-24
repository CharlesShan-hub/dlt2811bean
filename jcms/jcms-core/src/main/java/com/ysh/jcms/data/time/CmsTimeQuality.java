package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerTimeQuality;
import com.ysh.jcms.data.scalar.CmsBoolean;
import com.ysh.jcms.data.scalar.CmsInt32;

/**
 * TimeQuality ::= BIT STRING { leap-second-known, clock-failure,
 * clock-not-synchronized } (SIZE(8))
 * <p>
 * CmsTimeQuality stores 4 fields; InnerTimeQuality packs them as a single int.
 */
public class CmsTimeQuality extends CmsType {

    public CmsBoolean leap_seconds_known;
    public CmsBoolean clock_failure;
    public CmsBoolean clock_not_synchronized;
    public CmsInt32 precision;

    public CmsTimeQuality() {
        super(new InnerTimeQuality());
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
    public void syncToInner() {
        int packed = 0;
        if (leap_seconds_known.value()) packed |= 0x01;
        if (clock_failure.value()) packed |= 0x02;
        if (clock_not_synchronized.value()) packed |= 0x04;
        packed |= (precision.value() & 0x1F) << 3;
        ((InnerTimeQuality) inner).value = packed;
    }

    @Override
    public void syncFromInner() {
        int packed = ((InnerTimeQuality) inner).value;
        leap_seconds_known.value((packed & 0x01) != 0);
        clock_failure.value((packed & 0x02) != 0);
        clock_not_synchronized.value((packed & 0x04) != 0);
        precision.value((packed >> 3) & 0x1F);
    }
}
