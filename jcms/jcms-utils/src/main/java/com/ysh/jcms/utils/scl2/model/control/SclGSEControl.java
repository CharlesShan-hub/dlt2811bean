package com.ysh.jcms.utils.scl2.model.control;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Accessors(chain = true)
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
