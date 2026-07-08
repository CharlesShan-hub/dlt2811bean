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
public class SclEnumType {

    private String id;
    private String desc;

    private List<SclEnumVal> enumVals = new ArrayList<>();

    public SclEnumType addEnumVal(SclEnumVal enumVal) {
        enumVals.add(enumVal);
        return this;
    }

    public SclEnumVal findEnumValByOrd(int ord) {
        for (SclEnumVal v : enumVals) {
            if (v.getOrd() == ord) {
                return v;
            }
        }
        return null;
    }
}
