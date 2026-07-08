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
public class SclConnectedAP {

    private String iedName;
    private String apName;

    private final List<SclAddress> addresses = new ArrayList<>();
    private final List<SclGSE> gses = new ArrayList<>();
    private final List<SclSMV> smvs = new ArrayList<>();
    private final List<SclPhysConn> physConns = new ArrayList<>();

    public SclConnectedAP addAddress(SclAddress address) {
        this.addresses.add(address);
        return this;
    }

    public SclConnectedAP addGse(SclGSE gse) {
        this.gses.add(gse);
        return this;
    }

    public SclConnectedAP addSmv(SclSMV smv) {
        this.smvs.add(smv);
        return this;
    }

    public SclConnectedAP addPhysConn(SclPhysConn physConn) {
        this.physConns.add(physConn);
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
