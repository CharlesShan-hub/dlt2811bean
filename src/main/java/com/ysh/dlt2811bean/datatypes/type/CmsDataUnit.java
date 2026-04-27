package com.ysh.dlt2811bean.datatypes.type;

/**
 * Interface for CMS data unit types.
 *
 * @param <T> the concrete type implementing this interface
 * @param <V> the value type held by this data unit
 */
public interface CmsDataUnit<T extends CmsDataUnit<T, V>, V extends CmsType<V>>
        extends CmsScalar<T, V> {
}
