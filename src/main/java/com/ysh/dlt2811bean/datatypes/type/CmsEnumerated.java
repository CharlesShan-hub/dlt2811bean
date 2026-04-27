package com.ysh.dlt2811bean.datatypes.type;

public interface CmsEnumerated<T extends CmsEnumerated<T>> extends CmsScalar<T, Integer> {
    boolean is(int value);
}
