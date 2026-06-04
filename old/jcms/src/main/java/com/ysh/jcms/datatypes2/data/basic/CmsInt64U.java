package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT64U — unsigned 64-bit integer.
 *
 * C: typedef uint64_t ...  (8 bytes)
 * ASN.1: INTEGER (0..18446744073709551615)
 */
public class CmsInt64U extends IntegerType {
    public static final int SIZE = 8;
    public CmsInt64U() { this(0L); }
    public CmsInt64U(long value) { super(SIZE, value, true); }
}
