package com.ysh.jcms.datatype.basic;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsInt8 extends CmsScalar {
    public byte value;
    public static class ByValue extends CmsInt8 implements Structure.ByValue {}
}