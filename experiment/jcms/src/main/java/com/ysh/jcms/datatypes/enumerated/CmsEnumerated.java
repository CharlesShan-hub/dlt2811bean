package com.ysh.jcms.datatypes.enumerated;

import com.ysh.jcms.datatypes.type.CmsScalar;

public interface CmsEnumerated extends CmsScalar<Integer> {
    boolean is(int value);
}
