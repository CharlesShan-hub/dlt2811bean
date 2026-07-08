package com.ysh.jcms.utils.scl2.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SclGSESettings {

    private String appID;
    private String cbName;
    private String datSet;
}
