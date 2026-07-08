package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclVoltageLevel {
    private String name;
    private String desc;
    private SclVoltage voltage;
    private final List<SclBay> bays = new ArrayList<>();
    private final List<SclPowerTransformer> transformers = new ArrayList<>();  // TODO: PowerTransformer

    public SclVoltageLevel addBay(SclBay bay) { bays.add(bay); return this; }
    public SclVoltageLevel addTransformer(SclPowerTransformer transformer) { transformers.add(transformer); return this; }
}
