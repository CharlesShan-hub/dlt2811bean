package com.ysh.jcms.datatype.block;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.datatype.common.CmsObjectReference;
import com.ysh.jcms.datatype.common.CmsPhyComAddr;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsGoCB extends CmsType {
    public CmsBoolean.ByValue goEna = new CmsBoolean.ByValue();
    public CmsUint8Array.ByValue goID = new CmsUint8Array.ByValue();
    public CmsObjectReference.ByValue datSet = new CmsObjectReference.ByValue();
    public CmsInt32U.ByValue confRev = new CmsInt32U.ByValue();
    public CmsBoolean.ByValue ndsCom = new CmsBoolean.ByValue();
    public CmsPhyComAddr.ByValue dstAddress = new CmsPhyComAddr.ByValue();
    public CmsBoolean.ByValue dstAddress_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("goEna", "goID", "datSet", "confRev", "ndsCom",
                "dstAddress", "dstAddress_present");
    }

    @Override
    protected int encodeBufSize() { return 512; }

    public static class ByValue extends CmsGoCB implements Structure.ByValue {}
}