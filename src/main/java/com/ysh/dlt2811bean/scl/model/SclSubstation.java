package com.ysh.dlt2811bean.scl.model;

import java.util.ArrayList;
import java.util.List;

public class SclSubstation {

    private String name;
    private String desc;
    private List<SclVoltageLevel> voltageLevels = new ArrayList<>();

    public SclSubstation() {
    }

    public SclSubstation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SclVoltageLevel> getVoltageLevels() {
        return voltageLevels;
    }

    public void setVoltageLevels(List<SclVoltageLevel> voltageLevels) {
        this.voltageLevels = voltageLevels;
    }

    public void addVoltageLevel(SclVoltageLevel voltageLevel) {
        this.voltageLevels.add(voltageLevel);
    }

    public static class SclVoltageLevel {

        private String name;
        private String desc;
        private Double voltage;
        private String voltageUnit;
        private String voltageMultiplier;
        private List<SclBay> bays = new ArrayList<>();
        private List<SclPowerTransformer> powerTransformers = new ArrayList<>();

        public SclVoltageLevel() {
        }

        public SclVoltageLevel(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Double getVoltage() {
            return voltage;
        }

        public void setVoltage(Double voltage) {
            this.voltage = voltage;
        }

        public String getVoltageUnit() {
            return voltageUnit;
        }

        public void setVoltageUnit(String voltageUnit) {
            this.voltageUnit = voltageUnit;
        }

        public String getVoltageMultiplier() {
            return voltageMultiplier;
        }

        public void setVoltageMultiplier(String voltageMultiplier) {
            this.voltageMultiplier = voltageMultiplier;
        }

        public List<SclBay> getBays() {
            return bays;
        }

        public void setBays(List<SclBay> bays) {
            this.bays = bays;
        }

        public void addBay(SclBay bay) {
            this.bays.add(bay);
        }

        public List<SclPowerTransformer> getPowerTransformers() {
            return powerTransformers;
        }

        public void setPowerTransformers(List<SclPowerTransformer> powerTransformers) {
            this.powerTransformers = powerTransformers;
        }

        public void addPowerTransformer(SclPowerTransformer pt) {
            this.powerTransformers.add(pt);
        }
    }

    public static class SclBay {

        private String name;
        private String desc;
        private List<SclConductingEquipment> conductingEquipments = new ArrayList<>();
        private List<SclConnectivityNode> connectivityNodes = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclBay() {
        }

