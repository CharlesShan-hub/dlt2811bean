package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.bitarray.CmsTimeQuality;
import com.ysh.jcms.core.util.CmsBytesUtil;
import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerUtcTime;
import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.scalar.CmsInt24U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import java.nio.ByteBuffer;

/**
 * <pre>
 * {@code
 * UtcTime ::= OCTET STRING (SIZE(8)) — 7.2.1
 * }
 * </pre>
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
    public void rebind() {
        // JER decodes OCTET STRING as {"value": "hex", "length": N} — normalize to {"_": hex}
        if (inner._v.containsKey("value") && !inner._v.containsKey("_")) {
            inner._v.put("_", inner._v.get("value"));
            inner._v.remove("value");
            inner._v.remove("length");
        }
    }

    @Override
    public void syncToInner() {
        timeQuality.syncToInner();
        int tqValue = timeQuality.value();

        ByteBuffer buf = ByteBuffer.allocate(8);
        CmsBytesUtil.putInt32u(buf, secondsSinceEpoch.value());
        CmsBytesUtil.putInt24(buf, fractionOfSecond.value());
        buf.put((byte) tqValue);
        V.setVal(inner._v, buf.array());
    }

    @Override
    public void syncFromInner() {
        Object raw = V.getVal(inner._v);
        // JER decodes OCTET STRING as {"value": "hex", "length": N} — use "value" as fallback
        if (raw == null && inner._v.containsKey("value")) {
            raw = inner._v.get("value");
        }
        byte[] bytes;
        if (raw instanceof byte[]) {
            bytes = (byte[]) raw;
        } else if (raw instanceof DefaultInnerOctetString) {
            Object v = V.getVal(((DefaultInnerOctetString) raw)._v);
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
        V.setVal(timeQuality.inner._v, tqValue);
        timeQuality.syncFromInner();
    }

    public CmsUtcTime value(CmsUtcTime v) {
        return secondsSinceEpoch(v.secondsSinceEpoch.value()).fractionOfSecond(v.fractionOfSecond.value()).timeQuality(v.timeQuality);
    }

    // ── Domain JSON ──────────────────────────────────────────────────

    @Override
    public Object toJsonValue() {
        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("secondsSinceEpoch", secondsSinceEpoch.value());
        map.put("fractionOfSecond", fractionOfSecond.value());
        map.put("timeQuality", timeQuality.toJsonValue());
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fromJsonValue(Object value) {
        if (!(value instanceof java.util.Map))
            return;
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
        if (map.containsKey("secondsSinceEpoch"))
            secondsSinceEpoch.value(((Number) map.get("secondsSinceEpoch")).longValue());
        if (map.containsKey("fractionOfSecond"))
            fractionOfSecond.value(((Number) map.get("fractionOfSecond")).intValue());
        if (map.containsKey("timeQuality"))
            timeQuality.fromJsonValue(map.get("timeQuality"));
    }
}
