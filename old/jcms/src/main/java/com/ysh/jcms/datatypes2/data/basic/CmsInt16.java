package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT16 — signed 16-bit integer.
 *
 * C: typedef int16_t ...  (2 bytes)
 * ASN.1: INTEGER (-32768..32767)
 */
public class CmsInt16 extends IntegerType {
    public static final int SIZE = 2;
    public CmsInt16() { this(0); }
    public CmsInt16(int value) { super(SIZE, value, false); }
}
