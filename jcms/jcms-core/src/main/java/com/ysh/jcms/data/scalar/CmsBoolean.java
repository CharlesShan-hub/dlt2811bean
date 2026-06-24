package com.ysh.jcms.data.scalar;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.core.NativeBridge.Codec;

/**
 * typedef struct { int value; } cms_boolean_t;
 * sizeof = 4
 */
public class CmsBoolean extends CmsType {

    private boolean value = false;

    public CmsBoolean() { super(Codec.BOOLEAN);}
    public CmsBoolean(boolean value) { super(Codec.BOOLEAN); this.value = value; write(); }

    public boolean value() { return value; }
    public CmsBoolean value(boolean v) { this.value = v; write(); return this; }

    @Override
    protected int calcNativeSize() { return 4; }

    @Override
    public void write() { nativePtr.setInt(0, value ? 1 : 0); }

    @Override
    public void read() { this.value = nativePtr.getInt(0) != 0; }
}
