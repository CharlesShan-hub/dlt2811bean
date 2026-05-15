package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclBDA {

    private String name;
    private String desc;
    private String bType;
    private String type;
    private String valKind;
    private String sAddr;
    private Integer count;
    private final List<SclBDA> subBdas = new ArrayList<>();

    public void addSubBda(SclBDA subBda) { this.subBdas.add(subBda); }
}
