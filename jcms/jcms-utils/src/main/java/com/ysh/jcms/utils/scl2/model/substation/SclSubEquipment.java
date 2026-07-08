package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclSubEquipment {
    private String name;
    private String desc;
    private String phase;
    private Boolean virtual;
    private final List<SclLNode> lNodes = new ArrayList<>();
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclSubEquipment addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
    public SclSubEquipment addEqFunction(SclEqFunction eqFunction) { eqFunctions.add(eqFunction); return this; }
}
