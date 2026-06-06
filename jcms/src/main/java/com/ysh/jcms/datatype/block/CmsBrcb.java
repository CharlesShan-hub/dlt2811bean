package com.ysh.jcms.datatype.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt16;
import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
import com.ysh.jcms.datatype.common.CmsEntryId;
import com.ysh.jcms.datatype.common.CmsObjectReference;
import com.ysh.jcms.datatype.extended.CmsBinaryTime;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsBrcb extends CmsType {
    public CmsUint8Array.ByValue rptID = new CmsUint8Array.ByValue(129);
    public CmsBoolean.ByValue rptEna = new CmsBoolean.ByValue();
    public CmsObjectReference.ByValue datSet = new CmsObjectReference.ByValue();
    public CmsInt32U.ByValue confRev = new CmsInt32U.ByValue();
    public CmsRcbOptFlds.ByValue optFlds = new CmsRcbOptFlds.ByValue();
    public CmsInt32U.ByValue bufTm = new CmsInt32U.ByValue();
    public CmsInt16U.ByValue sqNum = new CmsInt16U.ByValue();
    public CmsTriggerConditions.ByValue trgOps = new CmsTriggerConditions.ByValue();
    public CmsInt32U.ByValue intgPd = new CmsInt32U.ByValue();
    public CmsBoolean.ByValue gi = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue purgeBuf = new CmsBoolean.ByValue();
    public CmsEntryId.ByValue entryID = new CmsEntryId.ByValue();
    public CmsBinaryTime.ByValue timeOfEntry = new CmsBinaryTime.ByValue();
    public CmsInt16.ByValue resvTms = new CmsInt16.ByValue();
    public CmsBoolean.ByValue resvTms_is_present = new CmsBoolean.ByValue();
    public CmsUint8Array.ByValue owner = new CmsUint8Array.ByValue(64);
    public CmsBoolean.ByValue owner_is_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("rptID", "rptEna", "datSet", "confRev", "optFlds", "bufTm",
                "sqNum", "trgOps", "intgPd", "gi", "purgeBuf", "entryID", "timeOfEntry",
                "resvTms", "resvTms_is_present", "owner", "owner_is_present");
    }

    @Override
    protected int encodeBufSize() { return 512; }

    public static class ByValue extends CmsBrcb implements Structure.ByValue {}
}