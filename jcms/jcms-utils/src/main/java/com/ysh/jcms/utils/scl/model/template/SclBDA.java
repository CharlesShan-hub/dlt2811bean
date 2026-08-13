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
public class SclBDA {

    private String name;
    private String desc;
    private String bType;
    private String type;
    private String valKind;
    private String sAddr;
    private Integer count;
    private Boolean valImport;
    /** 触发条件（tBDA 继承 agTrgOps，可选）。 */
    private Boolean dchg;
    private Boolean qchg;
    private Boolean dupd;

    private final List<SclVal> vals = new ArrayList<>();

    public SclBDA addVal(SclVal val) {
        vals.add(val);
        return this;
    }
}
