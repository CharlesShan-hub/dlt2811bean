package com.ysh.jcms.data.scalar;

/**
 * <pre>
 * {@code
 * AssociationId ::= OCTET STRING (SIZE(0..64)) — 8.2.1
 * }
 * </pre>
 */
public class CmsAssociationId extends CmsOctetString {

    public static final int MAX_LEN = 64;

    public CmsAssociationId() {
    }
    public CmsAssociationId(byte[] data) {
        value(data);
    }
    public CmsAssociationId(int value) {
        value(new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value});
    }
    public CmsAssociationId value(int v) {
        return (CmsAssociationId) value(new byte[]{(byte) (v >> 24), (byte) (v >> 16), (byte) (v >> 8), (byte) v});
    }
}
