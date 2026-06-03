package com.ysh.jcms.datatypes.string;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes.type.AbstractCmsString;
import com.ysh.jcms.datatypes.type.CmsFFIDatatypes;
import com.ysh.jcms.per.io.PerInputStream;
import com.ysh.jcms.per.io.PerOutputStream;
import com.ysh.jcms.per.types.PerBitString;

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
    protected int encodeBufSize() {
        return MAX_ENCODE_BUF_SIZE;
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        if (isFixed()) {
            // fixed: (value, nbits, max_nbits=0, buf, outLen)
            return CmsFFIDatatypes.Holder.INSTANCE.cms_bit_string_encode(value, size, 0, buf, outLen);
        } else if (isVariable()) {
            // variable: (value, nbits, max_nbits=ub, buf, outLen)
            return CmsFFIDatatypes.Holder.INSTANCE.cms_bit_string_encode(value, value.length * 8, max, buf, outLen);
        }
        throw new IllegalStateException(typeName + ": size or max must be set before encode");
    }

    @Override
    protected void perEncode(PerOutputStream pos) {
        int nbits = value.length * 8;
        if (isFixed()) {
            PerBitString.encodeFixedSize(pos, value, size);
        } else if (isVariable()) {
            PerBitString.encodeConstrained(pos, value, nbits, 0, max);
        }
    }

    public static CmsBitString decode(byte[] data, Mode mode, int length) {
       if (CmsFFIDatatypes.isAvailable()) {
           byte[] valBuf = new byte[65536];
           IntByReference valLen = new IntByReference(valBuf.length);
           int nbits = mode == Mode.FIXED ? length : 0;
           int maxNbits = mode == Mode.VARIABLE ? length : 0;
           CmsFFIDatatypes.Holder.INSTANCE.cms_bit_string_decode(data, data.length, nbits, maxNbits, valBuf, valLen);
           int len = valLen.getValue();
           if (len >= valBuf.length) len = 0;
           byte[] result = new byte[len];
           System.arraycopy(valBuf, 0, result, 0, len);
           CmsBitString bs = new CmsBitString(result);
           if (mode == Mode.FIXED) bs.size(length);
           else bs.max(length);
           return bs;
       }
        if (mode == Mode.FIXED) {
            return new CmsBitString(PerBitString.decodeFixedSizeBytes(new PerInputStream(data), length));
        }
        return new CmsBitString(PerBitString.decodeConstrained(new PerInputStream(data), 0, length));
    }

    @Override
    public String toString() {
        return String.format("(CmsBitString) [%d bits]", value.length * 8);
    }
}
