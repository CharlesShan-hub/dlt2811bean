package com.ysh.jcms.datatype.block;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
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
public class CmsMsvcbOptFlds extends CmsType {
    public CmsBoolean.ByValue refresh_time = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue sample_rate = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue data_set_name = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue security = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("refresh_time", "sample_rate", "data_set_name", "security");
    }

    public static class ByValue extends CmsMsvcbOptFlds implements Structure.ByValue {}
}