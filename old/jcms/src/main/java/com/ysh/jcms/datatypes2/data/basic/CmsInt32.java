package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT32 — signed 32-bit integer.
 *
 * C: typedef int32_t ...  (4 bytes)
 * ASN.1: INTEGER (-2147483648..2147483647)
 */
public class CmsInt32 extends IntegerType {
    public static final int SIZE = 4;
    public CmsInt32() { this(0); }
    public CmsInt32(int value) { super(SIZE, value, false); }
}
