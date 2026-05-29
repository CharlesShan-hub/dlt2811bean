package com.ysh.jcms.datatypes.type;

public interface CmsScalar<V> extends CmsType {

    V get();

    void set(V value);
}
