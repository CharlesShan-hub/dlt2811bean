package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclSubNetwork {

    private String name;
    private String desc;
    private String type;
    private final List<SclConnectedAP> connectedAPs = new ArrayList<>();

    public void addConnectedAP(SclConnectedAP cap) { this.connectedAPs.add(cap); }

    public SclConnectedAP findConnectedAPByIedName(String iedName) {
        for (SclConnectedAP cap : connectedAPs) {
            if (cap.getIedName().equals(iedName)) return cap;
        }
        return null;
    }
}
