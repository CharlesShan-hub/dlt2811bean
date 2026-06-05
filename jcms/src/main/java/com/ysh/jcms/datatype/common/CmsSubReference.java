package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsSubReference extends CmsUint8Array {
    public static final int MAX_LEN = 129;

    public CmsSubReference() {
        super(true);
    }

    public static class ByValue extends CmsSubReference implements Structure.ByValue {}
}