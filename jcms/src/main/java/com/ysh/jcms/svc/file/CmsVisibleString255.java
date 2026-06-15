package com.ysh.jcms.svc.file;

import com.sun.jna.Memory;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * VisibleString255 ::= VisibleString (SIZE(0..255))
 * typedef cms_uint8_array_t cms_visible_string255_t;
 *
 * Alias for CmsUint8Array with max size 255.
 */
public class CmsVisibleString255 extends CmsUint8Array {

    public static final int MAX_LEN = 255;

    public CmsVisibleString255() {}

    public CmsVisibleString255(byte[] data) { super(data); }

    public CmsVisibleString255(String s) { super(s); }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeVisibleString255(nativePtr); }

    @Override
    public void decode(byte[] data) {
        this.ownedData = new Memory(MAX_LEN + 1);
        this.value = ownedData;
        this.len = MAX_LEN;
        write();
        NativeBridge.decodeVisibleString255(nativePtr, data);
        read();
    }
}
