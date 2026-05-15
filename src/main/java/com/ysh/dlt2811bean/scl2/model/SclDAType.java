package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDAType {

    private String id;
    private String desc;
    private String iedType;
    private final List<SclBDA> bdas = new ArrayList<>();

    public void addBda(SclBDA bda) { this.bdas.add(bda); }

    public SclBDA findBdaByName(String name) {
        for (SclBDA bda : bdas) {
            if (bda.getName().equals(name)) return bda;
        }
        return null;
    }
}
