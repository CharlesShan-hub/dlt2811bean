package com.ysh.jcms.service.connection;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsAPDU;
import com.ysh.jcms.service.other.CmsAssociationId;
import java.util.Arrays;
import java.util.List;

public class CmsAbort extends CmsAPDU {
    public CmsAssociationId.ByValue assocId = new CmsAssociationId.ByValue();
    public CmsAbortReason.ByValue reason = new CmsAbortReason.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("reqId", "assocId", "reason");
    }

    @Override
    protected int encodeBufSize() { return 128; }

    public static class ByValue extends CmsAbort implements Structure.ByValue {}
}