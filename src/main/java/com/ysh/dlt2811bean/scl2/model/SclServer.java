package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclServer {

    private final List<SclLDevice> lDevices = new ArrayList<>();

    public void addLDevice(SclLDevice ld) { this.lDevices.add(ld); }

    public SclLDevice findLDeviceByInst(String inst) {
        for (SclLDevice ld : lDevices) {
            if (ld.getInst().equals(inst)) return ld;
        }
        return null;
    }

    public List<String> getLDeviceNames() {
        return lDevices.stream().map(SclLDevice::getInst).toList();
    }

    public List<String> getLDeviceNames(String after) {
        List<String> names = getLDeviceNames();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }

    public List<String> getAllLnNames() {
        List<String> names = new ArrayList<>();
        for (SclLDevice ld : lDevices) {
            names.addAll(ld.getLnNames());
        }
        return names;
    }

    public List<String> getAllLnNames(String after) {
        List<String> names = getAllLnNames();
        if (after == null || after.isEmpty()) return names;
        int idx = names.indexOf(after);
        if (idx < 0) return null;
        return names.subList(idx + 1, names.size());
    }
}
