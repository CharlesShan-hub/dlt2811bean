package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclVoltageLevel {

    private String name;
    private String desc;
    private String voltage;
    private final List<SclBay> bays = new ArrayList<>();

    public void addBay(SclBay bay) { this.bays.add(bay); }
}
