package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * INT16U — unsigned 16-bit integer.
 *
 * C: typedef uint16_t ...  (2 bytes)
 * ASN.1: INTEGER (0..65535)
 */
public class CmsInt16U extends IntegerType {
    public static final int SIZE = 2;
    public CmsInt16U() { this(0); }
    public CmsInt16U(int value) { super(SIZE, value, true); }
}
