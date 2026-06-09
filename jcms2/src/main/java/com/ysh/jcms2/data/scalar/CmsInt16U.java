package com.ysh.jcms2.data.scalar;

import com.ysh.jcms2.core.CmsType;
import com.ysh.jcms2.core.NativeBridge;

/**
 * typedef struct { uint16_t value; } cms_int16u_t;
 * sizeof = 2
 * value() 返回 int 0..65535。
 */
public class CmsInt16U extends CmsType {

    private short value;  /* Java short 存 uint16_t 的 bit 模式 */

    public CmsInt16U() {}
    public CmsInt16U(int value) { this.value = (short) (value & 0xFFFF); write(); }

    public int value() { return value & 0xFFFF; }
    public CmsInt16U value(int v) { this.value = (short) (v & 0xFFFF); write(); return this; }

    @Override protected int calcNativeSize() { return 2; }
    @Override public void write() { nativePtr.setShort(0, value); }
    @Override public void read() { this.value = nativePtr.getShort(0); }
    @Override public byte[] encode() { write(); return NativeBridge.encodeInt16U(nativePtr); }
    @Override public void decode(byte[] data) { NativeBridge.decodeInt16U(nativePtr, data); read(); }
}
