package com.ysh.jcms2;

import com.ysh.jcms2.nativebridge.NativeBridge;

/**
 * typedef struct { uint8_t value; } cms_int8u_t;
 * sizeof = 1
 * 叶子类型 — 没有 children。
 * Java byte 存 C uint8_t，value() 返回 int 0..255。
 */
public class CmsInt8U extends CmsType {

    private byte value;

    public CmsInt8U() {}
    public CmsInt8U(int value) { this.value = (byte) (value & 0xFF); write(); }

    public int value() { return value & 0xFF; }
    public CmsInt8U value(int v) { this.value = (byte) (v & 0xFF); write(); return this; }

    @Override
    protected int calcNativeSize() { return 1; }

    @Override
    public void write() { nativePtr.setByte(0, value); }

    @Override
    public void read() { this.value = nativePtr.getByte(0); }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeInt8U(nativePtr); }

    @Override
    public void decode(byte[] data) { NativeBridge.decodeInt8U(nativePtr, data); read(); }
}
