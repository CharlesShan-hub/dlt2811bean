package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclSubstation {
    private String name;
    private String desc;
    private final List<SclVoltageLevel> voltageLevels = new ArrayList<>();

    public SclSubstation addVoltageLevel(SclVoltageLevel voltageLevel) { voltageLevels.add(voltageLevel); return this; }
}
