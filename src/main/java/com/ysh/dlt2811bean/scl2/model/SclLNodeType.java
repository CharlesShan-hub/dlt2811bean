package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclLNodeType {

    private String id;
    private String lnClass;
    private String desc;
    private final List<SclDO> dos = new ArrayList<>();

    public void addDo(SclDO doObj) { this.dos.add(doObj); }

    public SclDO findDoByName(String name) {
        for (SclDO doObj : dos) {
            if (doObj.getName().equals(name)) return doObj;
        }
        return null;
    }
}
