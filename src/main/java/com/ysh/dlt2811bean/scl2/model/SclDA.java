package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SclDA {

    private String name;
    private String desc;
    private String fc;
    private String bType;
    private String type;
    private String valKind;
    private String sAddr;
    private Integer count;
    private final java.util.List<SclDA> subDas = new java.util.ArrayList<>();

    public void addSubDa(SclDA subDa) { this.subDas.add(subDa); }
}
