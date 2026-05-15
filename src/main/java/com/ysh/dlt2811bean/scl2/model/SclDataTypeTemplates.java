package com.ysh.dlt2811bean.scl2.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDataTypeTemplates {

    private final List<SclLNodeType> lNodeTypes = new ArrayList<>();
    private final List<SclDOType> doTypes = new ArrayList<>();
    private final List<SclDAType> daTypes = new ArrayList<>();
    private final List<SclEnumType> enumTypes = new ArrayList<>();

    public void addLNodeType(SclLNodeType lnt) { this.lNodeTypes.add(lnt); }

    public void addDoType(SclDOType dot) { this.doTypes.add(dot); }

    public void addDaType(SclDAType dat) { this.daTypes.add(dat); }

    public void addEnumType(SclEnumType et) { this.enumTypes.add(et); }

    public SclLNodeType findLNodeTypeById(String id) {
        for (SclLNodeType lnt : lNodeTypes) {
            if (lnt.getId().equals(id)) return lnt;
        }
        return null;
    }

    public SclDOType findDoTypeById(String id) {
        for (SclDOType dot : doTypes) {
            if (dot.getId().equals(id)) return dot;
        }
        return null;
    }

    public SclDAType findDaTypeById(String id) {
        for (SclDAType dat : daTypes) {
            if (dat.getId().equals(id)) return dat;
        }
        return null;
    }

    public SclEnumType findEnumTypeById(String id) {
        for (SclEnumType et : enumTypes) {
            if (et.getId().equals(id)) return et;
        }
        return null;
    }
}
