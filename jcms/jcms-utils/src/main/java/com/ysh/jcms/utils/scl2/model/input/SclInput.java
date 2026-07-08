package com.ysh.jcms.utils.scl2.model.input;

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
public class SclInput {

    private final List<SclExtRef> extRefs = new ArrayList<>();

    public SclInput addExtRef(SclExtRef extRef) {
        this.extRefs.add(extRef);
        return this;
    }
}
