package com.ysh.jcms.utils.scl.model.instance;

import com.ysh.jcms.utils.scl.model.SclVal;
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
public class SclDAI {

    private String name;
    private String sAddr;
    private String valKind;
    private Integer ix;
    private Boolean valImport;

    private final List<SclVal> vals = new ArrayList<>();

    public SclDAI addVal(SclVal val) {
        this.vals.add(val);
        return this;
    }
}
