package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { int value; } cms_boolean_t;
 * sizeof = 4
 */
public class CmsBoolean extends CmsType {

    private boolean value;

    public CmsBoolean() {}
    public CmsBoolean(boolean value) { this.value = value; write(); }

    public boolean value() { return value; }
    public CmsBoolean value(boolean v) { this.value = v; write(); return this; }

    @Override
    protected int calcNativeSize() { return 4; }

    @Override
    public void write() { nativePtr.setInt(0, value ? 1 : 0); }

    @Override
    public void read() { this.value = nativePtr.getInt(0) != 0; }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeBoolean(nativePtr); }

    @Override
    public void decode(byte[] data) { NativeBridge.decodeBoolean(nativePtr, data); read(); }
}
