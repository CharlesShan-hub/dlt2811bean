package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclConductingEquipment {
    private String name;
    private String desc;
    private String type;
    private final List<SclTerminal> terminals = new ArrayList<>();
    private final List<SclSubEquipment> subEquipments = new ArrayList<>();

    public SclConductingEquipment addTerminal(SclTerminal terminal) { terminals.add(terminal); return this; }
    public SclConductingEquipment addSubEquipment(SclSubEquipment subEquipment) { subEquipments.add(subEquipment); return this; }
}
