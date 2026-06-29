package com.ysh.jcms.utils.scl.model.template;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SclDataTypeTemplates {

    private final List<SclLNodeType> lNodeTypes = new ArrayList<>();
    private final List<SclDOType> doTypes = new ArrayList<>();
    private final List<SclDAType> daTypes = new ArrayList<>();
    private final List<SclEnumType> enumTypes = new ArrayList<>();

    // Index maps for O(1) lookups, lazily populated
    private transient Map<String, SclLNodeType> lNodeTypeIndex;
    private transient Map<String, SclDOType> doTypeIndex;
    private transient Map<String, SclDAType> daTypeIndex;
    private transient Map<String, SclEnumType> enumTypeIndex;

    private void ensureLNodeTypeIndex() {
        if (lNodeTypeIndex == null) {
            lNodeTypeIndex = new HashMap<>();
            for (SclLNodeType lnt : lNodeTypes) {
                lNodeTypeIndex.put(lnt.getId(), lnt);
            }
        }
    }

    private void ensureDoTypeIndex() {
        if (doTypeIndex == null) {
            doTypeIndex = new HashMap<>();
            for (SclDOType dot : doTypes) {
                doTypeIndex.put(dot.getId(), dot);
            }
        }
    }

    private void ensureDaTypeIndex() {
        if (daTypeIndex == null) {
            daTypeIndex = new HashMap<>();
            for (SclDAType dat : daTypes) {
                daTypeIndex.put(dat.getId(), dat);
            }
        }
    }

    private void ensureEnumTypeIndex() {
        if (enumTypeIndex == null) {
            enumTypeIndex = new HashMap<>();
            for (SclEnumType et : enumTypes) {
                enumTypeIndex.put(et.getId(), et);
            }
        }
    }

    public void addLNodeType(SclLNodeType lnt) {
        this.lNodeTypes.add(lnt);
        if (lNodeTypeIndex != null) lNodeTypeIndex.put(lnt.getId(), lnt);
    }

    public void addDoType(SclDOType dot) {
        this.doTypes.add(dot);
        if (doTypeIndex != null) doTypeIndex.put(dot.getId(), dot);
    }

    public void addDaType(SclDAType dat) {
        this.daTypes.add(dat);
        if (daTypeIndex != null) daTypeIndex.put(dat.getId(), dat);
    }

    public void addEnumType(SclEnumType et) {
        this.enumTypes.add(et);
        if (enumTypeIndex != null) enumTypeIndex.put(et.getId(), et);
    }

    public SclLNodeType findLNodeTypeById(String id) {
        if (id == null) return null;
        ensureLNodeTypeIndex();
        return lNodeTypeIndex.get(id);
    }

    public SclDOType findDoTypeById(String id) {
        if (id == null) return null;
        ensureDoTypeIndex();
        return doTypeIndex.get(id);
    }

    public SclDAType findDaTypeById(String id) {
        if (id == null) return null;
        ensureDaTypeIndex();
        return daTypeIndex.get(id);
    }

    public SclEnumType findEnumTypeById(String id) {
        if (id == null) return null;
        ensureEnumTypeIndex();
        return enumTypeIndex.get(id);
    }
}
