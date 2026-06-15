package com.ysh.dlt2811bean.datatypes.type;

/**
 * Interface for all CMS scalar types.
 * Defines set/get operations on top of {@link CmsType}.
 *
 * @param <T> the concrete type implementing this interface
 * @param <V> the wrapper type of the value
 */
public interface CmsScalar<T extends CmsScalar<T, V>, V> extends CmsType<T> {

    T set(V value);

    V get();
}
