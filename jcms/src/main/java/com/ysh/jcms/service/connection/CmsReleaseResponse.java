package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.common.CmsServiceError;
import com.ysh.jcms.ffi.CmsAPDU;
import com.ysh.jcms.service.other.CmsAssociationId;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsReleaseResponse extends CmsAPDU {
    public CmsAssociationId.ByValue assocId = new CmsAssociationId.ByValue();
    public CmsServiceError.ByValue serviceError = new CmsServiceError.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("reqId", "serviceError", "assocId");
    }

    @Override
    protected int encodeBufSize() { return 128; }

    public static class ByValue extends CmsReleaseResponse implements Structure.ByValue {}
}