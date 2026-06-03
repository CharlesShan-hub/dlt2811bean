package com.ysh.jcms.datatypes.type;

import java.util.List;

/**
 * Lightweight collection interface — a container of homogeneous elements.
 *
 * <p>In jcms, PER encoding is handled externally (e.g. via {@code CmsData} JNA),
 * so this interface is purely for Java-side collection semantics.
 *
 * @param <T> the concrete collection type (self-type)
 * @param <E> the element type
 */
public interface CmsCollection<T extends CmsCollection<T, E>, E> {

    int size();
    boolean isEmpty();
    E get(int index);
    T add(E element);
    T addAll(List<? extends E> items);
    List<E> elements();
    T copy();
}
