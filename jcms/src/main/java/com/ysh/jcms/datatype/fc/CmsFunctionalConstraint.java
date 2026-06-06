package com.ysh.jcms.datatype.fc;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsFunctionalConstraint extends CmsUint8Array {
    public static final int LEN = 2;

    public CmsFunctionalConstraint() {
        super(2, true);
    }

    public static class ByValue extends CmsFunctionalConstraint implements Structure.ByValue {}
}