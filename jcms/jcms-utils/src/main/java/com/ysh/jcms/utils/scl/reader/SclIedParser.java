package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.control.*;
import com.ysh.jcms.utils.scl.model.ied.*;
import com.ysh.jcms.utils.scl.model.input.*;
import com.ysh.jcms.utils.scl.model.instance.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

public class SclIedParser {

    public static SclIED parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclIED ied = new SclIED();
        ied.name(getAttr(reader, "name"));
        ied.desc(getAttr(reader, "desc"));
        ied.type(getAttr(reader, "type"));
        ied.manufacturer(getAttr(reader, "manufacturer"));
        ied.configVersion(getAttr(reader, "configVersion"));
        ied.originalSclVersion(getAttr(reader, "originalSclVersion"));
        ied.originalSclRevision(getAttr(reader, "originalSclRevision"));
        ied.originalSclRelease(intAttr(reader, "originalSclRelease"));
        ied.engRight(getAttr(reader, "engRight"));
        ied.owner(getAttr(reader, "owner"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Services" :
                        ied.services(parseServices(reader));
                        break;
                    case "AccessPoint" :
                        ied.addAccessPoint(parseAccessPoint(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ied;
    }

    // ==================== Services ====================

    private static SclServices parseServices(XMLStreamReader reader) throws XMLStreamException {
        SclServices services = new SclServices();

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "DynAssociation" :
                        services.dynAssociation(true);
                        skipElement(reader);
                        break;
                    case "GetDirectory" :
                        services.getDirectory(true);
                        skipElement(reader);
                        break;
                    case "GetDataObjectDefinition" :
                        services.getDataObjectDefinition(true);
                        skipElement(reader);
                        break;
                    case "GetDataSetValue" :
                        services.getDataSetValue(true);
                        skipElement(reader);
                        break;
                    case "DataSetDirectory" :
                        services.dataSetDirectory(true);
                        skipElement(reader);
                        break;
                    case "ReadWrite" :
                        services.readWrite(true);
                        skipElement(reader);
                        break;
                    case "FileHandling" :
                        services.fileHandling(true);
                        skipElement(reader);
                        break;
                    case "GetCBValues" :
                        services.getCBValues(true);
                        skipElement(reader);
                        break;
                    case "GSEDir" :
                        services.gSEDir(true);
                        skipElement(reader);
                        break;
                    case "TimerActivatedControl" :
                        services.timerActivatedControl(true);
                        skipElement(reader);
                        break;
                    case "ConfDataSet" :
                        services.confDataSetMax(intAttr(reader, "max"));
                        services.confDataSetMaxAttributes(intAttr(reader, "maxAttributes"));
                        skipElement(reader);
                        break;
                    case "ConfReportControl" :
                        services.confReportControlMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "ConfLogControl" :
                        services.confLogControlMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "GOOSE" :
                        services.gooseMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "GSSE" :
                        services.gsseMax(intAttr(reader, "max"));
                        skipElement(reader);
                        break;
                    case "ConfLNs" :
                        services.confLNsFixPrefix(boolAttr(reader, "fixPrefix"));
                        services.confLNsFixLnInst(boolAttr(reader, "fixLnInst"));
                        skipElement(reader);
                        break;
                    case "ReportSettings" :
                        services.reportSettings(parseReportSettings(reader));
                        break;
                    case "GSESettings" :
                        services.gseSettings(parseGseSettings(reader));
                        break;
                    case "LogSettings" :
                    case "SMVSettings" :
                    case "SettingGroups" :
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return services;
    }

    private static SclReportSettings parseReportSettings(XMLStreamReader reader) throws XMLStreamException {
        SclReportSettings rs = new SclReportSettings();
        rs.bufTime(getAttr(reader, "bufTime"));
        rs.cbName(getAttr(reader, "cbName"));
        rs.rptID(getAttr(reader, "rptID"));
        rs.datSet(getAttr(reader, "datSet"));
        rs.intgPd(getAttr(reader, "intgPd"));
        rs.optFields(getAttr(reader, "optFields"));
        skipElement(reader);
        return rs;
    }

    private static SclGSESettings parseGseSettings(XMLStreamReader reader) throws XMLStreamException {
        SclGSESettings gs = new SclGSESettings();
        gs.appID(getAttr(reader, "appID"));
        gs.cbName(getAttr(reader, "cbName"));
        gs.datSet(getAttr(reader, "datSet"));
        skipElement(reader);
        return gs;
    }

    // ==================== AccessPoint ====================

    private static SclAccessPoint parseAccessPoint(XMLStreamReader reader) throws XMLStreamException {
        SclAccessPoint ap = new SclAccessPoint();
        ap.name(getAttr(reader, "name"));
        ap.router(boolAttr(reader, "router"));
        ap.clock(boolAttr(reader, "clock"));
        ap.kdc(boolAttr(reader, "kdc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Server" :
                        ap.server(parseServer(reader));
                        break;
                    case "ServerAt" :
                        ap.serverAt(parseServerAt(reader));
                        break;
                    case "Services" :
                        // AccessPoint-level services (overrides IED-level)
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ap;
    }

    private static SclServerAt parseServerAt(XMLStreamReader reader) throws XMLStreamException {
        SclServerAt sa = new SclServerAt();
        sa.apName(getAttr(reader, "apName"));
        skipElement(reader);
        return sa;
    }

    // ==================== Server ====================

    private static SclServer parseServer(XMLStreamReader reader) throws XMLStreamException {
        SclServer server = new SclServer();
        server.timeout(intAttr(reader, "timeout"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Authentication" :
                        skipElement(reader);
                        break;
                    case "LDevice" :
                        server.addLDevice(parseLDevice(reader));
                        break;
                    case "Association" :
                        server.addAssociation(parseAssociation(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return server;
    }

    private static SclAssociation parseAssociation(XMLStreamReader reader) throws XMLStreamException {
        SclAssociation assoc = new SclAssociation();
        assoc.iedName(getAttr(reader, "iedName"));
        assoc.ldInst(getAttr(reader, "ldInst"));
        assoc.prefix(getAttr(reader, "prefix"));
        assoc.lnClass(getAttr(reader, "lnClass"));
        assoc.lnInst(getAttr(reader, "lnInst"));
        assoc.kind(getAttr(reader, "kind"));
        assoc.associationID(getAttr(reader, "associationID"));
        skipElement(reader);
        return assoc;
    }

    // ==================== LDevice ====================

    private static SclLDevice parseLDevice(XMLStreamReader reader) throws XMLStreamException {
        SclLDevice ld = new SclLDevice();
        ld.inst(getAttr(reader, "inst"));
        ld.desc(getAttr(reader, "desc"));
        ld.ldName(getAttr(reader, "ldName"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LN0" :
                        ld.addLn(parseLN0(reader));
                        break;
                    case "LN" :
                        ld.addLn(parseLN(reader));
                        break;
                    case "AccessControl" :
                        ld.accessControl(new SclAccessControl());
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ld;
    }

    // ==================== LN0 ====================

    private static SclLN parseLN0(XMLStreamReader reader) throws XMLStreamException {
        SclLN ln = parseLN(reader);
        // LN0 has additional elements: GSEControl, SampledValueControl, SettingControl
        // These are parsed inside parseLN -> parseAnyLN
        return ln;
    }

    // ==================== LN ====================

    private static SclLN parseLN(XMLStreamReader reader) throws XMLStreamException {
        SclLN ln = new SclLN();
        ln.lnType(getAttr(reader, "lnType"));
        ln.prefix(getAttr(reader, "prefix"));
        ln.lnClass(getAttr(reader, "lnClass"));
        ln.inst(getAttr(reader, "inst"));
        ln.desc(getAttr(reader, "desc"));

        parseAnyLN(reader, ln);
        return ln;
    }

    private static void parseAnyLN(XMLStreamReader reader, SclLN ln) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "DataSet" :
                        ln.addDataSet(parseDataSet(reader));
                        break;
                    case "ReportControl" :
                        ln.addReportControl(parseReportControl(reader));
                        break;
                    case "LogControl" :
                        ln.addLogControl(parseLogControl(reader));
                        break;
                    case "DOI" :
                        ln.addDoi(parseDOI(reader));
                        break;
                    case "Inputs" :
                        ln.addInput(parseInputs(reader));
                        break;
                    case "GSEControl" :
                        ln.addGseControl(parseGSEControl(reader));
                        break;
                    case "SampledValueControl" :
                        ln.addSvControl(parseSampledValueControl(reader));
                        break;
                    case "Log" :
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    // ==================== DataSet / FCDA ====================

    private static SclDataSet parseDataSet(XMLStreamReader reader) throws XMLStreamException {
        SclDataSet ds = new SclDataSet();
        ds.name(getAttr(reader, "name"));
        ds.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("FCDA".equals(reader.getLocalName())) {
                    ds.addFcda(parseFCDA(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ds;
    }

    private static SclFCDA parseFCDA(XMLStreamReader reader) throws XMLStreamException {
        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(getAttr(reader, "ldInst"));
        fcda.prefix(getAttr(reader, "prefix"));
        fcda.lnClass(getAttr(reader, "lnClass"));
        fcda.lnInst(getAttr(reader, "lnInst"));
        fcda.doName(getAttr(reader, "doName"));
        fcda.daName(getAttr(reader, "daName"));
        fcda.fc(getAttr(reader, "fc"));
        fcda.ix(intAttr(reader, "ix"));
        skipElement(reader);
        return fcda;
    }

    // ==================== ReportControl ====================

    private static SclReportControl parseReportControl(XMLStreamReader reader) throws XMLStreamException {
        SclReportControl rc = new SclReportControl();
        rc.name(getAttr(reader, "name"));
        rc.desc(getAttr(reader, "desc"));
        rc.rptID(getAttr(reader, "rptID"));
        rc.datSet(getAttr(reader, "datSet"));
        rc.confRev(getAttr(reader, "confRev"));
        rc.buffered(getAttr(reader, "buffered"));
        rc.bufTime(getAttr(reader, "bufTime"));
        rc.intgPd(getAttr(reader, "intgPd"));
        rc.indexed(getAttr(reader, "indexed"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "TrgOps" :
                    case "OptFields" :
                    case "RptEnabled" :
                        skipElement(reader);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return rc;
    }

    // ==================== LogControl ====================

    private static SclLogControl parseLogControl(XMLStreamReader reader) throws XMLStreamException {
        SclLogControl lc = new SclLogControl();
        lc.name(getAttr(reader, "name"));
        lc.desc(getAttr(reader, "desc"));
        lc.logName(getAttr(reader, "logName"));
        lc.datSet(getAttr(reader, "datSet"));
        lc.confRev(getAttr(reader, "confRev"));
        lc.intgPd(getAttr(reader, "intgPd"));
        lc.logEna(getAttr(reader, "logEna"));
        lc.reasonCode(getAttr(reader, "reasonCode"));
        skipElement(reader);
        return lc;
    }

    // ==================== GSEControl ====================

    private static SclGSEControl parseGSEControl(XMLStreamReader reader) throws XMLStreamException {
        SclGSEControl gse = new SclGSEControl();
        gse.name(getAttr(reader, "name"));
        gse.desc(getAttr(reader, "desc"));
        gse.appID(getAttr(reader, "appID"));
        gse.datSet(getAttr(reader, "datSet"));
        gse.confRev(getAttr(reader, "confRev"));
        gse.fixedOffs(getAttr(reader, "fixedOffs"));
        gse.type(getAttr(reader, "type"));
        gse.securityEnable(getAttr(reader, "securityEnable"));
        skipElement(reader);
        return gse;
    }

    // ==================== SampledValueControl ====================

    private static SclSampledValueControl parseSampledValueControl(XMLStreamReader reader) throws XMLStreamException {
        SclSampledValueControl sv = new SclSampledValueControl();
        sv.name(getAttr(reader, "name"));
        sv.desc(getAttr(reader, "desc"));
        sv.svID(getAttr(reader, "smvID"));
        sv.datSet(getAttr(reader, "datSet"));
        sv.confRev(getAttr(reader, "confRev"));
        sv.smpRate(getAttr(reader, "smpRate"));
        sv.nofASDU(getAttr(reader, "nofASDU"));
        sv.multicast(getAttr(reader, "multicast"));
        sv.securityEnable(getAttr(reader, "securityEnable"));
        skipElement(reader);
        return sv;
    }

    // ==================== Inputs / ExtRef ====================

    private static SclInput parseInputs(XMLStreamReader reader) throws XMLStreamException {
        SclInput inputs = new SclInput();

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ExtRef".equals(reader.getLocalName())) {
                    inputs.addExtRef(parseExtRef(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return inputs;
    }

    private static SclExtRef parseExtRef(XMLStreamReader reader) throws XMLStreamException {
        SclExtRef extRef = new SclExtRef();
        extRef.desc(getAttr(reader, "desc"));
        extRef.iedName(getAttr(reader, "iedName"));
        extRef.ldInst(getAttr(reader, "ldInst"));
        extRef.prefix(getAttr(reader, "prefix"));
        extRef.lnClass(getAttr(reader, "lnClass"));
        extRef.lnInst(getAttr(reader, "lnInst"));
        extRef.doName(getAttr(reader, "doName"));
        extRef.daName(getAttr(reader, "daName"));
        extRef.intAddr(getAttr(reader, "intAddr"));
        extRef.serviceType(getAttr(reader, "serviceType"));
        extRef.srcLDInst(getAttr(reader, "srcLDInst"));
        extRef.srcPrefix(getAttr(reader, "srcPrefix"));
        extRef.srcLnClass(getAttr(reader, "srcLNClass"));
        extRef.srcLnInst(getAttr(reader, "srcLNInst"));
        extRef.srcCBName(getAttr(reader, "srcCBName"));
        extRef.pServT(getAttr(reader, "pServT"));
        extRef.pLN(getAttr(reader, "pLN"));
        extRef.pDO(getAttr(reader, "pDO"));
        extRef.pDA(getAttr(reader, "pDA"));
        skipElement(reader);
        return extRef;
    }

    // ==================== DOI / SDI / DAI ====================

    private static SclDOI parseDOI(XMLStreamReader reader) throws XMLStreamException {
        SclDOI doi = new SclDOI();
        doi.name(getAttr(reader, "name"));
        doi.desc(getAttr(reader, "desc"));
        doi.ix(intAttr(reader, "ix"));
        doi.accessControl(getAttr(reader, "accessControl"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SDI" :
                        doi.addSdi(parseSDI(reader));
                        break;
                    case "DAI" :
                        doi.addDai(parseDAI(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return doi;
    }

    private static SclSDI parseSDI(XMLStreamReader reader) throws XMLStreamException {
        SclSDI sdi = new SclSDI();
        sdi.name(getAttr(reader, "name"));
        sdi.desc(getAttr(reader, "desc"));
        sdi.ix(intAttr(reader, "ix"));
        sdi.sAddr(getAttr(reader, "sAddr"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SDI" :
                        sdi.addSdi(parseSDI(reader));
                        break;
                    case "DAI" :
                        sdi.addDai(parseDAI(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return sdi;
    }

    private static SclDAI parseDAI(XMLStreamReader reader) throws XMLStreamException {
        SclDAI dai = new SclDAI();
        dai.name(getAttr(reader, "name"));
        dai.sAddr(getAttr(reader, "sAddr"));
        dai.valKind(getAttr(reader, "valKind"));
        dai.ix(intAttr(reader, "ix"));
        dai.valImport(boolAttr(reader, "valImport"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Val".equals(reader.getLocalName())) {
                    dai.addVal(SclReader.parseValChild(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return dai;
    }
}
