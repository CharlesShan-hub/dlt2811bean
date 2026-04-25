package com.ysh.dlt2811bean.data.type;

import java.util.List;

public interface CmsCollection<T extends CmsCollection<T, E>, E>
        extends CmsScalar<T, List<E>>, Iterable<E> {

    T capacity(int capacity);
    int getCapacity();
    int size();
    boolean isEmpty();
}