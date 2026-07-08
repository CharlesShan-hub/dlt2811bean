package com.ysh.jcms.utils.scl2.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SclIED {

    private String name;
    private String desc;
    private SclServices services;

    private final List<SclAccessPoint> accessPoints = new ArrayList<>();

    public SclIED addAccessPoint(SclAccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
        return this;
    }

    public SclAccessPoint findAccessPointByName(String name) {
        for (SclAccessPoint ap : accessPoints) {
            if (ap.getName().equals(name)) {
                return ap;
            }
        }
        return null;
    }
}
