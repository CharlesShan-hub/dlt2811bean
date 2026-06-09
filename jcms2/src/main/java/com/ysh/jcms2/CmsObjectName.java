package com.ysh.jcms2;

import com.ysh.jcms2.nativebridge.NativeBridge;

/**
 * ObjectName ::= VisibleString (SIZE(0..64))
 *
 * C 侧: cms_object_name_t 就是 cms_uint8_array_t 的别名。
 * FFI: cms_object_name_encode / cms_object_name_decode
 */
public class CmsObjectName extends CmsUint8Array {

    public static final int MAX_LEN = 64;

    public CmsObjectName() {}
    public CmsObjectName(String s) { super(s); }
    public CmsObjectName(byte[] data) { super(data); }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeObjectName(nativePtr); }

    @Override
    public void decode(byte[] data) { NativeBridge.decodeObjectName(nativePtr, data); read(); }
}
