package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclSDI {

    private String name;
    private String desc;
    private final List<SclDAI> dais = new ArrayList<>();
    private final List<SclSDI> sdis = new ArrayList<>();

    public void addDai(SclDAI dai) { this.dais.add(dai); }

    public void addSdi(SclSDI sdi) { this.sdis.add(sdi); }

    public SclDAI findDaiByName(String name) {
        for (SclDAI dai : dais) {
            if (dai.getName().equals(name)) return dai;
        }
        return null;
    }

    public SclSDI findSdiByName(String name) {
        for (SclSDI sdi : sdis) {
            if (sdi.getName().equals(name)) return sdi;
        }
        return null;
    }
}
