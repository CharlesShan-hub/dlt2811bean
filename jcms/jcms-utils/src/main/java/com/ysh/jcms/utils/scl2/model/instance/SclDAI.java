package com.ysh.jcms.utils.scl2.model.instance;

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
public class SclDAI {

    private String name;
    private String fc;
    private String sAddr;
    private String val;
    private String valKind;

    private final List<SclDAI> subDais = new ArrayList<>();

    public SclDAI addSubDai(SclDAI subDai) {
        this.subDais.add(subDai);
        return this;
    }
}
