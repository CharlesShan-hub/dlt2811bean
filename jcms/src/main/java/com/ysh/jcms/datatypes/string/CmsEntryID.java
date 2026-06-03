package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerOctetString;

public class CmsEntryID extends CmsOctetString {

    public CmsEntryID() {
        super(new byte[8]);
        size(8);
    }

    public CmsEntryID(byte[] value) {
        this();
        if (value.length != 8) {
            throw new IllegalArgumentException("EntryID must be exactly 8 bytes");
        }
        this.value = value;
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.Holder.INSTANCE.cms_entry_id_encode(value, buf, outLen);
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        PerOctetString.encodeFixedSize(pos, value, 8);
    }

    @Override
    public CmsEntryID copy() {
        return (CmsEntryID) super.copy();
    }

    @Override
    protected void ffiDecode(byte[] data) {
        byte[] val = new byte[8];
        CmsFFIDatatypes.Holder.INSTANCE.cms_entry_id_decode(data, data.length, val);
        this.value = val;
        this.present = true;
    }

    @Override
    protected void perDecode(PerInputStream pis) {
        this.value = PerOctetString.decodeFixedSize(pis, 8);
        this.present = true;
    }

    @Override
    public CmsEntryID decode(byte[] data) {
        return (CmsEntryID) super.decode(data);
    }

    public static CmsEntryID from(byte[] data) {
        return new CmsEntryID().decode(data);
    }
}
