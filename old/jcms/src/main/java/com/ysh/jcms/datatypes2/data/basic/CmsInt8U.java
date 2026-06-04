package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT8U — unsigned 8-bit integer.
 *
 * C: typedef uint8_t ...  (1 byte)
 * ASN.1: INTEGER (0..255)
 */
public class CmsInt8U extends IntegerType {
    public static final int SIZE = 1;
    public CmsInt8U() { this(0); }
    public CmsInt8U(int value) { super(SIZE, value, true); }
}
