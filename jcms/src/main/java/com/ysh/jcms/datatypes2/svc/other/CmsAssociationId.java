package com.ysh.jcms.datatypes2.svc.other;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsOctetString;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_association_id_t — OCTET STRING (SIZE(0..64)).
 */
public class CmsAssociationId extends CmsStructure {
    public static final int MAX_LEN = 64;

    public CmsOctetString data = new CmsOctetString(MAX_LEN);
    public int len;

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("data", "len");
    }

    @Override
    protected int ffiEncode(byte[] buf, IntByReference outLen) {
        return CmsFFI.INSTANCE.cms_association_id_encode(this, buf, outLen);
    }

    @Override
    protected void ffiDecode(byte[] data) {
        CmsFFI.INSTANCE.cms_association_id_decode(data, data.length, this);
    }

    @Override
    protected int encodeBufSize() { return 128; }

    public static CmsAssociationId from(byte[] data) { return new CmsAssociationId().decode(data); }
}
