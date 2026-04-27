package com.ysh.dlt2811bean.datatypes.type;

/**
 * Base class for CMS data unit types (CHOICE-based encodings).
 * <p>
 * Defines the common CHOICE indices shared by {@code CmsData} and {@code CmsDataDefinition}
 * (DL/T 2811 §7.7).
 *
 * @param <T> the concrete type
 * @param <V> the value type
 */
public abstract class AbstractCmsDataUnit<T extends AbstractCmsDataUnit<T, V>, V> extends AbstractCmsScalar<T, V> implements CmsDataUnit<T, V> {

    // ──────────────────────────────────────────────
    // Common CHOICE indices (DL/T 2811 §7.7)
    // ──────────────────────────────────────────────
    public static final int ERROR          = 0;
    public static final int ARRAY          = 1;
    public static final int STRUCTURE      = 2;
    public static final int BOOLEAN        = 3;
    public static final int INT8           = 4;
    public static final int INT16          = 5;
    public static final int INT32          = 6;
    public static final int INT64          = 7;
    public static final int INT8U          = 8;
    public static final int INT16U         = 9;
    public static final int INT32U         = 10;
    public static final int INT64U         = 11;
    public static final int FLOAT32        = 12;
    public static final int FLOAT64        = 13;
    public static final int BIT_STRING     = 14;
    public static final int OCTET_STRING   = 15;
    public static final int VISIBLE_STRING = 16;
    public static final int UNICODE_STRING = 17;
    public static final int UTC_TIME       = 18;
    public static final int BINARY_TIME    = 19;
    public static final int QUALITY        = 20;
    public static final int DBPOS          = 21;
    public static final int TCMD           = 22;
    public static final int CHECK          = 23;

    protected AbstractCmsDataUnit(String typeName, V defaultValue) {
        super(typeName, defaultValue);
    }
}
