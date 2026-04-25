package com.ysh.dlt2811bean.data.type;

import com.ysh.dlt2811bean.per.io.PerInputStream;
import com.ysh.dlt2811bean.per.io.PerOutputStream;

/**
 * Root interface for all CMS data types.
 * Defines the most basic operations that all CMS types must implement.
 *
 * @param <T> the concrete type implementing this interface
 */
public interface CmsType<T extends CmsType<T>> {
    void encode(PerOutputStream pos);

    T decode(PerInputStream pis) throws Exception;

    T copy();

    @Override
    String toString();
}
