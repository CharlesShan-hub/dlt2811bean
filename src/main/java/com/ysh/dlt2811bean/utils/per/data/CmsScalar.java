package com.ysh.dlt2811bean.utils.per.data;

/**
 * Interface for all CMS scalar types.
 * Defines get/set operations on top of {@link CmsType}.
 *
 * <p>Scalar types represent single values such as integers, booleans, and floats.
 *
 * @param <T> the concrete type implementing this interface
 */
public interface CmsScalar<T extends CmsScalar<T>> extends CmsType<T> {

    T set(Object value);

    <V> V get();
}
