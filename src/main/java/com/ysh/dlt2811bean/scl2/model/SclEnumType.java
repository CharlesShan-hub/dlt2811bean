package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclEnumType {

    private String id;
    private String desc;
    private final List<SclEnumVal> enumVals = new ArrayList<>();

    public void addEnumVal(SclEnumVal ev) { this.enumVals.add(ev); }

    public SclEnumVal findEnumValByOrd(int ord) {
        for (SclEnumVal ev : enumVals) {
            if (ev.getOrd() == ord) return ev;
        }
        return null;
    }

    public String findEnumValByOrdAsString(int ord) {
        SclEnumVal ev = findEnumValByOrd(ord);
        return ev != null ? ev.getValue() : null;
    }
}
