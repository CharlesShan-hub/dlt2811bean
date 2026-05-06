package com.ysh.dlt2811bean.scl.model;

import java.util.ArrayList;
import java.util.List;

public class SclIED {

    private String name;
    private String desc;
    private SclServices services;
    private List<SclAccessPoint> accessPoints = new ArrayList<>();

    public SclIED() {
    }

    public SclIED(String name) {
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

    public SclServices getServices() {
        return services;
    }

    public void setServices(SclServices services) {
        this.services = services;
    }

    public List<SclAccessPoint> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<SclAccessPoint> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public void addAccessPoint(SclAccessPoint accessPoint) {
        this.accessPoints.add(accessPoint);
    }

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

        public SclServices() {
        }

        public boolean isDynAssociation() {
            return dynAssociation;
        }

        public void setDynAssociation(boolean dynAssociation) {
            this.dynAssociation = dynAssociation;
        }

        public boolean isGetDirectory() {
            return getDirectory;
        }

        public void setGetDirectory(boolean getDirectory) {
            this.getDirectory = getDirectory;
        }

        public boolean isGetDataObjectDefinition() {
            return getDataObjectDefinition;
        }

        public void setGetDataObjectDefinition(boolean getDataObjectDefinition) {
            this.getDataObjectDefinition = getDataObjectDefinition;
        }

        public boolean isGetDataSetValue() {
            return getDataSetValue;
        }

        public void setGetDataSetValue(boolean getDataSetValue) {
            this.getDataSetValue = getDataSetValue;
        }

        public boolean isDataSetDirectory() {
            return dataSetDirectory;
        }

        public void setDataSetDirectory(boolean dataSetDirectory) {
            this.dataSetDirectory = dataSetDirectory;
        }

        public boolean isReadWrite() {
            return readWrite;
        }

        public void setReadWrite(boolean readWrite) {
            this.readWrite = readWrite;
        }

        public boolean isFileHandling() {
            return fileHandling;
        }

        public void setFileHandling(boolean fileHandling) {
            this.fileHandling = fileHandling;
        }

        public boolean isGetCBValues() {
            return getCBValues;
        }

        public void setGetCBValues(boolean getCBValues) {
            this.getCBValues = getCBValues;
        }

        public boolean isGSEDir() {
            return gSEDir;
        }

        public void setGSEDir(boolean gSEDir) {
            this.gSEDir = gSEDir;
        }

        public boolean isTimerActivatedControl() {
            return timerActivatedControl;
        }

        public void setTimerActivatedControl(boolean timerActivatedControl) {
            this.timerActivatedControl = timerActivatedControl;
        }

        public Integer getConfDataSetMax() {
            return confDataSetMax;
        }

        public void setConfDataSetMax(Integer confDataSetMax) {
            this.confDataSetMax = confDataSetMax;
        }

        public Integer getConfDataSetMaxAttributes() {
            return confDataSetMaxAttributes;
        }

        public void setConfDataSetMaxAttributes(Integer confDataSetMaxAttributes) {
            this.confDataSetMaxAttributes = confDataSetMaxAttributes;
        }

        public Integer getConfReportControlMax() {
            return confReportControlMax;
        }

        public void setConfReportControlMax(Integer confReportControlMax) {
            this.confReportControlMax = confReportControlMax;
        }

        public Integer getConfLogControlMax() {
            return confLogControlMax;
        }

        public void setConfLogControlMax(Integer confLogControlMax) {
            this.confLogControlMax = confLogControlMax;
        }

        public Integer getGooseMax() {
            return gooseMax;
        }

        public void setGooseMax(Integer gooseMax) {
            this.gooseMax = gooseMax;
        }

        public Integer getGsseMax() {
            return gsseMax;
        }

        public void setGsseMax(Integer gsseMax) {
            this.gsseMax = gsseMax;
        }

        public Boolean getConfLNsFixPrefix() {
            return confLNsFixPrefix;
        }

        public void setConfLNsFixPrefix(Boolean confLNsFixPrefix) {
            this.confLNsFixPrefix = confLNsFixPrefix;
        }

        public Boolean getConfLNsFixLnInst() {
            return confLNsFixLnInst;
        }

        public void setConfLNsFixLnInst(Boolean confLNsFixLnInst) {
            this.confLNsFixLnInst = confLNsFixLnInst;
        }

        public SclReportSettings getReportSettings() {
            return reportSettings;
        }

        public void setReportSettings(SclReportSettings reportSettings) {
            this.reportSettings = reportSettings;
        }

        public SclGSESettings getGseSettings() {
            return gseSettings;
        }

        public void setGseSettings(SclGSESettings gseSettings) {
            this.gseSettings = gseSettings;
        }
    }

