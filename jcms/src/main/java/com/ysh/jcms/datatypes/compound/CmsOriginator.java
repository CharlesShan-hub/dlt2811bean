package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class CmsOriginator extends AbstractCmsCompound<CmsOriginator> {

    public int orCat;
    public byte[] orIdent;

    public CmsOriginator() {
        super("Originator");
    }

    public CmsOriginator(int orCat, byte[] orIdent) {
        this();
        this.orCat = orCat;
        this.orIdent = orIdent;
    }

    public byte[] encode() {
        byte[] buf = new byte[512];
        IntByReference outLen = new IntByReference(buf.length);
        CmsFFIDatatypes.INSTANCE.cms_originator_encode(orCat, orIdent, orIdent.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOriginator decode(byte[] data) {
        IntByReference cat = new IntByReference();
        byte[] identBuf = new byte[256];
        IntByReference identLen = new IntByReference(identBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_originator_decode(data, data.length, cat, identBuf, identLen);
        byte[] ident = new byte[identLen.getValue()];
        System.arraycopy(identBuf, 0, ident, 0, ident.length);
        return new CmsOriginator(cat.getValue(), ident);
    }
}
