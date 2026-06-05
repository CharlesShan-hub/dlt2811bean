package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32;
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
public class CmsQuality extends CmsType {
    public CmsInt32.ByValue validity = new CmsInt32.ByValue();
    public CmsBoolean.ByValue overflow = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue outOfRange = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue badReference = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue oscillatory = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue failure = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue oldData = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue inconsistent = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue inaccurate = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue substituted = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue test = new CmsBoolean.ByValue();
    public CmsBoolean.ByValue operatorBlocked = new CmsBoolean.ByValue();

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("validity", "overflow", "outOfRange", "badReference",
                "oscillatory", "failure", "oldData", "inconsistent",
                "inaccurate", "substituted", "test", "operatorBlocked");
    }

    public static class ByValue extends CmsQuality implements Structure.ByValue {}
}