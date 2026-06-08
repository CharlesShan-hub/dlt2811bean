package com.ysh.jcms.datatype.control;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
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
public class CmsCheck extends CmsField {
    public CmsBoolean.ByValue syncheck = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue interlock_check = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("syncheck", "interlock_check");
    }

    public static class ByValue extends CmsCheck implements Structure.ByValue {}
}