package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;

public class CmsEntryId extends CmsUint8Array {
    public static final int LEN = 8;

    public CmsEntryId() {
        super(8, true);
        value(new byte[LEN]);  // initialize with 8 zero bytes
    }

    @Override
    public CmsEntryId value(byte[] data) {
        return (CmsEntryId) super.value(data);
    }

    /** Set from a 64-bit value as 8-byte big-endian. */
    public CmsEntryId value(long v) {
        byte[] b = new byte[8];
        for (int i = 7; i >= 0; i--) {
            b[i] = (byte) (v & 0xFF);
            v >>= 8;
        }
        return value(b);
    }

    /**
     * Build EntryID from a BinaryTime (6 bytes) + 2 zero bytes.
     * This is the typical format used in BRCB/URCB reports:
     * the entryID is derived from timeOfEntry (BinaryTime) with
     * two trailing zero bytes.
     */
    public CmsEntryId from(CmsBinaryTime bt) {
        byte[] btBytes = bt.encode();
        byte[] b = new byte[8];
        System.arraycopy(btBytes, 0, b, 0, Math.min(btBytes.length, 6));
        return value(b);
    }

    public static class ByValue extends CmsEntryId implements Structure.ByValue {}
}