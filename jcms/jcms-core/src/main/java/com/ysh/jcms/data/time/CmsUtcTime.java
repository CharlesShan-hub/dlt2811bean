package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerTimeQuality;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;

/**
 * UtcTime ::= OCTET STRING (SIZE(8)) — 7.2.1
 */
public class CmsUtcTime extends CmsType {

    public CmsInt32U secondsSinceEpoch;
    public CmsInt24U fractionOfSecond;
    public CmsTimeQuality timeQuality;

    public CmsUtcTime() {
        super(new InnerUtcTime());
        this.secondsSinceEpoch = new CmsInt32U();
        this.fractionOfSecond = new CmsInt24U();
        this.timeQuality = new CmsTimeQuality();
    }

    public CmsUtcTime secondsSinceEpoch(long v) {
        this.secondsSinceEpoch.value(v);
        return this;
    }
    public CmsUtcTime fractionOfSecond(int v) {
        this.fractionOfSecond.value(v);
        return this;
    }
    public CmsUtcTime timeQuality(CmsTimeQuality v) {
        this.timeQuality = v;
        return this;
    }
    public CmsUtcTime now() {
        long millis = System.currentTimeMillis();
        this.secondsSinceEpoch.value(millis / 1000);
        this.fractionOfSecond.value((int) ((millis % 1000) * 1000));
        this.timeQuality.leap_seconds_known(false).clock_failure(false).clock_not_synchronized(false).precision(24);
        return this;
    }

    @Override
    public void syncToInner() {
        timeQuality.syncToInner();
        int tqValue = ((InnerTimeQuality) timeQuality.inner).value;

        long secs = secondsSinceEpoch.value();
        int frac = fractionOfSecond.value();
        ((InnerUtcTime) inner).value = new byte[] {
            (byte) (secs >> 24), (byte) (secs >> 16), (byte) (secs >> 8), (byte) secs,
            (byte) (frac >> 16), (byte) (frac >> 8), (byte) frac,
            (byte) tqValue
        };
    }

    @Override
    public void syncFromInner() {
        byte[] buf = ((InnerUtcTime) inner).value;
        long secs = ((long) (buf[0] & 0xFF) << 24)
                  | ((long) (buf[1] & 0xFF) << 16)
                  | ((long) (buf[2] & 0xFF) << 8)
                  | (long) (buf[3] & 0xFF);
        secondsSinceEpoch.value(secs);

        int frac = ((buf[4] & 0xFF) << 16) | ((buf[5] & 0xFF) << 8) | (buf[6] & 0xFF);
        fractionOfSecond.value(frac);

        int tqValue = buf[7] & 0xFF;
        ((InnerTimeQuality) timeQuality.inner).value = tqValue;
        timeQuality.syncFromInner();
    }
}
