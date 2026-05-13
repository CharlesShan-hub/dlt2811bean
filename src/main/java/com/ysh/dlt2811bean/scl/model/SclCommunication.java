package com.ysh.dlt2811bean.scl.model;

import lombok.Data;
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
public class SclCommunication {

    private List<SclSubNetwork> subNetworks = new ArrayList<>();

    public void addSubNetwork(SclSubNetwork subNetwork) {
        this.subNetworks.add(subNetwork);
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclSubNetwork {

        private String name;
        private String type;
        private String desc;
        private String bitRate;
        private List<SclConnectedAP> connectedAPs = new ArrayList<>();

        public SclSubNetwork(String name) {
            this.name = name;
        }

        public void addConnectedAP(SclConnectedAP connectedAP) {
            this.connectedAPs.add(connectedAP);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclConnectedAP {

        private String iedName;
        private String apName;
        private SclAddress address;
        private List<SclGSE> gse = new ArrayList<>();
        private List<SclSMV> smv = new ArrayList<>();
        private SclPhysConn physConn;

        public SclConnectedAP(String iedName, String apName) {
            this.iedName = iedName;
            this.apName = apName;
        }

        public void addGse(SclGSE gse) {
            this.gse.add(gse);
        }

        public void addSmv(SclSMV smv) {
            this.smv.add(smv);
        }
    }

    @Getter
    @NoArgsConstructor
    public static class SclAddress {

        private final Map<String, String> params = new HashMap<>();

        public void addParam(String type, String value) {
            params.put(type, value);
        }

        public String getParam(String type) {
            return params.get(type);
        }

        public String getIp() {
            return params.get("IP");
        }

        public String getSubnet() {
            return params.get("IP-SUBNET");
        }

        public String getGateway() {
            return params.get("IP-GATEWAY");
        }

        public String getOsiTsel() {
            return params.get("OSI-TSEL");
        }

        public String getOsiPsel() {
            return params.get("OSI-PSEL");
        }

        public String getOsiSsel() {
            return params.get("OSI-SSEL");
        }

        public String getMacAddress() {
            return params.get("MAC-Address");
        }

        public String getAppid() {
            return params.get("APPID");
        }

        public String getVlanId() {
            return params.get("VLAN-ID");
        }

        public String getVlanPriority() {
            return params.get("VLAN-PRIORITY");
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclGSE {

        private String ldInst;
        private String cbName;
        private SclAddress address;
    }

    @Data
    @NoArgsConstructor
    public static class SclSMV {

        private String ldInst;
        private String cbName;
        private SclAddress address;
    }

    @Data
    @NoArgsConstructor
    public static class SclPhysConn {

        private String type;
        private String plugType;
        private String plug;
    }
}
