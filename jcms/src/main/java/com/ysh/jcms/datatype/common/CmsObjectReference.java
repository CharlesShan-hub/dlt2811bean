package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsObjectReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;
    public static class ByValue extends CmsObjectReference implements Structure.ByValue {}

    public CmsObjectReference() {
        super(129, true);
    }
}