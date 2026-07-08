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
public class SclServer {

    private final List<SclLDevice> lDevices = new ArrayList<>();

    public SclServer addLDevice(SclLDevice lDevice) {
        this.lDevices.add(lDevice);
        return this;
    }

    public SclLDevice findLDeviceByInst(String inst) {
        for (SclLDevice ld : lDevices) {
            if (ld.getInst().equals(inst)) {
                return ld;
            }
        }
        return null;
    }
}
