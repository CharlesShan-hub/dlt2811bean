package com.ysh.jcms.datatypes.compound;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsCompound;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.List;

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

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList(); // flat FFI — no struct fields
    }

    @Override
    public byte[] encode() {
        syncToNative();
        byte[] buf = new byte[encodeBufSize()];
        IntByReference outLen = new IntByReference(buf.length);
        ffiEncode(buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public CmsOriginator decode(byte[] data) {
        ffiDecode(data);
        syncFromNative();
        return this;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFIDatatypes.INSTANCE.cms_originator_encode(orCat, orIdent, orIdent.length, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        IntByReference cat = new IntByReference();
        byte[] identBuf = new byte[256];
        IntByReference identLen = new IntByReference(identBuf.length);
        CmsFFIDatatypes.INSTANCE.cms_originator_decode(data, data.length, cat, identBuf, identLen);
        this.orCat = cat.getValue();
        this.orIdent = new byte[identLen.getValue()];
        System.arraycopy(identBuf, 0, this.orIdent, 0, this.orIdent.length);
    }

    @Override
    protected int encodeBufSize() {
        return 512;
    }

    public static CmsOriginator from(byte[] data) {
        return new CmsOriginator().decode(data);
    }
}
