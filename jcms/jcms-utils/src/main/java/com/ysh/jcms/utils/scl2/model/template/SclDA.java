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
public class SclDA {

    private String name;
    private String desc;
    private String fc;
    private String bType;
    private String type;
    private String valKind;
    private String sAddr;
    private Integer count;

    private List<SclDA> subDas = new ArrayList<>();

    public SclDA addSubDa(SclDA subDa) {
        subDas.add(subDa);
        return this;
    }
}
