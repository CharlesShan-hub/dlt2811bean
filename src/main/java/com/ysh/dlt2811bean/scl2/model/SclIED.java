package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclIED {

    private String name;
    private String desc;
    private SclServices services;
    private final List<SclAccessPoint> accessPoints = new ArrayList<>();

    public void addAccessPoint(SclAccessPoint ap) { this.accessPoints.add(ap); }

    public SclAccessPoint findAccessPointByName(String name) {
        for (SclAccessPoint ap : accessPoints) {
            if (ap.getName().equals(name)) return ap;
        }
        return null;
    }
}
