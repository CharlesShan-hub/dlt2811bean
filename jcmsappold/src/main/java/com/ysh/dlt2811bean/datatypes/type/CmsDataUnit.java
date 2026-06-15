package com.ysh.dlt2811bean.datatypes.type;

/**
 * Interface for CMS data unit types.
 * <p>
 * Defines the common CHOICE indices shared by {@code CmsData} and {@code CmsDataDefinition}
 * (DL/T 2811 §7.7).
 *
 * @param <T> the concrete type implementing this interface
 * @param <V> the value type held by this data unit
 */
public interface CmsDataUnit<T extends CmsDataUnit<T, V>, V> extends CmsScalar<T, V> {

    int ERROR          = 0;
    int ARRAY          = 1;
    int STRUCTURE      = 2;
    int BOOLEAN        = 3;
    int INT8           = 4;
    int INT16          = 5;
    int INT32          = 6;
    int INT64          = 7;
    int INT8U          = 8;
    int INT16U         = 9;
    int INT32U         = 10;
    int INT64U         = 11;
    int FLOAT32        = 12;
    int FLOAT64        = 13;
    int BIT_STRING     = 14;
    int OCTET_STRING   = 15;
    int VISIBLE_STRING = 16;
    int UNICODE_STRING = 17;
    int UTC_TIME       = 18;
    int BINARY_TIME    = 19;
    int QUALITY        = 20;
    int DBPOS          = 21;
    int TCMD           = 22;
    int CHECK          = 23;
}