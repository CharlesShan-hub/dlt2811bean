package com.ysh.jcms.datatypes.enumerated;

import com.ysh.jcms.datatypes.type.CmsScalar;

public interface CmsEnumerated<T extends CmsEnumerated<T>> extends CmsScalar<T, Integer> {
    boolean is(int value);
}
