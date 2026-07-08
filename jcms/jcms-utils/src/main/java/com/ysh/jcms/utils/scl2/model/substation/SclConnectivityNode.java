package com.ysh.jcms.utils.scl2.model.substation;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter @Accessors(chain = true) @NoArgsConstructor
public class SclConnectivityNode {
    private String name;
    private String pathName;
    private final List<SclLNode> lNodes = new ArrayList<>();

    public SclConnectivityNode addLNode(SclLNode lNode) { lNodes.add(lNode); return this; }
}
