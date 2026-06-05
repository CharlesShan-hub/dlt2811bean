package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsAuthenticationParameter extends CmsType {
    public CmsUint8Array.ByValue cert = new CmsUint8Array.ByValue();
    public long signed_time_ms;
    public CmsUint8Array.ByValue sig_val = new CmsUint8Array.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("cert", "signed_time_ms", "sig_val");
    }

    @Override
    protected int encodeBufSize() { return 4096; }

    public static class ByValue extends CmsAuthenticationParameter implements Structure.ByValue {}
}