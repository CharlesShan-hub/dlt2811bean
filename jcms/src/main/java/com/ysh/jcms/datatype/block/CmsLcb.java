package com.ysh.jcms.datatype.block;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.datatype.common.CmsObjectReference;
import com.ysh.jcms.ffi.CmsField;
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
public class CmsLcb extends CmsField {
    public CmsBoolean.ByValue logEna = new CmsBoolean.ByValue();
    public CmsObjectReference.ByValue datSet = new CmsObjectReference.ByValue();
    public CmsTriggerConditions.ByValue trgOps = new CmsTriggerConditions.ByValue();
    public CmsInt32U.ByValue intgPd = new CmsInt32U.ByValue();
    public CmsObjectReference.ByValue logRef = new CmsObjectReference.ByValue();
    public CmsLcbOptFlds.ByValue optFlds = new CmsLcbOptFlds.ByValue();
    public CmsBoolean.ByValue optFlds_present = new CmsBoolean.ByValue();
    public CmsInt32U.ByValue bufTm = new CmsInt32U.ByValue();
    public CmsBoolean.ByValue bufTm_present = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("logEna", "datSet", "trgOps", "intgPd", "logRef",
                "optFlds", "optFlds_present", "bufTm", "bufTm_present");
    }

    @Override
    protected int encodeBufSize() { return 512; }

    public static class ByValue extends CmsLcb implements Structure.ByValue {}
}