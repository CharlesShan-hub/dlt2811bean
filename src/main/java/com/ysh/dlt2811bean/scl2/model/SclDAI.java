package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDAI {

    private String name;
    private String fc;
    private String sAddr;
    private String val;
    private String valKind;
    private final List<SclDAI> subDais = new ArrayList<>();

    public void addSubDai(SclDAI subDai) { this.subDais.add(subDai); }
}
