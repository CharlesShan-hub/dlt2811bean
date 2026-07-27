package com.ysh.jcms.core;

import com.ysh.jcms.data.InnerBase;

/**
 * Empty InnerBase implementation for CmsType subclasses that do not have
 * a corresponding ASN.1 PDU Inner* type. These classes are field containers
 * used within other PDUs; encode/decode is handled by the parent PDU.
 *
 * encode() and decode() throw UnsupportedOperationException if called directly.
 */
public class InnerEmpty extends InnerBase {
    public byte[] encode() { throw new UnsupportedOperationException("InnerEmpty has no ASN.1 definition"); }
    public byte[] encodeTest() { throw new UnsupportedOperationException("InnerEmpty has no ASN.1 definition"); }
    public static InnerEmpty decode(byte[] data) { return new InnerEmpty(); }
}
