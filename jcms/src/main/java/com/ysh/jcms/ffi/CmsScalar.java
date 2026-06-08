package com.ysh.jcms.ffi;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(fluent = true)
public abstract class CmsScalar extends CmsField {

    protected CmsScalar() {
        super();
    }

    @Override
    protected List<String> getFieldOrder() {
        return Collections.singletonList("value");
    }
}
