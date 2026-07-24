package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsType;
import com.ysh.jcms.data.InnerOriginator;

/**
 * Originator ::= SEQUENCE { orCat [0] INTEGER (0..8), orIdent [1] OCTET STRING (SIZE(0..64)) } — 7.5.2
 */
public class CmsOriginator extends CmsType {

    public static final int OR_CAT_NOT_SUPPORTED = 0;
    public static final int OR_CAT_BAY_CONTROL = 1;
    public static final int OR_CAT_STATION_CONTROL = 2;
    public static final int OR_CAT_REMOTE_CONTROL = 3;
    public static final int OR_CAT_AUTOMATIC_BAY = 4;
    public static final int OR_CAT_AUTOMATIC_STATION = 5;
    public static final int OR_CAT_AUTOMATIC_REMOTE = 6;
    public static final int OR_CAT_MAINTENANCE = 7;
    public static final int OR_CAT_PROCESS = 8;

    public CmsOriginator() {
        super(new InnerOriginator());
    }

    public int orCat() {
        return ((InnerOriginator) inner).orCat;
    }
    public CmsOriginator orCat(int v) {
        if (v < 0 || v > 8)
            throw new IllegalArgumentException("orCat out of range [0,8]: " + v);
        ((InnerOriginator) inner).orCat = v;
        return this;
    }
    public CmsOriginator orIdent(byte[] v) {
        ((InnerOriginator) inner).orIdent = v;
        return this;
    }
}