    public static class SclReportSettings {

        private String bufTime;
        private String cbName;
        private String rptID;
        private String datSet;
        private String intgPd;
        private String optFields;

        public SclReportSettings() {
        }

        public String getBufTime() {
            return bufTime;
        }

        public void setBufTime(String bufTime) {
            this.bufTime = bufTime;
        }

        public String getCbName() {
            return cbName;
        }

        public void setCbName(String cbName) {
            this.cbName = cbName;
        }

        public String getRptID() {
            return rptID;
        }

        public void setRptID(String rptID) {
            this.rptID = rptID;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }

        public String getIntgPd() {
            return intgPd;
        }

        public void setIntgPd(String intgPd) {
            this.intgPd = intgPd;
        }

        public String getOptFields() {
            return optFields;
        }

        public void setOptFields(String optFields) {
            this.optFields = optFields;
        }
    }

    public static class SclGSESettings {

        private String appID;
        private String cbName;
        private String datSet;

        public SclGSESettings() {
        }

        public String getAppID() {
            return appID;
        }

        public void setAppID(String appID) {
            this.appID = appID;
        }

        public String getCbName() {
            return cbName;
        }

        public void setCbName(String cbName) {
            this.cbName = cbName;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }
    }

    public static class SclAccessPoint {

        private String name;
        private SclServer server;

        public SclAccessPoint() {
        }

        public SclAccessPoint(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SclServer getServer() {
            return server;
        }

        public void setServer(SclServer server) {
            this.server = server;
        }
    }

    public static class SclServer {

        private List<SclLDevice> lDevices = new ArrayList<>();

        public SclServer() {
        }

        public List<SclLDevice> getLDevices() {
            return lDevices;
        }

        public void setLDevices(List<SclLDevice> lDevices) {
            this.lDevices = lDevices;
        }

        public void addLDevice(SclLDevice lDevice) {
            this.lDevices.add(lDevice);
        }
    }

    public static class SclLDevice {

        private String inst;
        private String desc;
        private SclLN0 ln0;
        private List<SclLN> lns = new ArrayList<>();

        public SclLDevice() {
        }

        public SclLDevice(String inst) {
            this.inst = inst;
        }

        public String getInst() {
            return inst;
        }

        public void setInst(String inst) {
            this.inst = inst;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public SclLN0 getLn0() {
            return ln0;
        }

        public void setLn0(SclLN0 ln0) {
            this.ln0 = ln0;
        }

        public List<SclLN> getLns() {
            return lns;
        }

        public void setLns(List<SclLN> lns) {
            this.lns = lns;
        }

        public void addLn(SclLN ln) {
            this.lns.add(ln);
        }
    }

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

        public SclLN0() {
        }

        public String getLnType() {
            return lnType;
        }

        public void setLnType(String lnType) {
            this.lnType = lnType;
        }

        public String getLnClass() {
            return lnClass;
        }

        public void setLnClass(String lnClass) {
            this.lnClass = lnClass;
        }

        public String getInst() {
            return inst;
        }

