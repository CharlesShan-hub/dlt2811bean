package com.ysh.jcms.datatypes2.data.extended;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_binary_time_t — BinaryTime (OCTET STRING SIZE(6): Int32U msOfDay + Int16U daysSince1984)
 *
 * C: typedef struct { uint32_t msOfDay; uint16_t daysSince1984; } cms_binary_time_t;
 */
public class CmsBinaryTime extends Structure {
    public int msOfDay;               /* INT32U: milliseconds since midnight (0..86399999) */
    public short daysSince1984;       /* INT16U: days since 1984-01-01 */

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("msOfDay", "daysSince1984");
    }
}
