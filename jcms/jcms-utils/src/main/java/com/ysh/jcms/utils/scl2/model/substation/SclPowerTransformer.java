package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclPowerTransformer {
    private String name;
    private String desc;
    private String type;
    private final List<SclTransformerWinding> windings = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();

    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclPowerTransformer addWinding(SclTransformerWinding winding) { windings.add(winding); return this; }
    public SclPowerTransformer addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
    public SclPowerTransformer addEqFunction(SclEqFunction eqFunction) { eqFunctions.add(eqFunction); return this; }
}
