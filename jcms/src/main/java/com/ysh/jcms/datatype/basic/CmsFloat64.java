package com.ysh.jcms.datatype.basic;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter                         
@Accessors(fluent = true)
public class CmsFloat64 extends CmsScalar {
    public double value;
    public static class ByValue extends CmsFloat64 implements Structure.ByValue {}
}