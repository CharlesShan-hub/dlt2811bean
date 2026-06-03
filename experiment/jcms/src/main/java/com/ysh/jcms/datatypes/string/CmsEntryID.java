package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

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
        return CmsFFIDatatypes.INSTANCE.cms_entry_id_encode(value, buf, outLen);
    }

    @Override
    public CmsEntryID copy() {
        return (CmsEntryID) super.copy();
    }

    public static CmsEntryID decode(byte[] data) {
        byte[] val = new byte[8];
        CmsFFIDatatypes.INSTANCE.cms_entry_id_decode(data, data.length, val);
        CmsEntryID eid = new CmsEntryID(val);
        return eid;
    }
}
