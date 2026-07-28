package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.DefaultInnerOctetString;
import com.ysh.jcms.data.InnerBinaryTime;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;

/**
 * BinaryTime ::= OCTET STRING (SIZE(6)) — 7.2.2
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

    @Override
    public void syncToInner() {
        DefaultInnerOctetString dos = valueField();
        dos.value = ByteBuffer.allocate(6)
            .putInt((int) msOfDay.value())
            .putShort((short) daysSince1984.value())
            .array();
    }

    @Override
    public void syncFromInner() {
        DefaultInnerOctetString dos = valueField();
        if (dos.value == null || dos.value.length < 6) {
            msOfDay.value(0L);
            daysSince1984.value(0);
            return;
        }
        ByteBuffer bb = ByteBuffer.wrap(dos.value);
        msOfDay.value(bb.getInt() & 0xFFFFFFFFL);
        daysSince1984.value(bb.getShort() & 0xFFFF);
    }

    /** Access inner.value field reflectively, works for InnerBinaryTime and subtypes like InnerEntryTime. */
    private DefaultInnerOctetString valueField() {
        try {
            Field f = inner.getClass().getField("value");
            DefaultInnerOctetString v = (DefaultInnerOctetString) f.get(inner);
            if (v == null) {
                v = new DefaultInnerOctetString();
                f.set(inner, v);
            }
            return v;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
