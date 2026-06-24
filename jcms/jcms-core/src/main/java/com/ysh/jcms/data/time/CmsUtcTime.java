package com.ysh.jcms.data.time;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * UtcTime ::= OCTET STRING (SIZE(8))  —  7.2.1
 */
public class CmsUtcTime extends CmsType {

    public CmsInt32U secondsSinceEpoch;
    public CmsInt24U fractionOfSecond;
    public CmsTimeQuality timeQuality;

    public CmsUtcTime() { super(Codec.UTC_TIME);
        this.secondsSinceEpoch = new CmsInt32U();
        this.fractionOfSecond = new CmsInt24U();
        this.timeQuality = new CmsTimeQuality();
    }
    
    public CmsUtcTime secondsSinceEpoch(long v) { this.secondsSinceEpoch.value(v); return this; }
    public CmsUtcTime fractionOfSecond(int v) { this.fractionOfSecond.value(v); return this; }
    public CmsUtcTime timeQuality(CmsTimeQuality v) { this.timeQuality = v; return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(secondsSinceEpoch, fractionOfSecond, timeQuality);
    }
}