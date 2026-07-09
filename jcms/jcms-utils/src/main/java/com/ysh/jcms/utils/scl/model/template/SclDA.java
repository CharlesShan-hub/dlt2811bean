package com.ysh.jcms.utils.scl.model.template;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;

import com.ysh.jcms.utils.scl.model.SclVal;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
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
    private Boolean dchg;
    private Boolean qchg;
    private Boolean dupd;
    private Boolean valImport;

    private final List<SclVal> vals = new ArrayList<>();
    private final List<SclProtNs> protNs = new ArrayList<>();

    public SclDA addVal(SclVal val) {
        vals.add(val);
        return this;
    }

    public SclDA addProtNs(SclProtNs p) {
        protNs.add(p);
        return this;
    }
}
