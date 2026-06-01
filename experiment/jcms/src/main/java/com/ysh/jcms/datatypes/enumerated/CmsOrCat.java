package com.ysh.jcms.datatypes.enumerated;

public class CmsOrCat extends AbstractCmsEnumerated {

    public CmsOrCat() {
        this(0);
    }

    public CmsOrCat(int value) {
        super("OrCat", value, 9);
    }

    @Override
    public byte[] encode() {
        throw new UnsupportedOperationException("should encode with CmsOriginator");
    }

    public static CmsOrCat decode(byte[] data) {
        throw new UnsupportedOperationException("should decode with CmsOriginator");
    }

    @Override
    public CmsOrCat copy() {
        CmsOrCat clone = new CmsOrCat();
        return copyTo(clone);
    }
}
