package com.ysh.jcms.datatype.basic;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsInt8U extends CmsScalar {
    public byte value;
    public static class ByValue extends CmsInt8U implements Structure.ByValue {}
}