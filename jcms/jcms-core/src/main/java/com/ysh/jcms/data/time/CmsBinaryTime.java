package com.ysh.jcms.data.time;

import com.ysh.jcms.core.NativeBridge;
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

    public CmsBinaryTime() {
        this.msOfDay = new CmsInt32U();
        this.daysSince1984 = new CmsInt16U();
    }
    
    // -- chain setters --
    public CmsBinaryTime msOfDay(long v) { this.msOfDay.value(v); return this; }
    public CmsBinaryTime daysSince1984(int v) { this.daysSince1984.value(v); return this; }
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(msOfDay, daysSince1984);
    }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeBinaryTime(nativePtr); }
    @Override
    public void decode(byte[] data) { write(); NativeBridge.decodeBinaryTime(nativePtr, data); read(); }
}