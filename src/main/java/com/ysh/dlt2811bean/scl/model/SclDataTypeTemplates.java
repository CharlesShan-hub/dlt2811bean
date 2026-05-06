package com.ysh.dlt2811bean.scl.model;

import java.util.ArrayList;
import java.util.List;

public class SclDataTypeTemplates {

    private List<SclLNodeType> lNodeTypes = new ArrayList<>();
    private List<SclDOType> doTypes = new ArrayList<>();
    private List<SclDAType> daTypes = new ArrayList<>();
    private List<SclEnumType> enumTypes = new ArrayList<>();

    public SclDataTypeTemplates() {
    }

    public List<SclLNodeType> getLNodeTypes() {
        return lNodeTypes;
    }

    public void setLNodeTypes(List<SclLNodeType> lNodeTypes) {
        this.lNodeTypes = lNodeTypes;
    }

    public void addLNodeType(SclLNodeType lNodeType) {
        this.lNodeTypes.add(lNodeType);
    }

    public SclLNodeType findLNodeTypeById(String id) {
        for (SclLNodeType lnt : lNodeTypes) {
            if (lnt.getId().equals(id)) {
                return lnt;
            }
        }
        return null;
    }

    public List<SclDOType> getDoTypes() {
        return doTypes;
    }

    public void setDoTypes(List<SclDOType> doTypes) {
        this.doTypes = doTypes;
    }

    public void addDoType(SclDOType doType) {
        this.doTypes.add(doType);
    }

    public SclDOType findDoTypeById(String id) {
        for (SclDOType dot : doTypes) {
            if (dot.getId().equals(id)) {
                return dot;
            }
        }
        return null;
    }

    public List<SclDAType> getDaTypes() {
        return daTypes;
    }

    public void setDaTypes(List<SclDAType> daTypes) {
        this.daTypes = daTypes;
    }

    public void addDaType(SclDAType daType) {
        this.daTypes.add(daType);
    }

    public SclDAType findDaTypeById(String id) {
        for (SclDAType dat : daTypes) {
            if (dat.getId().equals(id)) {
                return dat;
            }
        }
        return null;
    }

    public List<SclEnumType> getEnumTypes() {
        return enumTypes;
    }

    public void setEnumTypes(List<SclEnumType> enumTypes) {
        this.enumTypes = enumTypes;
    }

    public void addEnumType(SclEnumType enumType) {
        this.enumTypes.add(enumType);
    }

    public SclEnumType findEnumTypeById(String id) {
        for (SclEnumType et : enumTypes) {
            if (et.getId().equals(id)) {
                return et;
            }
        }
        return null;
    }

    public static class SclLNodeType {

        private String id;
        private String lnClass;
        private String desc;
        private List<SclDO> dos = new ArrayList<>();

        public SclLNodeType() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLnClass() {
            return lnClass;
        }

        public void setLnClass(String lnClass) {
            this.lnClass = lnClass;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclDO> getDos() {
            return dos;
        }

        public void setDos(List<SclDO> dos) {
            this.dos = dos;
        }

        public void addDo(SclDO doObj) {
            this.dos.add(doObj);
        }
    }

    public static class SclDO {

        private String name;
        private String type;

        public SclDO() {
        }

        public SclDO(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SclDOType {

        private String id;
        private String cdc;
        private String desc;
        private List<SclDA> das = new ArrayList<>();
        private List<SclSDO> sdos = new ArrayList<>();

        public SclDOType() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCdc() {
            return cdc;
        }

        public void setCdc(String cdc) {
            this.cdc = cdc;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclDA> getDas() {
            return das;
        }

        public void setDas(List<SclDA> das) {
            this.das = das;
        }

        public void addDa(SclDA da) {
            this.das.add(da);
        }

        public List<SclSDO> getSdos() {
            return sdos;
        }

        public void setSdos(List<SclSDO> sdos) {
            this.sdos = sdos;
        }

        public void addSdo(SclSDO sdo) {
            this.sdos.add(sdo);
        }
    }

    public static class SclDA {

        private String name;
        private String type;
        private String bType;
        private String fc;
        private boolean dchg;
        private boolean qchg;
        private boolean dupd;
        private Integer count;

        public SclDA() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBType() {
            return bType;
        }

        public void setBType(String bType) {
            this.bType = bType;
        }

        public String getFc() {
            return fc;
        }

        public void setFc(String fc) {
            this.fc = fc;
        }

        public boolean isDchg() {
            return dchg;
        }

        public void setDchg(boolean dchg) {
            this.dchg = dchg;
        }

        public boolean isQchg() {
            return qchg;
        }

        public void setQchg(boolean qchg) {
            this.qchg = qchg;
        }

        public boolean isDupd() {
            return dupd;
        }

        public void setDupd(boolean dupd) {
            this.dupd = dupd;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public static class SclSDO {

        private String name;
        private String type;

        public SclSDO() {
        }

        public SclSDO(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SclDAType {

        private String id;
        private String desc;
        private List<SclBDA> bdas = new ArrayList<>();

        public SclDAType() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclBDA> getBdas() {
            return bdas;
        }

        public void setBdas(List<SclBDA> bdas) {
            this.bdas = bdas;
        }

        public void addBda(SclBDA bda) {
            this.bdas.add(bda);
        }
    }

    public static class SclBDA {

        private String name;
        private String type;
        private String bType;
        private Integer count;

        public SclBDA() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBType() {
            return bType;
        }

        public void setBType(String bType) {
            this.bType = bType;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }

    public static class SclEnumType {

        private String id;
        private String desc;
        private List<SclEnumVal> enumVals = new ArrayList<>();

        public SclEnumType() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclEnumVal> getEnumVals() {
            return enumVals;
        }

        public void setEnumVals(List<SclEnumVal> enumVals) {
            this.enumVals = enumVals;
        }

        public void addEnumVal(SclEnumVal enumVal) {
            this.enumVals.add(enumVal);
        }
    }

    public static class SclEnumVal {

        private int ord;
        private String value;

        public SclEnumVal() {
        }

        public SclEnumVal(int ord, String value) {
            this.ord = ord;
            this.value = value;
        }

        public int getOrd() {
            return ord;
        }

        public void setOrd(int ord) {
            this.ord = ord;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
