package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclSubEquipment {
    private String name;
    private String phase;
}
