package com.ysh.jcms.utils.scl2.model.input;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
public class SclDataSet {

    private String name;
    private String desc;
    private boolean dynamic;

    private final List<SclFCDA> fcDas = new ArrayList<>();

    public SclDataSet addFcda(SclFCDA fcda) {
        this.fcDas.add(fcda);
        return this;
    }

    public SclFCDA findFcdaByFc(String fc) {
        for (SclFCDA fcda : fcDas) {
            if (fcda.getFc().equals(fc)) {
                return fcda;
            }
        }
        return null;
    }
}
