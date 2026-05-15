package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclBay {

    private String name;
    private String desc;
    private final List<SclConductingEquipment> equipments = new ArrayList<>();

    public void addEquipment(SclConductingEquipment eq) { this.equipments.add(eq); }
}
