package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT8 — signed 8-bit integer.
 *
 * C: typedef int8_t ...  (1 byte)
 * ASN.1: INTEGER (-128..127)
 */
public class CmsInt8 extends IntegerType {
    public static final int SIZE = 1;
    public CmsInt8() { this(0); }
    public CmsInt8(int value) { super(SIZE, value, false); }
}
