package com.ysh.jcms.datatype.extended;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsTimeQuality extends CmsType {
    public CmsBoolean.ByValue leap_seconds_known = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue clock_failure = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue clock_not_synchronized = new CmsBoolean.ByValue();
    public CmsInt32.ByValue precision = new CmsInt32.ByValue();

    {
        precision().value(31);  // 11111 = not specified
    }

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("leap_seconds_known", "clock_failure", "clock_not_synchronized",
                "precision");
    }

    public static class ByValue extends CmsTimeQuality implements Structure.ByValue {}
}