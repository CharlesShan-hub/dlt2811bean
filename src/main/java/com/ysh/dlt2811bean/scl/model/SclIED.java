package com.ysh.dlt2811bean.scl.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class SclIED {

    private String name;
    private String desc;
    private SclServices services;
    private List<SclAccessPoint> accessPoints = new ArrayList<>();

    public SclIED(String name) {
        this.name = name;
    }

    public void addAccessPoint(SclAccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
    }

    /**
     * Finds an AccessPoint by its name.
     *
     * @param name the AccessPoint name to search for
     * @return the matching AccessPoint, or null if not found
     */
    public SclAccessPoint findAccessPointByName(String name) {
        for (SclAccessPoint ap : accessPoints) {
            if (ap.getName().equals(name)) {
                return ap;
            }
        }
        return null;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclServices {

        private boolean dynAssociation;
        private boolean getDirectory;
        private boolean getDataObjectDefinition;
        private boolean getDataSetValue;
        private boolean dataSetDirectory;
        private boolean readWrite;
        private boolean fileHandling;
        private boolean getCBValues;
        private boolean gSEDir;
        private boolean timerActivatedControl;
        private Integer confDataSetMax;
        private Integer confDataSetMaxAttributes;
        private Integer confReportControlMax;
        private Integer confLogControlMax;
        private Integer gooseMax;
        private Integer gsseMax;
        private Boolean confLNsFixPrefix;
        private Boolean confLNsFixLnInst;
        private SclReportSettings reportSettings;
        private SclGSESettings gseSettings;
    }

    @Data
    @NoArgsConstructor
    public static class SclReportSettings {

        private String bufTime;
        private String cbName;
        private String rptID;
        private String datSet;
        private String intgPd;
        private String optFields;
    }

    @Data
    @NoArgsConstructor
    public static class SclGSESettings {

        private String appID;
        private String cbName;
        private String datSet;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclAccessPoint {

        private String name;
        private SclServer server;

        public SclAccessPoint(String name) {
            this.name = name;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclServer {

        private List<SclLDevice> lDevices = new ArrayList<>();

        public void addLDevice(SclLDevice lDevice) {
            this.lDevices.add(lDevice);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclLDevice {

        private String inst;
        private String desc;
        private SclLN0 ln0;
        private List<SclLN> lns = new ArrayList<>();

        public SclLDevice(String inst) {
            this.inst = inst;
        }

        public void addLn(SclLN ln) {
            this.lns.add(ln);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclLN0 {

        private String lnType;
        private String lnClass;
        private String inst;
        private String desc;
        private List<SclDataSet> dataSets = new ArrayList<>();
        private List<SclReportControl> reportControls = new ArrayList<>();
        private List<SclLogControl> logControls = new ArrayList<>();
        private List<SclGSEControl> gseControls = new ArrayList<>();
        private List<SclSampledValueControl> sampledValueControls = new ArrayList<>();
        private List<SclDOI> dois = new ArrayList<>();

        public void addDataSet(SclDataSet dataSet) {
            this.dataSets.add(dataSet);
        }

        public void addReportControl(SclReportControl rc) {
            this.reportControls.add(rc);
        }

        public void addLogControl(SclLogControl lc) {
            this.logControls.add(lc);
        }

        public void addGseControl(SclGSEControl gse) {
            this.gseControls.add(gse);
        }

        public void addSampledValueControl(SclSampledValueControl sv) {
            this.sampledValueControls.add(sv);
        }

        public void addDoi(SclDOI doi) {
            this.dois.add(doi);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclLN {

        private String lnType;
        private String lnClass;
        private String inst;
        private String prefix;
        private String desc;
        private List<SclDOI> dois = new ArrayList<>();
        private List<SclInputs> inputs;

        public void addDoi(SclDOI doi) {
            this.dois.add(doi);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclDataSet {

        private String name;
        private String desc;
        private List<SclFCDA> fcdaList = new ArrayList<>();

        public SclDataSet(String name) {
            this.name = name;
        }

        public void addFcda(SclFCDA fcda) {
            this.fcdaList.add(fcda);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclFCDA {

        private String ldInst;
        private String lnClass;
        private String lnInst;
        private String prefix;
        private String doName;
        private String daName;
        private String fc;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclReportControl {

        private String name;
        private String desc;
        private String rptID;
        private String datSet;
        private String confRev;
        private boolean buffered;
        private String bufTime;
        private String intgPd;
        private SclTrgOps trgOps;
        private SclOptFields optFields;
        private SclRptEnabled rptEnabled;

        public SclReportControl(String name) {
            this.name = name;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclTrgOps {

        private boolean dchg;
        private boolean qchg;
        private boolean dupd;
        private boolean period;
        private boolean gi;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclOptFields {

        private boolean dataSet;
        private boolean bufOvfl;
        private boolean configRef;
        private boolean dataRef;
        private boolean entryID;
        private boolean reasonCode;
        private boolean timeStamp;
        private boolean seqNum;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclRptEnabled {

        private int max;
        private List<SclClientLN> clientLNs = new ArrayList<>();

        public void addClientLN(SclClientLN clientLN) {
            this.clientLNs.add(clientLN);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclClientLN {

        private String iedName;
        private String ldInst;
        private String lnClass;
        private String lnInst;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclLogControl {

        private String name;
        private String datSet;
        private String logName;
        private String desc;
        private SclTrgOps trgOps;
    }

    @Data
    @NoArgsConstructor
    public static class SclGSEControl {

        private String name;
        private String datSet;
        private String appID;
        private String confRev;
        private String type;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclSampledValueControl {

        private String name;
        private String datSet;
        private String smvID;
        private int smpRate;
        private int nofASDU;
        private boolean multicast;
        private String confRev;
        private SclSmvOpts smvOpts;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclSmvOpts {

        private boolean sampleRate;
        private boolean refreshTime;
        private boolean sampleSynchronized;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclDOI {

        private String name;
        private String desc;
        private List<SclDAI> dais = new ArrayList<>();
        private List<SclSDI> sdis = new ArrayList<>();

        public void addDai(SclDAI dai) {
            this.dais.add(dai);
        }

        public void addSdi(SclSDI sdi) {
            this.sdis.add(sdi);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclDAI {

        private String name;
        private String value;
        private String sAddr;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclSDI {

        private String name;
        private List<SclDAI> dais = new ArrayList<>();

        public void addDai(SclDAI dai) {
            this.dais.add(dai);
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class SclInputs {

        private List<SclExtRef> extRefs = new ArrayList<>();

        public void addExtRef(SclExtRef extRef) {
            this.extRefs.add(extRef);
        }
    }

    @Data
    @NoArgsConstructor
    public static class SclExtRef {

        private String intAddr;
        private String desc;
        private String iedName;
        private String ldInst;
        private String lnClass;
        private String lnInst;
        private String doName;
        private String daName;
        private String serviceType;
    }
}