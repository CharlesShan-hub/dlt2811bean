package com.ysh.jcms.utils.scl.model.input;

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
            if (fcda.fc().equals(fc)) {
                return fcda;
            }
        }
        return null;
    }
}
