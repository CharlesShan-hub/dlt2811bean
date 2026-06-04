package com.ysh.jcms.datatypes2.data.basic;

import com.sun.jna.IntegerType;

/**
 * BOOLEAN — 对应 C 的 int（4 字节）。
 *
 * C: typedef int cms_boolean_t;
 * ASN.1: Boolean ::= INTEGER (0..1)
 */
public class CmsBoolean extends IntegerType {
    public CmsBoolean() { this(false); }
    public CmsBoolean(boolean value) { super(4, value ? 1 : 0, true); }
    public boolean get() { return intValue() != 0; }
}
