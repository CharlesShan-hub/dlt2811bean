package com.ysh.jcms.datatypetest;

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
public class CmsTestPair extends CmsType {
    public CmsInt32.ByValue a = new CmsInt32.ByValue();
    public CmsInt32.ByValue b = new CmsInt32.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("a", "b");
    }

    @Override
    protected int encodeBufSize() { return 32; }
}
