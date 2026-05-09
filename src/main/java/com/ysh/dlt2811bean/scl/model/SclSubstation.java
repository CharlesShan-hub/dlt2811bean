package com.ysh.dlt2811bean.scl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclSubstation {

    private String name;
    private String desc;
    private List<SclVoltageLevel> voltageLevels = new ArrayList<>();

    public SclSubstation(String name) {
        this.name = name;
    }

    public void addVoltageLevel(SclVoltageLevel voltageLevel) {
        this.voltageLevels.add(voltageLevel);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclVoltageLevel {

        private String name;
        private String desc;
        private Double voltage;
        private String voltageUnit;
        private String voltageMultiplier;
        private List<SclBay> bays = new ArrayList<>();
        private List<SclPowerTransformer> powerTransformers = new ArrayList<>();

        public SclVoltageLevel(String name) {
            this.name = name;
        }

        public void addBay(SclBay bay) {
            this.bays.add(bay);
        }

        public void addPowerTransformer(SclPowerTransformer pt) {
            this.powerTransformers.add(pt);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclBay {

        private String name;
        private String desc;
        private List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
        private List<SclConnectivityNode> connectivityNodes = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclBay(String name) {
            this.name = name;
        }

        public void addConductingEquipment(SclConductingEquipment ce) {
            this.conductingEquipments.add(ce);
        }

        public void addConnectivityNode(SclConnectivityNode cn) {
            this.connectivityNodes.add(cn);
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclPowerTransformer {

        private String name;
        private String type;
        private List<SclTransformerWinding> windings = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclPowerTransformer(String name) {
            this.name = name;
        }

        public void addWinding(SclTransformerWinding winding) {
            this.windings.add(winding);
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclTransformerWinding {

        private String name;
        private String type;
        private List<SclTerminal> terminals = new ArrayList<>();

        public SclTransformerWinding(String name) {
            this.name = name;
        }

        public void addTerminal(SclTerminal terminal) {
            this.terminals.add(terminal);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclConductingEquipment {

        private String name;
        private String type;
        private String desc;
        private List<SclTerminal> terminals = new ArrayList<>();
        private List<SclSubEquipment> subEquipments = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclConductingEquipment(String name, String type) {
            this.name = name;
            this.type = type;
        }

        public void addTerminal(SclTerminal terminal) {
            this.terminals.add(terminal);
        }

        public void addSubEquipment(SclSubEquipment subEquipment) {
            this.subEquipments.add(subEquipment);
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclSubEquipment {

        private String name;
        private String phase;
        private String desc;
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclSubEquipment(String name) {
            this.name = name;
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclTerminal {

        private String connectivityNode;
        private String substationName;
        private String voltageLevelName;
        private String bayName;
        private String cNodeName;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SclConnectivityNode {

        private String name;
        private String pathName;

        public SclConnectivityNode(String name) {
            this.name = name;
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclLNode {

        private String iedName;
        private String ldInst;
        private String lnClass;
        private String lnInst;
        private String prefix;
        private String desc;
    }
}
