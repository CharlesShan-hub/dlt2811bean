package com.ysh.jcms.datatype.common;
import com.sun.jna.Structure;

import com.ysh.jcms.ffi.CmsScalar;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsDbpos extends CmsScalar {
    public static final int INTERMEDIATE = 0;
    public static final int OFF          = 1;
    public static final int ON           = 2;
    public static final int BAD_STATE    = 3;

    public int value;

    public static class ByValue extends CmsDbpos implements Structure.ByValue {}
}