package com.ysh.jcms.datatype.control;
import com.sun.jna.Structure;

import com.ysh.jcms.datatype.basic.CmsInt32;
import com.ysh.jcms.datatype.basic.CmsUint8Array;
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
public class CmsOriginator extends CmsType {
    public static final int ORCAT_NOT_SUPPORTED      = 0;
    public static final int ORCAT_BAY_CONTROL        = 1;
    public static final int ORCAT_STATION_CONTROL    = 2;
    public static final int ORCAT_REMOTE_CONTROL     = 3;
    public static final int ORCAT_AUTOMATIC_BAY      = 4;
    public static final int ORCAT_AUTOMATIC_STATION  = 5;
    public static final int ORCAT_AUTOMATIC_REMOTE   = 6;
    public static final int ORCAT_MAINTENANCE        = 7;
    public static final int ORCAT_PROCESS            = 8;

    public CmsInt32.ByValue or_cat = new CmsInt32.ByValue();
    public CmsUint8Array.ByValue or_ident = new CmsUint8Array.ByValue(64);

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("or_cat", "or_ident");
    }

    public static class ByValue extends CmsOriginator implements Structure.ByValue {}
}