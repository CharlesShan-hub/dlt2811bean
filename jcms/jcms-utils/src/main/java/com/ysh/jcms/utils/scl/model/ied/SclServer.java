package com.ysh.jcms.utils.scl.model.ied;

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
public class SclServer {

    private Integer timeout = 30;

    private final List<SclLDevice> lDevices = new ArrayList<>();
    private final List<SclAssociation> associations = new ArrayList<>();

    public SclServer addLDevice(SclLDevice lDevice) {
        this.lDevices.add(lDevice);
        return this;
    }

    public SclServer addAssociation(SclAssociation association) {
        this.associations.add(association);
        return this;
    }

    public SclLDevice findLDeviceByInst(String inst) {
        for (SclLDevice ld : lDevices) {
            if (ld.inst().equals(inst)) {
                return ld;
            }
        }
        return null;
    }
}
