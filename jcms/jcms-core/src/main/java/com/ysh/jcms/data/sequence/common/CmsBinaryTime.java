package com.ysh.jcms.data.sequence.common;

import com.ysh.jcms.data.core.CmsType;
import com.ysh.jcms.data.InnerBase;
import com.ysh.jcms.data.InnerBinaryTime;
import com.ysh.jcms.data.V;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
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
    public CmsBinaryTime value(CmsBinaryTime v) {
        this.msOfDay.value(v.msOfDay.value());
        this.daysSince1984.value(v.daysSince1984.value());
        return this;
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
