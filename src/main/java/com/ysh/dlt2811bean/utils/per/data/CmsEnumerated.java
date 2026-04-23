package com.ysh.dlt2811bean.utils.per.data;

public interface CmsEnumerated<T extends CmsEnumerated<T>> extends CmsScalar<T, Integer> {
    boolean is(int value);
}
