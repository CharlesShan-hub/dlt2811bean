package com.ysh.jcms.utils.scl.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true, fluent = true) @NoArgsConstructor
public class SclSubstation {
    private String name;
    private String desc;
    private final List<SclVoltageLevel> voltageLevels = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();
    private final List<SclPowerTransformer> transformers = new ArrayList<>();
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    private final List<SclFunction> functions = new ArrayList<>();

    public SclSubstation addVoltageLevel(SclVoltageLevel voltageLevel) { voltageLevels.add(voltageLevel); return this; }
    public SclSubstation addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
    public SclSubstation addTransformer(SclPowerTransformer transformer) { transformers.add(transformer); return this; }
    public SclSubstation addGeneralEquipment(SclGeneralEquipment ge) { generalEquipments.add(ge); return this; }
    public SclSubstation addFunction(SclFunction function) { functions.add(function); return this; }
}
