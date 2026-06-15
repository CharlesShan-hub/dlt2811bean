package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.scalar.CmsBoolean;
import java.util.Arrays;
import java.util.List;

/**
 * Check ::= BIT STRING (SIZE(2))  —  7.5.3
 * PER: align + 1 byte (2 bits)
 *
 * All-pointer container:
 *   [0] syncheck        → CmsBoolean*
 *   [8] interlock_check → CmsBoolean*
 */
public class CmsCheck extends CmsType {

    public CmsBoolean syncheck;
    public CmsBoolean interlock_check;

    public CmsCheck() {
        this.syncheck        = new CmsBoolean();
        this.interlock_check = new CmsBoolean();
    }
    
    public CmsCheck syncheck(boolean v) { this.syncheck.value(v); return this; }
    public CmsCheck interlock_check(boolean v) { this.interlock_check.value(v); return this; }
    
    @Override
    public List<? extends CmsType> children() {
        return Arrays.asList(syncheck, interlock_check);
    }

    @Override public byte[] encode() { write(); return NativeBridge.encodeCheck(nativePtr); }
    @Override public void decode(byte[] data) { write(); NativeBridge.decodeCheck(nativePtr, data); read(); }
}