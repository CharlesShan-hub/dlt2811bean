package com.ysh.jcms2;

import com.ysh.jcms2.nativebridge.NativeBridge;

/**
 * typedef struct { int8_t value; } cms_int8_t;
 * sizeof = 1
 * 叶子类型 — 没有 children。
 */
public class CmsInt8 extends CmsType {

    private byte value;

    public CmsInt8() {}
    public CmsInt8(byte value) { this.value = value; write(); }
    public CmsInt8(int value)  { this((byte) value); }

    public byte value() { return value; }
    public CmsInt8 value(byte v) { this.value = v; write(); return this; }
    public CmsInt8 value(int v)  { return value((byte) v); }

    @Override
    protected int calcNativeSize() { return 1; }

    @Override
    public void write() { nativePtr.setByte(0, value); }

    @Override
    public void read() { this.value = nativePtr.getByte(0); }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeInt8(nativePtr); }

    @Override
    public void decode(byte[] data) { NativeBridge.decodeInt8(nativePtr, data); read(); }
}
