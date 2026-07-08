package com.ysh.jcms.utils.scl2.model.template;

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
public class SclDOType {

    private String id;
    private String desc;
    private String cdc;

    private List<SclDA> das = new ArrayList<>();
    private List<SclSDO> sdos = new ArrayList<>();

    public SclDOType addDa(SclDA da) {
        das.add(da);
        return this;
    }

    public SclDOType addSdo(SclSDO sdo) {
        sdos.add(sdo);
        return this;
    }

    public SclDA findDaByName(String name) {
        for (SclDA d : das) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null;
    }

    public SclDA findDaByFc(String fc) {
        for (SclDA d : das) {
            if (fc.equals(d.getFc())) {
                return d;
            }
        }
        return null;
    }
}
