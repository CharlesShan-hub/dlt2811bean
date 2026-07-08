package com.ysh.jcms.utils.scl2.model.template;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SclDO {

    private String name;
    private String desc;
    private String type;
}
