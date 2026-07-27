package com.ysh.jcms.data.time;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerBinaryTime;
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

    @Override
    public void syncToInner() {
        ((InnerBinaryTime) inner).value = ByteBuffer.allocate(6)
            .putInt((int) msOfDay.value())
            .putShort((short) daysSince1984.value())
            .array();
    }

    @Override
    public void syncFromInner() {
        ByteBuffer bb = ByteBuffer.wrap(((InnerBinaryTime) inner).value);
        msOfDay.value(bb.getInt() & 0xFFFFFFFFL);
        daysSince1984.value(bb.getShort() & 0xFFFF);
    }
}
