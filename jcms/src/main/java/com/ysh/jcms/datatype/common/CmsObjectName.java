package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsObjectName extends CmsUint8Array {
    public static final int MAX_LEN = 64;

    public CmsObjectName() {
        super(64, true);
    }

    public static class ByValue extends CmsObjectName implements Structure.ByValue {}
}