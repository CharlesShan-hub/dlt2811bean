package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsInt16U;
import com.ysh.jcms.datatype.basic.CmsInt32U;
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
public class CmsEntryTime extends CmsType {
    public CmsInt32U.ByValue msOfDay = new CmsInt32U.ByValue();
    public CmsInt16U.ByValue daysSince1984 = new CmsInt16U.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("msOfDay", "daysSince1984");
    }

    public static class ByValue extends CmsEntryTime implements Structure.ByValue {}
}