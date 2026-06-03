package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;

public class CmsBitString extends AbstractCmsString<CmsBitString, byte[]> {

    public CmsBitString() {
        super("BIT STRING", new byte[0]);
    }

    public CmsBitString(byte[] value) {
        this();
        this.value = value != null ? value : new byte[0];
        this.present = true;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        int nbits = value.length * 8;
        if (isFixed()) {
            return CmsFFIDatatypes.INSTANCE.cms_bit_string_encode(value, nbits, 0, buf, outLen);
        } else if (isVariable()) {
            return CmsFFIDatatypes.INSTANCE.cms_bit_string_encode(value, nbits, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    public static CmsBitString decode(byte[] data, Mode mode, int length) {
        byte[] valBuf = new byte[65536];
        IntByReference valLen = new IntByReference(valBuf.length);
        int nbits = mode == Mode.FIXED ? length : 0;
        int maxNbits = mode == Mode.VARIABLE ? length : 0;
        CmsFFIDatatypes.INSTANCE.cms_bit_string_decode(data, data.length, nbits, maxNbits, valBuf, valLen);
        int len = valLen.getValue();
        if (len >= valBuf.length) len = 0;
        byte[] result = new byte[len];
        System.arraycopy(valBuf, 0, result, 0, len);
        CmsBitString bs = new CmsBitString(result);
        if (mode == Mode.FIXED) bs.size(length);
        else bs.max(length);
        return bs;
    }

    @Override
    public String toString() {
        return String.format("(CmsBitString) [%d bits]", value.length * 8);
    }
}
