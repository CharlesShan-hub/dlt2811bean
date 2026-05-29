package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsOriginator extends AbstractCmsCompound {

    private final int orCat;
    private final byte[] orIdent;

    public CmsOriginator(int orCat, byte[] orIdent) {
        super("Originator");
        this.orCat = orCat;
        this.orIdent = orIdent;
    }

    public int getOrCat() { return orCat; }
    public byte[] getOrIdent() { return orIdent; }

    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_encode_Originator(orCat, orIdent, orIdent.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOriginator decode(byte[] data) {
        IntByReference cat = new IntByReference();
        byte[] identBuf = new byte[256];
        IntByReference identLen = new IntByReference(identBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_decode_Originator(data, data.length, cat, identBuf, identLen);
        byte[] ident = new byte[identLen.getValue()];
        System.arraycopy(identBuf, 0, ident, 0, ident.length);
        return new CmsOriginator(cat.getValue(), ident);
    }

    public CmsOriginator copy() {
        return new CmsOriginator(orCat, orIdent.clone());
    }
}
