package com.ysh.dlt2811bean.scl.model;

import java.util.ArrayList;
import java.util.List;

public class SclCommunication {

    private List<SclSubNetwork> subNetworks = new ArrayList<>();

    public SclCommunication() {
    }

    public List<SclSubNetwork> getSubNetworks() {
        return subNetworks;
    }

    public void setSubNetworks(List<SclSubNetwork> subNetworks) {
        this.subNetworks = subNetworks;
    }

    public void addSubNetwork(SclSubNetwork subNetwork) {
        this.subNetworks.add(subNetwork);
    }

    public static class SclSubNetwork {

        private String name;
        private String type;
        private String desc;
        private Double bitRate;
        private List<SclConnectedAP> connectedAPs = new ArrayList<>();

        public SclSubNetwork() {
        }

        public SclSubNetwork(String name) {
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

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Double getBitRate() {
            return bitRate;
        }

        public void setBitRate(Double bitRate) {
            this.bitRate = bitRate;
        }

        public List<SclConnectedAP> getConnectedAPs() {
            return connectedAPs;
        }

        public void setConnectedAPs(List<SclConnectedAP> connectedAPs) {
            this.connectedAPs = connectedAPs;
        }

        public void addConnectedAP(SclConnectedAP connectedAP) {
            this.connectedAPs.add(connectedAP);
        }
    }

    public static class SclConnectedAP {

        private String iedName;
        private String apName;
        private SclAddress address;
        private List<SclGSE> gse = new ArrayList<>();
        private List<SclSMV> smv = new ArrayList<>();
        private SclPhysConn physConn;

        public SclConnectedAP() {
        }

        public SclConnectedAP(String iedName, String apName) {
            this.iedName = iedName;
            this.apName = apName;
        }

        public String getIedName() {
            return iedName;
        }

        public void setIedName(String iedName) {
            this.iedName = iedName;
        }

        public String getApName() {
            return apName;
        }

        public void setApName(String apName) {
            this.apName = apName;
        }

        public SclAddress getAddress() {
            return address;
        }

        public void setAddress(SclAddress address) {
            this.address = address;
        }

        public List<SclGSE> getGse() {
            return gse;
        }

        public void setGse(List<SclGSE> gse) {
            this.gse = gse;
        }

        public void addGse(SclGSE gse) {
            this.gse.add(gse);
        }

        public List<SclSMV> getSmv() {
            return smv;
        }

        public void setSmv(List<SclSMV> smv) {
            this.smv = smv;
        }

        public void addSmv(SclSMV smv) {
            this.smv.add(smv);
        }

        public SclPhysConn getPhysConn() {
            return physConn;
        }

        public void setPhysConn(SclPhysConn physConn) {
            this.physConn = physConn;
        }
    }

    public static class SclAddress {

        private String ip;
        private String subnet;
        private String gateway;
        private String osiTsel;
        private String osiPsel;
        private String osiSsel;

        public SclAddress() {
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getSubnet() {
            return subnet;
        }

        public void setSubnet(String subnet) {
            this.subnet = subnet;
        }

        public String getGateway() {
            return gateway;
        }

        public void setGateway(String gateway) {
            this.gateway = gateway;
        }

        public String getOsiTsel() {
            return osiTsel;
        }

        public void setOsiTsel(String osiTsel) {
            this.osiTsel = osiTsel;
        }

        public String getOsiPsel() {
            return osiPsel;
        }

        public void setOsiPsel(String osiPsel) {
            this.osiPsel = osiPsel;
        }

        public String getOsiSsel() {
            return osiSsel;
        }

        public void setOsiSsel(String osiSsel) {
            this.osiSsel = osiSsel;
        }
    }

    public static class SclGSE {

        private String ldInst;
        private String cbName;
        private SclAddress address;

        public SclGSE() {
        }

        public String getLdInst() {
            return ldInst;
        }

        public void setLdInst(String ldInst) {
            this.ldInst = ldInst;
        }

        public String getCbName() {
            return cbName;
        }

        public void setCbName(String cbName) {
            this.cbName = cbName;
        }

        public SclAddress getAddress() {
            return address;
        }

        public void setAddress(SclAddress address) {
            this.address = address;
        }
    }

    public static class SclSMV {

        private String ldInst;
        private String cbName;
        private SclAddress address;

        public SclSMV() {
        }

        public String getLdInst() {
            return ldInst;
        }

        public void setLdInst(String ldInst) {
            this.ldInst = ldInst;
        }

        public String getCbName() {
            return cbName;
        }

        public void setCbName(String cbName) {
            this.cbName = cbName;
        }

        public SclAddress getAddress() {
            return address;
        }

        public void setAddress(SclAddress address) {
            this.address = address;
        }
    }

    public static class SclPhysConn {

        private String type;
        private String plugType;
        private String plug;

        public SclPhysConn() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPlugType() {
            return plugType;
        }

        public void setPlugType(String plugType) {
            this.plugType = plugType;
        }

        public String getPlug() {
            return plug;
        }

        public void setPlug(String plug) {
            this.plug = plug;
        }
    }
}
