package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
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
public class CmsAssociateRequest extends CmsType {
    public CmsUint8Array.ByValue sap_ref = new CmsUint8Array.ByValue();
    public CmsBoolean sap_ref_present = new CmsBoolean.ByValue();
    public CmsAuthenticationParameter.ByValue auth_param = new CmsAuthenticationParameter.ByValue();
    public CmsBoolean auth_param_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("sap_ref", "sap_ref_present", "auth_param", "auth_param_present");
    }

    @Override
    protected int encodeBufSize() { return 4096; }

    public static class ByValue extends CmsAssociateRequest implements Structure.ByValue {}
}