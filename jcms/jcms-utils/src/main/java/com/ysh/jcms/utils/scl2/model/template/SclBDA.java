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
public class SclBDA {

    private String name;
    private String desc;
    private String bType;
    private String type;
    private String valKind;
    private String sAddr;
    private Integer count;

    private List<SclBDA> subBdas = new ArrayList<>();

    public SclBDA addSubBda(SclBDA subBda) {
        subBdas.add(subBda);
        return this;
    }
}
