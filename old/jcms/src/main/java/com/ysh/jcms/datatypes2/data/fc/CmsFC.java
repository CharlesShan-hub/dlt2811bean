package com.ysh.jcms.datatypes2.data.fc;

import com.sun.jna.IntegerType;

/**
 * INT8U — FunctionalConstraint (OCTET STRING (SIZE(2))).
 *
 * C: typedef uint8_t fc_t;
 */
public class CmsFC extends IntegerType {
    public static final int SIZE = 1;
    public CmsFC() { this(0); }
    public CmsFC(int value) { super(SIZE, value, true); }
}
