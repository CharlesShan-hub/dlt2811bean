package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.datatype.basic.CmsBoolean;
import com.ysh.jcms.datatype.basic.CmsInt32;
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
public class CmsQuality extends CmsField {
    public static final int GOOD          = 0;
    public static final int INVALID       = 1;
    public static final int RESERVED      = 2;
    public static final int QUESTIONABLE  = 3;

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

    @Override
    protected int encodeBufSize() { return 32; }

    /** 由于 {@code test} 字段与 {@link CmsField#test()} 冲突，用此方法启用 FFI codec。 */
    public CmsField super_test() { return super.test(); }

    public static class ByValue extends CmsQuality implements Structure.ByValue {}
}