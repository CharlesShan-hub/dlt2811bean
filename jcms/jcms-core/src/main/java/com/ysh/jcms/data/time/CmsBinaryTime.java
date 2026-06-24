package com.ysh.jcms.data.time;

import com.ysh.jcms.core.NativeBridge.Codec;
import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.scalar.CmsInt16U;
import com.ysh.jcms.data.scalar.CmsInt32U;
import java.util.Arrays;
import java.util.List;

/**
 * BinaryTime ::= OCTET STRING (SIZE(6))  —  7.2.2
 */
public class CmsBinaryTime extends CmsType {

    public CmsInt32U msOfDay;
    public CmsInt16U daysSince1984;

    public CmsBinaryTime() { super(Codec.BINARY_TIME);
        this.msOfDay = new CmsInt32U();
        this.daysSince1984 = new CmsInt16U();
    }
    
    public CmsBinaryTime msOfDay(long v) { this.msOfDay.value(v); return this; }
    public CmsBinaryTime daysSince1984(int v) { this.daysSince1984.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(msOfDay, daysSince1984);
    }
}