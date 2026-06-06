package com.ysh.jcms.datatype.block;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.datatype.common.CmsObjectReference;
import com.ysh.jcms.datatype.common.CmsPhyComAddr;
import com.ysh.jcms.ffi.CmsType;
import lombok.Getter;
import lombok.experimental.Accessors;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsMsvcb extends CmsType {
    public CmsBoolean.ByValue svEna = new CmsBoolean.ByValue();
    public CmsUint8Array.ByValue msvID = new CmsUint8Array.ByValue(129);
    public CmsObjectReference.ByValue datSet = new CmsObjectReference.ByValue();
    public CmsInt32U.ByValue confRev = new CmsInt32U.ByValue();
    public CmsSmpMod.ByValue smpMod = new CmsSmpMod.ByValue();
    public CmsBoolean.ByValue smpMod_present = new CmsBoolean.ByValue();
    public CmsInt16U.ByValue smpRate = new CmsInt16U.ByValue();
    public CmsMsvcbOptFlds.ByValue optFlds = new CmsMsvcbOptFlds.ByValue();
    public CmsPhyComAddr.ByValue dstAddress = new CmsPhyComAddr.ByValue();
    public CmsBoolean.ByValue dstAddress_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("svEna", "msvID", "datSet", "confRev",
                "smpMod", "smpMod_present", "smpRate", "optFlds",
                "dstAddress", "dstAddress_present");
    }

    @Override
    protected int encodeBufSize() { return 512; }

    public static class ByValue extends CmsMsvcb implements Structure.ByValue {}
}