        public void setInst(String inst) {
            this.inst = inst;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public List<SclDataSet> getDataSets() {
            return dataSets;
        }

        public void setDataSets(List<SclDataSet> dataSets) {
            this.dataSets = dataSets;
        }

        public void addDataSet(SclDataSet dataSet) {
            this.dataSets.add(dataSet);
        }

        public List<SclReportControl> getReportControls() {
            return reportControls;
        }

        public void setReportControls(List<SclReportControl> reportControls) {
            this.reportControls = reportControls;
        }

        public void addReportControl(SclReportControl rc) {
            this.reportControls.add(rc);
        }

        public List<SclLogControl> getLogControls() {
            return logControls;
        }

        public void setLogControls(List<SclLogControl> logControls) {
            this.logControls = logControls;
        }

        public void addLogControl(SclLogControl lc) {
            this.logControls.add(lc);
        }

        public List<SclGSEControl> getGseControls() {
            return gseControls;
        }

        public void setGseControls(List<SclGSEControl> gseControls) {
            this.gseControls = gseControls;
        }

        public void addGseControl(SclGSEControl gse) {
            this.gseControls.add(gse);
        }

        public List<SclSampledValueControl> getSampledValueControls() {
            return sampledValueControls;
        }

        public void setSampledValueControls(List<SclSampledValueControl> sampledValueControls) {
            this.sampledValueControls = sampledValueControls;
        }

        public void addSampledValueControl(SclSampledValueControl sv) {
            this.sampledValueControls.add(sv);
        }

        public List<SclDOI> getDois() {
            return dois;
        }

        public void setDois(List<SclDOI> dois) {
            this.dois = dois;
        }

        public void addDoi(SclDOI doi) {
            this.dois.add(doi);
        }
    }

    public static class SclLN {

        private String lnType;
        private String lnClass;
        private String inst;
        private String prefix;
        private String desc;
        private List<SclDOI> dois = new ArrayList<>();
        private List<SclInputs> inputs;

        public SclLN() {
        }

        public String getLnType() {
            return lnType;
        }

        public void setLnType(String lnType) {
            this.lnType = lnType;
        }

        public String getLnClass() {
            return lnClass;
        }

        public void setLnClass(String lnClass) {
            this.lnClass = lnClass;
        }

        public String getInst() {
            return inst;
        }

        public void setInst(String inst) {
            this.inst = inst;
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

        public List<SclDOI> getDois() {
            return dois;
        }

        public void setDois(List<SclDOI> dois) {
            this.dois = dois;
        }

        public void addDoi(SclDOI doi) {
            this.dois.add(doi);
        }

        public List<SclInputs> getInputs() {
            return inputs;
        }

        public void setInputs(List<SclInputs> inputs) {
            this.inputs = inputs;
        }
    }

    public static class SclDataSet {

        private String name;
        private String desc;
        private List<SclFCDA> fcdaList = new ArrayList<>();

        public SclDataSet() {
        }

        public SclDataSet(String name) {
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

        public List<SclFCDA> getFcdaList() {
            return fcdaList;
        }

        public void setFcdaList(List<SclFCDA> fcdaList) {
            this.fcdaList = fcdaList;
        }

        public void addFcda(SclFCDA fcda) {
            this.fcdaList.add(fcda);
        }
    }

    public static class SclFCDA {

        private String ldInst;
        private String lnClass;
        private String lnInst;
        private String prefix;
        private String doName;
        private String daName;
        private String fc;

        public SclFCDA() {
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

        public String getDoName() {
            return doName;
        }

        public void setDoName(String doName) {
            this.doName = doName;
        }

        public String getDaName() {
            return daName;
        }

        public void setDaName(String daName) {
            this.daName = daName;
        }

        public String getFc() {
            return fc;
        }

        public void setFc(String fc) {
            this.fc = fc;
        }
    }

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

        public SclReportControl() {
        }

        public SclReportControl(String name) {
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

        public String getRptID() {
            return rptID;
        }

        public void setRptID(String rptID) {
            this.rptID = rptID;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }

        public String getConfRev() {
            return confRev;
        }

        public void setConfRev(String confRev) {
            this.confRev = confRev;
        }

        public boolean isBuffered() {
            return buffered;
        }

        public void setBuffered(boolean buffered) {
            this.buffered = buffered;
        }

        public String getBufTime() {
            return bufTime;
        }

        public void setBufTime(String bufTime) {
            this.bufTime = bufTime;
        }

        public String getIntgPd() {
            return intgPd;
        }

        public void setIntgPd(String intgPd) {
            this.intgPd = intgPd;
        }

        public SclTrgOps getTrgOps() {
            return trgOps;
        }

        public void setTrgOps(SclTrgOps trgOps) {
            this.trgOps = trgOps;
        }

        public SclOptFields getOptFields() {
            return optFields;
        }

        public void setOptFields(SclOptFields optFields) {
            this.optFields = optFields;
        }

        public SclRptEnabled getRptEnabled() {
            return rptEnabled;
        }

        public void setRptEnabled(SclRptEnabled rptEnabled) {
            this.rptEnabled = rptEnabled;
        }
    }

