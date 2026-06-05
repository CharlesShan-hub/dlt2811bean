package com.ysh.jcms.datatype.block;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsTriggerConditions extends CmsType {
    public static class ByValue extends CmsTriggerConditions implements Structure.ByValue {}

    public CmsBoolean.ByValue data_change = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue quality_change = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue data_update = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue integrity = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue general_interrogation = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("data_change", "quality_change", "data_update",
                "integrity", "general_interrogation");
    }
}