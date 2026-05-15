package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclGSE {

    private String ldInst;
    private String cbName;
    private final List<SclAddress> addresses = new ArrayList<>();

    public void addAddress(SclAddress addr) { this.addresses.add(addr); }

    public String findAddressByType(String type) {
        for (SclAddress addr : addresses) {
            if (addr.getType().equals(type)) return addr.getValue();
        }
        return null;
    }
}
