package com.ysh.jcms.datatype.extended;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsInt24U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
import com.ysh.jcms.ffi.CmsType;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsUtcTime extends CmsType {
    public CmsInt32U.ByValue seconds_since_epoch = new CmsInt32U.ByValue();
    public CmsInt24U.ByValue fraction_of_second = new CmsInt24U.ByValue();
    public CmsTimeQuality.ByValue time_quality = new CmsTimeQuality.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }

    public static class ByValue extends CmsUtcTime implements Structure.ByValue {}
}