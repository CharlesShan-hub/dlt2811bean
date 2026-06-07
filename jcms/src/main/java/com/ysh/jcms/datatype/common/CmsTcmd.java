package com.ysh.jcms.datatype.common;

import com.sun.jna.Structure;
import com.ysh.jcms.ffi.CmsScalar;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public class CmsTcmd extends CmsScalar {
    public static final int RESERVED = 0;
    public static final int SELECT   = 1;
    public static final int OPERATE  = 2;
    public static final int CANCEL   = 3;

    public int value;

    public static class ByValue extends CmsTcmd implements Structure.ByValue {}
}