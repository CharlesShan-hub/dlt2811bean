package com.ysh.jcms.utils.scl2.model.template;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclEnumVal {

    private int ord;
    private String value;
    private String desc;
}
