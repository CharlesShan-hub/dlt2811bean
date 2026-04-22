package com.ysh.dlt2811bean.utils.per.data;

import com.ysh.dlt2811bean.utils.per.io.PerInputStream;
import com.ysh.dlt2811bean.utils.per.io.PerOutputStream;

/**
 * Root interface for all CMS data types.
 * Defines the most basic operations that all CMS types must implement.
 *
 * @param <T> the concrete type implementing this interface
 */
public interface CmsType<T extends CmsType<T>> {
    void encode(PerOutputStream pos);

    T decode(PerInputStream pis) throws Exception;

    @Override
    String toString();
}
