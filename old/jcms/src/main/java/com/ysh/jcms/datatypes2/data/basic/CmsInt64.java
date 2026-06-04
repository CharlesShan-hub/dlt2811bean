package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT64 — signed 64-bit integer.
 *
 * C: typedef int64_t ...  (8 bytes)
 * ASN.1: INTEGER (-9223372036854775808..9223372036854775807)
 */
public class CmsInt64 extends IntegerType {
    public static final int SIZE = 8;
    public CmsInt64() { this(0L); }
    public CmsInt64(long value) { super(SIZE, value, false); }
}
