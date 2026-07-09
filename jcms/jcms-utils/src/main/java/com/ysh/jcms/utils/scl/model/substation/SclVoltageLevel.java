package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclVoltageLevel {
    private String name;
    private String desc;
    private SclVoltage voltage;
    private String nomFreq;
    private Integer numPhases;
    private final List<SclBay> bays = new ArrayList<>();
    private final List<SclPowerTransformer> transformers = new ArrayList<>();
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();
    private final List<SclFunction> functions = new ArrayList<>();

    public SclVoltageLevel addBay(SclBay bay) { bays.add(bay); return this; }
    public SclVoltageLevel addTransformer(SclPowerTransformer transformer) { transformers.add(transformer); return this; }
    public SclVoltageLevel addGeneralEquipment(SclGeneralEquipment ge) { generalEquipments.add(ge); return this; }
    public SclVoltageLevel addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
    public SclVoltageLevel addFunction(SclFunction function) { functions.add(function); return this; }
}
