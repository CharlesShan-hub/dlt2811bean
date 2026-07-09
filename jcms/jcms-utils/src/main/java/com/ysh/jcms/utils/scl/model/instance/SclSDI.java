package com.ysh.jcms.utils.scl.model.instance;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclSDI {

    private String name;
    private String desc;
    private Integer ix;
    private String sAddr;

    private final List<SclDAI> dais = new ArrayList<>();
    private final List<SclSDI> sdis = new ArrayList<>();

    public SclSDI addDai(SclDAI dai) {
        this.dais.add(dai);
        return this;
    }

    public SclSDI addSdi(SclSDI sdi) {
        this.sdis.add(sdi);
        return this;
    }

    public SclDAI findDaiByName(String name) {
        for (SclDAI dai : dais) {
            if (dai.name().equals(name)) {
                return dai;
            }
        }
        return null;
    }

    public SclSDI findSdiByName(String name) {
        for (SclSDI sdi : sdis) {
            if (sdi.name().equals(name)) {
                return sdi;
            }
        }
        return null;
    }
}
