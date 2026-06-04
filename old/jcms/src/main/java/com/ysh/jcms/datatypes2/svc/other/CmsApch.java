package com.ysh.jcms.datatypes2.svc.other;

import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * cms_apch_t — APCH (4-byte frame header).
 *
 * C: typedef struct { uint8_t cc; uint8_t sc; uint16_t fl; } cms_apch_t;
 */
public class CmsApch extends Structure {
    public byte cc;    /* control code */
    public byte sc;    /* service code */
    public short fl;   /* frame length */

    @Override
    protected List<String> getFieldOrder() {
        return Arrays.asList("cc", "sc", "fl");
    }
}
