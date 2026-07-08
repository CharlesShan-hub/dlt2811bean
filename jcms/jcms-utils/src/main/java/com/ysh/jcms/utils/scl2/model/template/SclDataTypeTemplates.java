package com.ysh.jcms.utils.scl2.model.template;

import lombok.experimental.Accessors;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

@Getter
@Setter
@Accessors(chain = true, fluent = true)
@NoArgsConstructor
public class SclDataTypeTemplates {

    private List<SclLNodeType> lNodeTypes = new ArrayList<>();
    private List<SclDOType> doTypes = new ArrayList<>();
    private List<SclDAType> daTypes = new ArrayList<>();
    private List<SclEnumType> enumTypes = new ArrayList<>();

    private transient Map<String, SclLNodeType> lNodeTypeIndex;
    private transient Map<String, SclDOType> doTypeIndex;
    private transient Map<String, SclDAType> daTypeIndex;
    private transient Map<String, SclEnumType> enumTypeIndex;

    public SclDataTypeTemplates addLNodeType(SclLNodeType lNodeType) {
        lNodeTypes.add(lNodeType);
        return this;
    }

    public SclDataTypeTemplates addDoType(SclDOType doType) {
        doTypes.add(doType);
        return this;
    }

    public SclDataTypeTemplates addDaType(SclDAType daType) {
        daTypes.add(daType);
        return this;
    }

    public SclDataTypeTemplates addEnumType(SclEnumType enumType) {
        enumTypes.add(enumType);
        return this;
    }

    public SclLNodeType findLNodeTypeById(String id) {
        if (lNodeTypeIndex == null) {
            lNodeTypeIndex = new HashMap<>();
            for (SclLNodeType t : lNodeTypes) {
                lNodeTypeIndex.put(t.id(), t);
            }
        }
        return lNodeTypeIndex.get(id);
    }

    public SclDOType findDoTypeById(String id) {
        if (doTypeIndex == null) {
            doTypeIndex = new HashMap<>();
            for (SclDOType t : doTypes) {
                doTypeIndex.put(t.id(), t);
            }
        }
        return doTypeIndex.get(id);
    }

    public SclDAType findDaTypeById(String id) {
        if (daTypeIndex == null) {
            daTypeIndex = new HashMap<>();
            for (SclDAType t : daTypes) {
                daTypeIndex.put(t.id(), t);
            }
        }
        return daTypeIndex.get(id);
    }

    public SclEnumType findEnumTypeById(String id) {
        if (enumTypeIndex == null) {
            enumTypeIndex = new HashMap<>();
            for (SclEnumType t : enumTypes) {
                enumTypeIndex.put(t.id(), t);
            }
        }
        return enumTypeIndex.get(id);
    }
}
