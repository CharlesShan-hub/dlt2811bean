package com.ysh.jcms.utils.scl2.model.communication;

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
public class SclGSE {

    private String ldInst;
    private String cbName;

    private final List<SclAddress> addresses = new ArrayList<>();

    public SclGSE addAddress(SclAddress address) {
        this.addresses.add(address);
        return this;
    }

    public SclAddress findAddressByType(String type) {
        for (SclAddress addr : addresses) {
            if (addr.getType().equals(type)) {
                return addr;
            }
        }
        return null;
    }
}
