package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclConnectedAP {

    private String iedName;
    private String apName;
    private final List<SclAddress> addresses = new ArrayList<>();
    private final List<SclGSE> gses = new ArrayList<>();
    private final List<SclSMV> smvs = new ArrayList<>();

    public void addAddress(SclAddress addr) { this.addresses.add(addr); }

    public void addGse(SclGSE gse) { this.gses.add(gse); }

    public void addSmv(SclSMV smv) { this.smvs.add(smv); }

    public String findAddressByType(String type) {
        for (SclAddress addr : addresses) {
            if (addr.getType().equals(type)) return addr.getValue();
        }
        return null;
    }
}