    public static class SclTrgOps {

        private boolean dchg;
        private boolean qchg;
        private boolean dupd;
        private boolean period;
        private boolean gi;

        public SclTrgOps() {
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

        public boolean isPeriod() {
            return period;
        }

        public void setPeriod(boolean period) {
            this.period = period;
        }

        public boolean isGi() {
            return gi;
        }

        public void setGi(boolean gi) {
            this.gi = gi;
        }
    }

    public static class SclOptFields {

        private boolean dataSet;
        private boolean bufOvfl;
        private boolean configRef;
        private boolean dataRef;
        private boolean entryID;
        private boolean reasonCode;
        private boolean timeStamp;
        private boolean seqNum;

        public SclOptFields() {
        }

        public boolean isDataSet() {
            return dataSet;
        }

        public void setDataSet(boolean dataSet) {
            this.dataSet = dataSet;
        }

        public boolean isBufOvfl() {
            return bufOvfl;
        }

        public void setBufOvfl(boolean bufOvfl) {
            this.bufOvfl = bufOvfl;
        }

        public boolean isConfigRef() {
            return configRef;
        }

        public void setConfigRef(boolean configRef) {
            this.configRef = configRef;
        }

        public boolean isDataRef() {
            return dataRef;
        }

        public void setDataRef(boolean dataRef) {
            this.dataRef = dataRef;
        }

        public boolean isEntryID() {
            return entryID;
        }

        public void setEntryID(boolean entryID) {
            this.entryID = entryID;
        }

        public boolean isReasonCode() {
            return reasonCode;
        }

        public void setReasonCode(boolean reasonCode) {
            this.reasonCode = reasonCode;
        }

        public boolean isTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(boolean timeStamp) {
            this.timeStamp = timeStamp;
        }

        public boolean isSeqNum() {
            return seqNum;
        }

        public void setSeqNum(boolean seqNum) {
            this.seqNum = seqNum;
        }
    }

    public static class SclRptEnabled {

        private int max;
        private List<SclClientLN> clientLNs = new ArrayList<>();

        public SclRptEnabled() {
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public List<SclClientLN> getClientLNs() {
            return clientLNs;
        }

        public void setClientLNs(List<SclClientLN> clientLNs) {
            this.clientLNs = clientLNs;
        }

        public void addClientLN(SclClientLN clientLN) {
            this.clientLNs.add(clientLN);
        }
    }

    public static class SclClientLN {

        private String iedName;
        private String ldInst;
        private String lnClass;
        private String lnInst;

        public SclClientLN() {
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
    }

    public static class SclLogControl {

        private String name;
        private String datSet;
        private String logName;
        private String desc;
        private SclTrgOps trgOps;

        public SclLogControl() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }

        public String getLogName() {
            return logName;
        }

        public void setLogName(String logName) {
            this.logName = logName;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public SclTrgOps getTrgOps() {
            return trgOps;
        }

        public void setTrgOps(SclTrgOps trgOps) {
            this.trgOps = trgOps;
        }
    }

    public static class SclGSEControl {

        private String name;
        private String datSet;
        private String appID;
        private String confRev;
        private String type;

        public SclGSEControl() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }

        public String getAppID() {
            return appID;
        }

        public void setAppID(String appID) {
            this.appID = appID;
        }

        public String getConfRev() {
            return confRev;
        }

