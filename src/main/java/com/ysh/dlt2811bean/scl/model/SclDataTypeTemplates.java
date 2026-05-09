package com.ysh.dlt2811bean.scl.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclDataTypeTemplates {

    private List<SclLNodeType> lNodeTypes = new ArrayList<>();
    private List<SclDOType> doTypes = new ArrayList<>();
    private List<SclDAType> daTypes = new ArrayList<>();
    private List<SclEnumType> enumTypes = new ArrayList<>();

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

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclLNodeType {

        private String id;
        private String lnClass;
        private String desc;
        private List<SclDO> dos = new ArrayList<>();

        public void addDo(SclDO doObj) {
            this.dos.add(doObj);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SclDO {

        private String name;
        private String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclDOType {

        private String id;
        private String cdc;
        private String desc;
        private List<SclDA> das = new ArrayList<>();
        private List<SclSDO> sdos = new ArrayList<>();

        public void addDa(SclDA da) {
            this.das.add(da);
        }

        public void addSdo(SclSDO sdo) {
            this.sdos.add(sdo);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclDA {

        private String name;
        private String type;
        private String bType;
        private String fc;
        private boolean dchg;
        private boolean qchg;
        private boolean dupd;
        private Integer count;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SclSDO {

        private String name;
        private String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclDAType {

        private String id;
        private String desc;
        private List<SclBDA> bdas = new ArrayList<>();

        public void addBda(SclBDA bda) {
            this.bdas.add(bda);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclBDA {

        private String name;
        private String type;
        private String bType;
        private Integer count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclEnumType {

        private String id;
        private String desc;
        private List<SclEnumVal> enumVals = new ArrayList<>();

        public void addEnumVal(SclEnumVal enumVal) {
            this.enumVals.add(enumVal);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SclEnumVal {

        private int ord;
        private String value;
    }
}
