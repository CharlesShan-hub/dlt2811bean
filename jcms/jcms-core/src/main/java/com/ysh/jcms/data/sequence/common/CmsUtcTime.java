package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.util.CmsBytesUtil;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.nio.ByteBuffer;

/**
 * UtcTime ::= OCTET STRING (SIZE(8)) — 7.2.1
 */
public class CmsUtcTime extends CmsType {

    public CmsInt32U secondsSinceEpoch = new CmsInt32U();
    public CmsInt24U fractionOfSecond = new CmsInt24U();
    public CmsTimeQuality timeQuality = new CmsTimeQuality();

    public CmsUtcTime() {
        super(new InnerUtcTime());
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
        this.timeQuality.value(v);
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
        Object tqObj = timeQuality.inner._v.get("_");
        int tqValue = tqObj instanceof Number ? ((Number) tqObj).intValue() : 0;

        ByteBuffer buf = ByteBuffer.allocate(8);
        CmsBytesUtil.putInt32u(buf, secondsSinceEpoch.value());
        CmsBytesUtil.putInt24(buf, fractionOfSecond.value());
        buf.put((byte) tqValue);
        inner._v.put("_", buf.array());
    }

    @Override
    public void syncFromInner() {
        Object raw = inner._v.get("_");
        byte[] bytes;
        if (raw instanceof byte[]) {
            bytes = (byte[]) raw;
        } else if (raw instanceof DefaultInnerOctetString) {
            Object v = ((DefaultInnerOctetString) raw)._v.get("_");
            if (v instanceof byte[]) {
                bytes = (byte[]) v;
            } else if (v instanceof String) {
                bytes = InnerBase.unhex((String) v);
            } else {
                secondsSinceEpoch.value(0L);
                fractionOfSecond.value(0);
                return;
            }
        } else if (raw instanceof String) {
            bytes = InnerBase.unhex((String) raw);
        } else {
            secondsSinceEpoch.value(0L);
            fractionOfSecond.value(0);
            return;
        }
        if (bytes.length < 8) {
            secondsSinceEpoch.value(0L);
            fractionOfSecond.value(0);
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(bytes);
        secondsSinceEpoch.value(CmsBytesUtil.getInt32u(buf));
        fractionOfSecond.value(CmsBytesUtil.getInt24(buf));

        int tqValue = buf.get() & 0xFF;
        timeQuality.inner._v.put("_", tqValue);
        timeQuality.syncFromInner();
    }

    public CmsUtcTime value(CmsUtcTime v) {
        return secondsSinceEpoch(v.secondsSinceEpoch.value())
            .fractionOfSecond(v.fractionOfSecond.value())
            .timeQuality(v.timeQuality);
    }
}
