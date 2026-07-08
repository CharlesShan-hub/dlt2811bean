package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclConductingEquipment {
    private String name;
    private String desc;
    private String type;
    private final List<SclTerminal> terminals = new ArrayList<>();
    private final List<SclSubEquipment> subEquipments = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();

    private final List<SclEqFunction> eqFunctions = new ArrayList<>();

    public SclConductingEquipment addTerminal(SclTerminal terminal) { terminals.add(terminal); return this; }
    public SclConductingEquipment addSubEquipment(SclSubEquipment subEquipment) { subEquipments.add(subEquipment); return this; }
    public SclConductingEquipment addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
    public SclConductingEquipment addEqFunction(SclEqFunction eqFunction) { eqFunctions.add(eqFunction); return this; }
}
