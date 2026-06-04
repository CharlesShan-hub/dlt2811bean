package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT32U — unsigned 32-bit integer.
 *
 * C: typedef uint32_t ...  (4 bytes)
 * ASN.1: INTEGER (0..4294967295)
 */
public class CmsInt32U extends IntegerType {
    public static final int SIZE = 4;
    public CmsInt32U() { this(0L); }
    public CmsInt32U(long value) { super(SIZE, value, true); }
}
