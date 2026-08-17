package com.ysh.jcms.core.data.sequence.common;

import com.ysh.jcms.core.data.core.CmsType;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerBinaryTime;
import com.ysh.jcms.data.V;
import com.ysh.jcms.core.data.scalar.CmsInt16U;
import com.ysh.jcms.core.data.scalar.CmsInt32U;
import java.nio.ByteBuffer;

/**
 * <pre>
 * {@code
 * BinaryTime ::= OCTET STRING (SIZE(6)) — 7.2.2
 * }
 * </pre>
 */
public class CmsBinaryTime extends CmsType {

    public CmsInt32U msOfDay = new CmsInt32U();
    public CmsInt16U daysSince1984 = new CmsInt16U();

    public CmsBinaryTime() {
        super(new InnerBinaryTime());
    }

    public CmsBinaryTime msOfDay(long v) {
        this.msOfDay.value(v);
        return this;
    }
    public CmsBinaryTime daysSince1984(int v) {
        this.daysSince1984.value(v);
        return this;
    }
    public CmsBinaryTime value(CmsBinaryTime v) {
        this.msOfDay.value(v.msOfDay.value());
        this.daysSince1984.value(v.daysSince1984.value());
        return this;
    }

    // ── Domain JSON ──────────────────────────────────────────────────

    @Override
    public Object toJsonValue() {
        java.util.Map<String, Object> map = new java.util.LinkedHashMap<>();
        map.put("msOfDay", msOfDay.value());
        map.put("daysSince1984", daysSince1984.value());
        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void fromJsonValue(Object value) {
        if (!(value instanceof java.util.Map))
            return;
        java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
        if (map.containsKey("msOfDay"))
            msOfDay.value(((Number) map.get("msOfDay")).longValue());
        if (map.containsKey("daysSince1984"))
            daysSince1984.value(((Number) map.get("daysSince1984")).intValue());
    }

    @Override
    public void syncToInner() {
        V.setVal(inner._v, ByteBuffer.allocate(6).putInt((int) msOfDay.value()).putShort((short) daysSince1984.value()).array());
    }

    @Override
    public void syncFromInner() {
        Object raw = V.getVal(inner._v);
        byte[] bytes;
        if (raw instanceof byte[]) {
            bytes = (byte[]) raw;
        } else if (raw instanceof String) {
            bytes = InnerBase.unhex((String) raw);
        } else {
            msOfDay.value(0L);
            daysSince1984.value(0);
            return;
        }
        if (bytes.length < 6) {
            msOfDay.value(0L);
            daysSince1984.value(0);
            return;
        }
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        msOfDay.value(bb.getInt() & 0xFFFFFFFFL);
        daysSince1984.value(bb.getShort() & 0xFFFF);
    }
}
