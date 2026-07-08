package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclTerminal {
    private String connectivityNode;
    private String substationName;
    private String voltageLevelName;
    private String bayName;
    private String cNodeName;
    private String name;
    private String processName;
    private String lineName;
}
