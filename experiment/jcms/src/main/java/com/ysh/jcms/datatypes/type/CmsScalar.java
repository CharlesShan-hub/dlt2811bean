package com.ysh.jcms.datatypes.type;

public interface CmsScalar<T extends CmsScalar<T, V>, V> extends CmsType<T> {

    V get();

    void set(V value);
}
