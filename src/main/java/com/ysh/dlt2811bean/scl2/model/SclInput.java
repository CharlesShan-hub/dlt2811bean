package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclInput {

    private final List<SclExtRef> extRefs = new ArrayList<>();

    public void addExtRef(SclExtRef extRef) { this.extRefs.add(extRef); }
}
