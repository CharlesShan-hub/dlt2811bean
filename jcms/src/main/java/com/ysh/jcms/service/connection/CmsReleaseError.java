package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsReleaseError extends CmsType {
    public CmsServiceError.ByValue serviceError = new CmsServiceError.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("serviceError");
    }

    @Override
    protected int encodeBufSize() { return 128; }

    public static class ByValue extends CmsReleaseError implements Structure.ByValue {}
}