        public void setConfRev(String confRev) {
            this.confRev = confRev;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class SclSampledValueControl {

        private String name;
        private String datSet;
        private String smvID;
        private int smpRate;
        private int nofASDU;
        private boolean multicast;
        private String confRev;
        private SclSmvOpts smvOpts;

        public SclSampledValueControl() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDatSet() {
            return datSet;
        }

        public void setDatSet(String datSet) {
            this.datSet = datSet;
        }

        public String getSmvID() {
            return smvID;
        }

        public void setSmvID(String smvID) {
            this.smvID = smvID;
        }

        public int getSmpRate() {
            return smpRate;
        }

        public void setSmpRate(int smpRate) {
            this.smpRate = smpRate;
        }

        public int getNofASDU() {
            return nofASDU;
        }

        public void setNofASDU(int nofASDU) {
            this.nofASDU = nofASDU;
        }

        public boolean isMulticast() {
            return multicast;
        }

        public void setMulticast(boolean multicast) {
            this.multicast = multicast;
        }

        public String getConfRev() {
            return confRev;
        }

        public void setConfRev(String confRev) {
            this.confRev = confRev;
        }

        public SclSmvOpts getSmvOpts() {
            return smvOpts;
        }

        public void setSmvOpts(SclSmvOpts smvOpts) {
            this.smvOpts = smvOpts;
        }
    }

    public static class SclSmvOpts {

        private boolean sampleRate;
        private boolean refreshTime;
        private boolean sampleSynchronized;

        public SclSmvOpts() {
        }

        public boolean isSampleRate() {
            return sampleRate;
        }

        public void setSampleRate(boolean sampleRate) {
            this.sampleRate = sampleRate;
        }

        public boolean isRefreshTime() {
            return refreshTime;
        }

        public void setRefreshTime(boolean refreshTime) {
            this.refreshTime = refreshTime;
        }

        public boolean isSampleSynchronized() {
            return sampleSynchronized;
        }

        public void setSampleSynchronized(boolean sampleSynchronized) {
            this.sampleSynchronized = sampleSynchronized;
        }
    }

    public static class SclDOI {

        private String name;
        private String desc;
        private List<SclDAI> dais = new ArrayList<>();
        private List<SclSDI> sdis = new ArrayList<>();

        public SclDOI() {
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

        public List<SclDAI> getDais() {
            return dais;
        }

        public void setDais(List<SclDAI> dais) {
            this.dais = dais;
        }

        public void addDai(SclDAI dai) {
            this.dais.add(dai);
        }

        public List<SclSDI> getSdis() {
            return sdis;
        }

        public void setSdis(List<SclSDI> sdis) {
            this.sdis = sdis;
        }

        public void addSdi(SclSDI sdi) {
            this.sdis.add(sdi);
        }
    }

    public static class SclDAI {

        private String name;
        private String value;
        private String sAddr;

        public SclDAI() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getSAddr() {
            return sAddr;
        }

        public void setSAddr(String sAddr) {
            this.sAddr = sAddr;
        }
    }

    public static class SclSDI {

        private String name;
        private List<SclDAI> dais = new ArrayList<>();

        public SclSDI() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<SclDAI> getDais() {
            return dais;
        }

        public void setDais(List<SclDAI> dais) {
            this.dais = dais;
        }

        public void addDai(SclDAI dai) {
            this.dais.add(dai);
        }
    }

    public static class SclInputs {

        private List<SclExtRef> extRefs = new ArrayList<>();

        public SclInputs() {
        }

        public List<SclExtRef> getExtRefs() {
            return extRefs;
        }

        public void setExtRefs(List<SclExtRef> extRefs) {
            this.extRefs = extRefs;
        }

        public void addExtRef(SclExtRef extRef) {
            this.extRefs.add(extRef);
        }
    }

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

        public SclExtRef() {
        }

        public String getIntAddr() {
            return intAddr;
        }

        public void setIntAddr(String intAddr) {
            this.intAddr = intAddr;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
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

        public String getDoName() {
            return doName;
        }

        public void setDoName(String doName) {
            this.doName = doName;
        }

        public String getDaName() {
            return daName;
        }

        public void setDaName(String daName) {
            this.daName = daName;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }
    }
}