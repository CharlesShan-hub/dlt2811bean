package com.ysh.jcms.datatypes2.svc.other;

import com.sun.jna.ptr.IntByReference;
import com.ysh.jcms.datatypes2.data.basic.CmsInt32U;
import com.ysh.jcms.datatypes2.data.extended.CmsUtcTime;
import com.ysh.jcms.datatypes2.ffi.CmsFFI;
import com.ysh.jcms.datatypes2.ffi.CmsStructure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_authentication_parameter_t — Authentication Parameter.
 */
public class CmsAuthenticationParameter extends CmsStructure {
    public byte[] cert = new byte[2048];
    public CmsInt32U certLen = new CmsInt32U();
    public CmsUtcTime signedTime = new CmsUtcTime();
    public byte[] sigVal = new byte[2048];
    public CmsInt32U sigLen = new CmsInt32U();

    @Override protected List<String> getFieldOrder() { return Arrays.asList("cert", "certLen", "signedTime", "sigVal", "sigLen"); }
    @Override protected int ffiEncode(byte[] buf, IntByReference outLen) { return CmsFFI.INSTANCE.cms_authentication_parameter_encode(this, buf, outLen); }
    @Override protected void ffiDecode(byte[] data) { CmsFFI.INSTANCE.cms_authentication_parameter_decode(data, data.length, this); }
    @Override protected int encodeBufSize() { return 4096; }
    public static CmsAuthenticationParameter from(byte[] data) { return new CmsAuthenticationParameter().decode(data); }
}
