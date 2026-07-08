package com.ysh.jcms.utils.scl2.model.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclCommunication {

    private final List<SclSubNetwork> subNetworks = new ArrayList<>();

    public SclCommunication addSubNetwork(SclSubNetwork subNetwork) {
        this.subNetworks.add(subNetwork);
        return this;
    }

    public SclSubNetwork findSubNetworkByName(String name) {
        for (SclSubNetwork sn : subNetworks) {
            if (sn.name().equals(name)) {
                return sn;
            }
        }
        return null;
    }
}
