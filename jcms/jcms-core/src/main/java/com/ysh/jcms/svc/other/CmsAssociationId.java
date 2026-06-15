package com.ysh.jcms.svc.other;

import com.sun.jna.Memory;
import com.ysh.jcms.core.NativeBridge;
import com.ysh.jcms.data.string.CmsUint8Array;

/**
 * AssociationId ::= OCTET STRING (SIZE(0..64))  —  8.2.1
 * typedef cms_uint8_array_t cms_association_id_t;
 */
public class CmsAssociationId extends CmsUint8Array {

    public static final int MAX_LEN = 64;

    public CmsAssociationId() {}

    public CmsAssociationId(byte[] data) { super(data); }
    public CmsAssociationId(int value) {
        super(new byte[]{(byte)(value>>24),(byte)(value>>16),(byte)(value>>8),(byte)value});
    }
    public CmsAssociationId value(int v) {
        return (CmsAssociationId) value(new byte[]{(byte)(v>>24),(byte)(v>>16),(byte)(v>>8),(byte)v});
    }

    @Override
    public byte[] encode() { write(); return NativeBridge.encodeAssociationId(nativePtr); }

    @Override
    public void decode(byte[] data) {
        this.ownedData = new Memory(MAX_LEN);
        this.value = ownedData;
        this.len = 0;
        write();
        NativeBridge.decodeAssociationId(nativePtr, data);
        read();
    }
}
