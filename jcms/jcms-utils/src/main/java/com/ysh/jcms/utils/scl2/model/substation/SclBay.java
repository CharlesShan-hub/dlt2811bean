package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclBay {
    private String name;
    private String desc;
    private final List<SclConductingEquipment> equipments = new ArrayList<>();
    private final List<SclConnectivityNode> connectivityNodes = new ArrayList<>();

    public SclBay addEquipment(SclConductingEquipment equipment) { equipments.add(equipment); return this; }
    public SclBay addConnectivityNode(SclConnectivityNode connectivityNode) { connectivityNodes.add(connectivityNode); return this; }
}
