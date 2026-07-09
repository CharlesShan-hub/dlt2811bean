package com.ysh.jcms.utils.scl.model.control;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclGSEControl {

    private String name;
    private String desc;
    private String appID;
    private String datSet;
    private String confRev;
    private String fixedOffs;
    private String type;
    private String securityEnable;
}
