package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDOType {

    private String id;
    private String desc;
    private String cdc;
    private final List<SclDA> das = new ArrayList<>();

    public void addDa(SclDA da) { this.das.add(da); }

    public SclDA findDaByName(String name) {
        for (SclDA da : das) {
            if (da.getName().equals(name)) return da;
        }
        return null;
    }

    public List<SclDA> findDaByFc(String fc) {
        List<SclDA> result = new ArrayList<>();
        for (SclDA da : das) {
            if (da.getFc().equals(fc)) result.add(da);
        }
        return result;
    }
}
