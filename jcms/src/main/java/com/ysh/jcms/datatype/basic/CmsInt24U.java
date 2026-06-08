package com.ysh.jcms.datatype.basic;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsInt24U extends CmsScalar {
    public int value;
    public static class ByValue extends CmsInt24U implements Structure.ByValue {}
}