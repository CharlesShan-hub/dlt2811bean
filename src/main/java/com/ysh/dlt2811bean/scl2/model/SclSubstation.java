package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclSubstation {

    private String name;
    private String desc;
    private final List<SclVoltageLevel> voltageLevels = new ArrayList<>();

    public void addVoltageLevel(SclVoltageLevel vl) { this.voltageLevels.add(vl); }
}
