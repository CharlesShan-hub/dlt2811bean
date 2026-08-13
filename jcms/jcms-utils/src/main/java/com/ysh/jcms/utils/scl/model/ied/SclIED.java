package com.ysh.jcms.utils.scl.model.ied;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclIED {

    private String name;
    private String desc;
    private String type;
    private String manufacturer;
    private String configVersion;
    private String originalSclVersion = "2003";
    private String originalSclRevision = "A";
    private Integer originalSclRelease = 1;
    private String engRight = "full";
    private String owner;
    private SclServices services;

    private final List<SclAccessPoint> accessPoints = new ArrayList<>();

    /** Lazy index: LD instance name → LD (built on first lDevice() call, invalidated on addAccessPoint). */
    private transient Map<String, SclLDevice> ldByInst;

    public SclIED addAccessPoint(SclAccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
        this.ldByInst = null;
        return this;
    }

    public SclAccessPoint findAccessPointByName(String name) {
        for (SclAccessPoint ap : accessPoints) {
            if (ap.name().equals(name)) {
                return ap;
            }
        }
        return null;
    }

    /** All logical devices across all access points under this IED. */
    public List<SclLDevice> lDevices() {
        List<SclLDevice> result = new ArrayList<>();
        for (SclAccessPoint ap : accessPoints) {
            SclServer srv = ap.server();
            if (srv != null) {
                result.addAll(srv.lDevices());
            }
        }
        return result;
    }

    /**
     * Find a logical device by instance name across all access points (O(1) after
     * first call).
     */
    public SclLDevice lDevice(String inst) {
        if (inst == null)
            return null;
        if (ldByInst == null) {
            Map<String, SclLDevice> idx = new HashMap<>();
            for (SclAccessPoint ap : accessPoints) {
                SclServer srv = ap.server();
                if (srv != null) {
                    for (SclLDevice ld : srv.lDevices()) {
                        if (ld.inst() != null) {
                            idx.putIfAbsent(ld.inst(), ld);
                        }
                    }
                }
            }
            ldByInst = idx;
        }
        return ldByInst.get(inst);
    }
}
