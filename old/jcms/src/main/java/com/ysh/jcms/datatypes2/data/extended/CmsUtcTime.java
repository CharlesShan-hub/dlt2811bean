package com.ysh.jcms.datatypes2.data.extended;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_utc_time_t — UtcTime (SEQUENCE of Int32U + Int24U + TimeQuality)
 *
 * C: typedef struct { uint32_t seconds_since_epoch; uint32_t fraction_of_second; uint8_t time_quality; } cms_utc_time_t;
 */
public class CmsUtcTime extends Structure {
    public int seconds_since_epoch;    /* INT32U */
    public int fraction_of_second;     /* INT24U (0..16777215) */
    public byte time_quality;          /* 8-bit TimeQuality */

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("seconds_since_epoch", "fraction_of_second", "time_quality");
    }
}
