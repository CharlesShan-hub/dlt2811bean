package com.ysh.jcms.data.time;

import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * UtcTime ::= OCTET STRING (SIZE(8))  —  7.2.1
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
    
    // -- chain setters --
    public CmsUtcTime seconds_since_epoch(long v) { this.seconds_since_epoch.value(v); return this; }
    public CmsUtcTime fraction_of_second(int v) { this.fraction_of_second.value(v); return this; }
    public CmsUtcTime time_quality(CmsTimeQuality v) { this.time_quality = v; return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(seconds_since_epoch, fraction_of_second, time_quality);
    }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeUtcTime(nativePtr); }
    @Override
    public void decode(byte[] data) { write(); NativeBridge.decodeUtcTime(nativePtr, data); read(); }
}