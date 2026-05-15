package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclHeader {

    private String id;
    private String version;
    private String revision;
    private String toolId;
    private String nameStructure;
    private String text;
    private final List<SclHitem> history = new ArrayList<>();

    public void addHitem(SclHitem hitem) { this.history.add(hitem); }
}
