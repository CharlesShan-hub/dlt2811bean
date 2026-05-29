package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.CmsFFI;
import com.ysh.jcms.datatypes.type.AbstractCmsScalar;

public class CmsEntryID extends AbstractCmsScalar<byte[]> {

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
    public byte[] encode() {
        byte[] buf = new byte[16];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_EntryID(value, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsEntryID decode(byte[] data) {
        byte[] val = new byte[8];
        CmsFFI.INSTANCE.cms_decode_EntryID(data, data.length, val);
        return new CmsEntryID(val);
    }

    @Override
    public CmsEntryID copy() {
        CmsEntryID clone = new CmsEntryID();
        return copyTo(clone);
    }
}
