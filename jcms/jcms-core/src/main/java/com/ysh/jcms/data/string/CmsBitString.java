package com.ysh.jcms.data.string;

/**
 * BitString ::= BIT STRING
 *
 * PER encoding: constrained length + align + content (MSB-first packed bits).
 *
 * For BitString, the inherited `len` field stores the number of bits (nbits),
 * NOT bytes. The {@link #value()} getter converts nbits → bytes automatically.
 */
public class CmsBitString extends CmsUint8Array {

    @Override
    protected int defaultBufSize() {
        return 2;
    }

    public CmsBitString() {
    }
    public CmsBitString(byte[] data) {
        super(data);
    }

    @Override
    protected int valueByteLen() {
        return (len + 7) / 8;
    }
}
