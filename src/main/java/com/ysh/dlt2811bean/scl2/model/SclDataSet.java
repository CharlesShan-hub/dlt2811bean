package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDataSet {

    private String name;
    private String desc;
    private final List<SclFCDA> fcDas = new ArrayList<>();

    public void addFcda(SclFCDA fcda) { this.fcDas.add(fcda); }

    public List<SclFCDA> findFcdaByFc(String fc) {
        List<SclFCDA> result = new ArrayList<>();
        for (SclFCDA fcda : fcDas) {
            if (fcda.getFc().equals(fc)) result.add(fcda);
        }
        return result;
    }
}
