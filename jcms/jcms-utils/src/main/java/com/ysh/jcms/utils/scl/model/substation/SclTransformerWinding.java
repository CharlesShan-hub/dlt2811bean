package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclTransformerWinding {
    private String name;
    private String desc;
    private String type;
    private SclTapChanger tapChanger;
    private SclTerminal neutralPoint;
    private final List<SclTerminal> terminals = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();
    private final List<SclSubEquipment> subEquipments = new ArrayList<>();
    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclTransformerWinding addTerminal(SclTerminal terminal) {
        terminals.add(terminal);
        return this;
    }
    public SclTransformerWinding addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }
    public SclTransformerWinding addSubEquipment(SclSubEquipment subEquipment) {
        subEquipments.add(subEquipment);
        return this;
    }
    public SclTransformerWinding addEqFunction(SclEqFunction eqFunction) {
        eqFunctions.add(eqFunction);
        return this;
    }
}
