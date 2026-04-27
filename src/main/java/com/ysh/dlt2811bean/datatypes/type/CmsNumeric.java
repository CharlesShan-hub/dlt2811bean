package com.ysh.dlt2811bean.datatypes.type;

/**
 *  Interface for all CMS numeric types.
 *
 * @param <T> the concrete numeric
 * @param <V> the value type (e.g. Integer, Long, Float, Double)
 */
public interface CmsNumeric<T extends CmsNumeric<T, V>, V> extends CmsScalar<T, V> {
}
