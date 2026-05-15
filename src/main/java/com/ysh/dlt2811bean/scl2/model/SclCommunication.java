package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclCommunication {

    private final List<SclSubNetwork> subNetworks = new ArrayList<>();

    public void addSubNetwork(SclSubNetwork sn) { this.subNetworks.add(sn); }

    public SclSubNetwork findSubNetworkByName(String name) {
        for (SclSubNetwork sn : subNetworks) {
            if (sn.getName().equals(name)) return sn;
        }
        return null;
    }
}
