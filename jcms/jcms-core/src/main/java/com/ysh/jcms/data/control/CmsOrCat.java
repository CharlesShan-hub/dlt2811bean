package com.ysh.jcms.data.control;

import com.ysh.jcms.core.CmsEnum;

/**
 * orCat ::= INTEGER (0..8) — 7.5.2
 */
@CmsEnum.ValueRange(min = 0, max = 8)
public class CmsOrCat extends CmsEnum<CmsOrCat> {

    public static final int NOT_SUPPORTED = 0;
    public static final int BAY_CONTROL = 1;
    public static final int STATION_CONTROL = 2;
    public static final int REMOTE_CONTROL = 3;
    public static final int AUTOMATIC_BAY = 4;
    public static final int AUTOMATIC_STATION = 5;
    public static final int AUTOMATIC_REMOTE = 6;
    public static final int MAINTENANCE = 7;
    public static final int PROCESS = 8;

    public CmsOrCat() {}
    public CmsOrCat(int v) { this(); value(v); }
}
