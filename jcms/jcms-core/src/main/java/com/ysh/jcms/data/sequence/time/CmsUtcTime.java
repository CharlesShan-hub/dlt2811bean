package com.ysh.jcms.data.sequence.time;

import com.ysh.jcms.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.util.CmsBytesUtil;
import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerTimeQuality;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.scalar.CmsInt24U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.lang.reflect.Field;
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

        ByteBuffer buf = ByteBuffer.allocate(8);
        CmsBytesUtil.putInt32u(buf, secondsSinceEpoch.value());
        CmsBytesUtil.putInt24(buf, fractionOfSecond.value());
        buf.put((byte) tqValue);
        innerValueBuf(buf.array());
    }

    @Override
    public void syncFromInner() {
        ByteBuffer buf = ByteBuffer.wrap(innerValueBuf());
        secondsSinceEpoch.value(CmsBytesUtil.getInt32u(buf));
        fractionOfSecond.value(CmsBytesUtil.getInt24(buf));

        int tqValue = buf.get() & 0xFF;
        ((InnerTimeQuality) timeQuality.inner).value = tqValue;
        timeQuality.syncFromInner();
    }

    /** Access inner.value (DefaultInnerOctetString) reflectively — works for InnerUtcTime and subtypes like InnerTimeStamp. */
    private DefaultInnerOctetString innerValue() {
        try {
            Field f = inner.getClass().getField("value");
            DefaultInnerOctetString v = (DefaultInnerOctetString) f.get(inner);
            if (v == null) { v = new DefaultInnerOctetString(); f.set(inner, v); }
            return v;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    private byte[] innerValueBuf() {
        DefaultInnerOctetString v = innerValue();
        if (v.value == null || v.value.length < 8) v.value = new byte[8];
        return v.value;
    }

    private void innerValueBuf(byte[] buf) {
        innerValue().value = buf;
    }
}
