package com.ysh.jcms;

import com.sun.jna.ptr.IntByReference;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public final class CmsOriginator {

    private final int orCat;
    private final byte[] orIdent;

    public CmsOriginator(int orCat, byte[] orIdent) {
        this.orCat = orCat;
        this.orIdent = orIdent;
    }

    public byte[] encode() {
        byte[] buf = new byte[256];
        com.sun.jna.ptr.IntByReference outLen = new com.sun.jna.ptr.IntByReference(buf.length);
        CmsFFI.INSTANCE.cms_encode_Originator(orCat, orIdent, orIdent.length, buf, outLen);
        byte[] result = new byte[outLen.getValue()];
        System.arraycopy(buf, 0, result, 0, result.length);
        return result;
    }

    public static CmsOriginator decode(byte[] data) {
        IntByReference cat = new IntByReference();
        byte[] ident = new byte[64];
        IntByReference identCap = new IntByReference(ident.length);
        CmsFFI.INSTANCE.cms_decode_Originator(data, data.length, cat, ident, identCap);
        byte[] identResult = new byte[identCap.getValue()];
        System.arraycopy(ident, 0, identResult, 0, identResult.length);
        return new CmsOriginator(cat.getValue(), identResult);
    }

    @Override
    public String toString() {
        return "Originator{orCat=" + orCat + ", orIdent=" + bytesToHex(orIdent) + "}";
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
}
