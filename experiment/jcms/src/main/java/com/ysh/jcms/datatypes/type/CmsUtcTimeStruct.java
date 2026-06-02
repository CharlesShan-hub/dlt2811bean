package com.ysh.jcms.datatypes.type;

import com.sun.jna.Structure;

public class CmsUtcTimeStruct extends Structure {

    public int seconds_since_epoch;
    public int fraction_of_second;
    public byte time_quality;

    public CmsUtcTimeStruct() {
    }

    public static class ByReference extends CmsUtcTimeStruct implements Structure.ByReference {
    }

    @Override
    protected java.util.List<String> getFieldOrder() {
        return java.util.Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }
}
