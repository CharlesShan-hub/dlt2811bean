package com.ysh.jcms.utils.scl2.model.template;

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
public class SclLNodeType {

    private String id;
    private String lnClass;
    private String desc;
    private String iedType = "";

    private List<SclDO> dos = new ArrayList<>();

    public SclLNodeType addDo(SclDO doObj) {
        dos.add(doObj);
        return this;
    }

    public SclDO findDoByName(String name) {
        for (SclDO d : dos) {
            if (d.name().equals(name)) {
                return d;
            }
        }
        return null;
    }
}
