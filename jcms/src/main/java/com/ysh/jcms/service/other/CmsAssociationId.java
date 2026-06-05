package com.ysh.jcms.service.other;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsUint8Array;

public class CmsAssociationId extends CmsUint8Array {
    public static final int MAX = 64;

    public CmsAssociationId() {
        super(true);
    }

    public static class ByValue extends CmsAssociationId implements Structure.ByValue {}
}