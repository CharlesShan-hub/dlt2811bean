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
public class SclBay {
    private String name;
    private String desc;
    private final List<SclConductingEquipment> equipments = new ArrayList<>();
    private final List<SclConnectivityNode> connectivityNodes = new ArrayList<>();
    private final List<SclLNode> lNodes = new ArrayList<>();
    private final List<SclPowerTransformer> transformers = new ArrayList<>();
    private final List<SclGeneralEquipment> generalEquipments = new ArrayList<>();
    private final List<SclFunction> functions = new ArrayList<>();

    public SclBay addEquipment(SclConductingEquipment equipment) {
        equipments.add(equipment);
        return this;
    }
    public SclBay addConnectivityNode(SclConnectivityNode connectivityNode) {
        connectivityNodes.add(connectivityNode);
        return this;
    }
    public SclBay addLNode(SclLNode lNode) {
        lNodes.add(lNode);
        return this;
    }
    public SclBay addTransformer(SclPowerTransformer transformer) {
        transformers.add(transformer);
        return this;
    }
    public SclBay addGeneralEquipment(SclGeneralEquipment ge) {
        generalEquipments.add(ge);
        return this;
    }
    public SclBay addFunction(SclFunction function) {
        functions.add(function);
        return this;
    }
}