        public SclBay(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclConductingEquipment> getConductingEquipments() {
            return conductingEquipments;
        }

        public void setConductingEquipments(List<SclConductingEquipment> conductingEquipments) {
            this.conductingEquipments = conductingEquipments;
        }

        public void addConductingEquipment(SclConductingEquipment ce) {
            this.conductingEquipments.add(ce);
        }

        public List<SclConnectivityNode> getConnectivityNodes() {
            return connectivityNodes;
        }

        public void setConnectivityNodes(List<SclConnectivityNode> connectivityNodes) {
            this.connectivityNodes = connectivityNodes;
        }

        public void addConnectivityNode(SclConnectivityNode cn) {
            this.connectivityNodes.add(cn);
        }

        public List<SclLNode> getLNodes() {
            return lNodes;
        }

        public void setLNodes(List<SclLNode> lNodes) {
            this.lNodes = lNodes;
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    public static class SclPowerTransformer {

        private String name;
        private String type;
        private List<SclTransformerWinding> windings = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclPowerTransformer() {
        }

        public SclPowerTransformer(String name) {
            this.name = name;
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

        public List<SclTransformerWinding> getWindings() {
            return windings;
        }

        public void setWindings(List<SclTransformerWinding> windings) {
            this.windings = windings;
        }

        public void addWinding(SclTransformerWinding winding) {
            this.windings.add(winding);
        }

        public List<SclLNode> getLNodes() {
            return lNodes;
        }

        public void setLNodes(List<SclLNode> lNodes) {
            this.lNodes = lNodes;
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    public static class SclTransformerWinding {

        private String name;
        private String type;
        private List<SclTerminal> terminals = new ArrayList<>();

        public SclTransformerWinding() {
        }

        public SclTransformerWinding(String name) {
            this.name = name;
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

        public List<SclTerminal> getTerminals() {
            return terminals;
        }

        public void setTerminals(List<SclTerminal> terminals) {
            this.terminals = terminals;
        }

        public void addTerminal(SclTerminal terminal) {
            this.terminals.add(terminal);
        }
    }

    public static class SclConductingEquipment {

        private String name;
        private String type;
        private String desc;
        private List<SclTerminal> terminals = new ArrayList<>();
        private List<SclSubEquipment> subEquipments = new ArrayList<>();
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclConductingEquipment() {
        }

        public SclConductingEquipment(String name, String type) {
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclTerminal> getTerminals() {
            return terminals;
        }

        public void setTerminals(List<SclTerminal> terminals) {
            this.terminals = terminals;
        }

        public void addTerminal(SclTerminal terminal) {
            this.terminals.add(terminal);
        }

        public List<SclSubEquipment> getSubEquipments() {
            return subEquipments;
        }

        public void setSubEquipments(List<SclSubEquipment> subEquipments) {
            this.subEquipments = subEquipments;
        }

        public void addSubEquipment(SclSubEquipment subEquipment) {
            this.subEquipments.add(subEquipment);
        }

        public List<SclLNode> getLNodes() {
            return lNodes;
        }

        public void setLNodes(List<SclLNode> lNodes) {
            this.lNodes = lNodes;
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    public static class SclSubEquipment {

        private String name;
        private String phase;
        private String desc;
        private List<SclLNode> lNodes = new ArrayList<>();

        public SclSubEquipment() {
        }

        public SclSubEquipment(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclLNode> getLNodes() {
            return lNodes;
        }

        public void setLNodes(List<SclLNode> lNodes) {
            this.lNodes = lNodes;
        }

        public void addLNode(SclLNode lNode) {
            this.lNodes.add(lNode);
        }
    }

    public static class SclTerminal {

        private String connectivityNode;
        private String substationName;
        private String voltageLevelName;
        private String bayName;
        private String cNodeName;

        public SclTerminal() {
        }

        public String getConnectivityNode() {
            return connectivityNode;
        }

        public void setConnectivityNode(String connectivityNode) {
            this.connectivityNode = connectivityNode;
        }

        public String getSubstationName() {
            return substationName;
        }

        public void setSubstationName(String substationName) {
            this.substationName = substationName;
        }

        public String getVoltageLevelName() {
            return voltageLevelName;
        }

        public void setVoltageLevelName(String voltageLevelName) {
            this.voltageLevelName = voltageLevelName;
        }

        public String getBayName() {
            return bayName;
        }

        public void setBayName(String bayName) {
            this.bayName = bayName;
        }

        public String getCNodeName() {
            return cNodeName;
        }

        public void setCNodeName(String cNodeName) {
            this.cNodeName = cNodeName;
        }
    }

    public static class SclConnectivityNode {

        private String name;
        private String pathName;

        public SclConnectivityNode() {
        }

        public SclConnectivityNode(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPathName() {
            return pathName;
        }

        public void setPathName(String pathName) {
            this.pathName = pathName;
        }
    }

    public static class SclLNode {

        private String iedName;
        private String ldInst;
        private String lnClass;
        private String lnInst;
        private String prefix;
        private String desc;

        public SclLNode() {
        }

        public String getIedName() {
            return iedName;
        }

        public void setIedName(String iedName) {
            this.iedName = iedName;
        }

        public String getLdInst() {
            return ldInst;
        }

        public void setLdInst(String ldInst) {
            this.ldInst = ldInst;
        }

        public String getLnClass() {
            return lnClass;
        }

        public void setLnClass(String lnClass) {
            this.lnClass = lnClass;
        }

        public String getLnInst() {
            return lnInst;
        }

        public void setLnInst(String lnInst) {
            this.lnInst = lnInst;
        }

        public String getPrefix() {
            return prefix;
        }

        public void setPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
