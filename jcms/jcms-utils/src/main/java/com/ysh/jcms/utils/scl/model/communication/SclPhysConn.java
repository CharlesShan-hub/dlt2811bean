package com.ysh.jcms.utils.scl.model.communication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclPhysConn {

    private String type;

    private final List<SclAddress> ps = new ArrayList<>();

    public SclPhysConn addP(SclAddress p) {
        this.ps.add(p);
        return this;
    }
}
