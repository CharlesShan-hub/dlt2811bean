package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsEntryId extends CmsUint8Array {
    public static final int LEN = 8;
    public static class ByValue extends CmsEntryId implements Structure.ByValue {}

    public CmsEntryId() {
        super(true);
    }
}