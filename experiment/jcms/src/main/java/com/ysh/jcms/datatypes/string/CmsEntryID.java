package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsEntryID extends AbstractCmsScalar<CmsEntryID, byte[]> {

    public CmsEntryID() {
        super("EntryID", new byte[8]);
    }

    public CmsEntryID(byte[] value) {
        super("EntryID", new byte[8]);
        set(value);
    }

    @Override
    public void set(byte[] value) {
        if (value.length != 8) {
            throw new IllegalArgumentException("EntryID must be exactly 8 bytes");
        }
        super.set(value);
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_entry_id_encode(value, buf, outLen);
    }

    public static CmsEntryID decode(byte[] data) {
        byte[] val = new byte[8];
        CmsFFIDatatypes.INSTANCE.cms_entry_id_decode(data, data.length, val);
        return new CmsEntryID(val);
    }
}
