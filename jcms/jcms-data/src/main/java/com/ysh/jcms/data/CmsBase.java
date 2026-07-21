// Auto-generated. Base class for all Cms data types.

package com.ysh.jcms.data;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class CmsBase {
    public static final String DEFAULT_ENCODING = "per";

    @Override
    public String toString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            return getClass().getSimpleName() + "{...}";
        }
    }
}
