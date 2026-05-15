package com.ysh.dlt2811bean.scl2.reader;

import com.ysh.dlt2811bean.scl2.model.*;
import lombok.extern.slf4j.Slf4j;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.*;

@Slf4j
public class SclReader {

    public SclDocument read(String filePath) {
        return read(Paths.get(filePath));
    }

    public SclDocument read(Path filePath) {
        try (InputStream is = new FileInputStream(filePath.toFile())) {
            XMLStreamReader reader = newFactory().createXMLStreamReader(is);
            SclDocument scl = parseDocument(reader);
            scl.setOriginalFilePath(filePath.toString());
            return scl;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SCL file: " + filePath, e);
        }
    }

    public SclDocument read(File file) {
        return read(file.toPath());
    }

    public SclDocument read(InputStream inputStream) {
        try {
            XMLStreamReader reader = newFactory().createXMLStreamReader(inputStream);
            return parseDocument(reader);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse SCL from input stream", e);
        }
    }

    private static XMLInputFactory newFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return factory;
    }

    private SclDocument parseDocument(XMLStreamReader reader) throws Exception {
        SclDocument scl = new SclDocument();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "SCL" -> {
                        scl.setXmlns(getAttr(reader, "xmlns"));
                        scl.setXsiSchemaLocation(getAttr(reader, "xsi:schemaLocation"));
                        String fileTypeStr = getAttr(reader, "fileType");
                        if (!fileTypeStr.isEmpty()) {
                            try {
                                scl.setFileType(SclDocument.SclFileType.valueOf(fileTypeStr));
                            } catch (IllegalArgumentException e) {
                                scl.setFileType(SclDocument.SclFileType.UNKNOWN);
                            }
                        }
                    }
                    case "Header" -> scl.setHeader(parseHeader(reader));
                    case "Substation" -> scl.setSubstation(parseSubstation(reader));
                    case "Communication" -> scl.setCommunication(parseCommunication(reader));
                    case "IED" -> scl.addIed(parseIed(reader));
                    case "DataTypeTemplates" -> scl.setDataTypeTemplates(parseDataTypeTemplates(reader));
                }
            }
        }
        return scl;
    }

    private static String getAttr(XMLStreamReader reader, String name) {
        for (int i = 0; i < reader.getAttributeCount(); i++) {
            if (reader.getAttributeLocalName(i).equals(name)) {
                return reader.getAttributeValue(i);
            }
        }
        return "";
    }

    private SclHeader parseHeader(XMLStreamReader reader) throws Exception {
        SclHeader header = new SclHeader();
        header.setId(getAttr(reader, "id"));
        header.setVersion(getAttr(reader, "version"));
        header.setRevision(getAttr(reader, "revision"));
        header.setToolId(getAttr(reader, "toolID"));
        header.setNameStructure(getAttr(reader, "nameStructure"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("Text".equals(tag)) {
                    header.setText(reader.getElementText());
                } else if ("Hitem".equals(tag)) {
                    header.addHitem(parseHitem(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Header".equals(reader.getLocalName())) break;
            }
        }
        return header;
    }

    private SclHitem parseHitem(XMLStreamReader reader) {
        SclHitem hitem = new SclHitem();
        hitem.setVersion(getAttr(reader, "version"));
        hitem.setRevision(getAttr(reader, "revision"));
        hitem.setWhen(getAttr(reader, "when"));
        hitem.setWho(getAttr(reader, "who"));
        hitem.setWhat(getAttr(reader, "what"));
        hitem.setWhy(getAttr(reader, "why"));
        skipElement(reader);
        return hitem;
    }

    private SclSubstation parseSubstation(XMLStreamReader reader) throws Exception {
        SclSubstation substation = new SclSubstation();
        substation.setName(getAttr(reader, "name"));
        substation.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("VoltageLevel".equals(reader.getLocalName())) {
                    substation.addVoltageLevel(parseVoltageLevel(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Substation".equals(reader.getLocalName())) break;
            }
        }
        return substation;
    }

    private SclVoltageLevel parseVoltageLevel(XMLStreamReader reader) throws Exception {
        SclVoltageLevel vl = new SclVoltageLevel();
        vl.setName(getAttr(reader, "name"));
        vl.setDesc(getAttr(reader, "desc"));
        vl.setVoltage(getAttr(reader, "voltage"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Bay".equals(reader.getLocalName())) {
                    vl.addBay(parseBay(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("VoltageLevel".equals(reader.getLocalName())) break;
            }
        }
        return vl;
    }

    private SclBay parseBay(XMLStreamReader reader) throws Exception {
        SclBay bay = new SclBay();
        bay.setName(getAttr(reader, "name"));
        bay.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ConductingEquipment".equals(reader.getLocalName())) {
                    bay.addEquipment(parseConductingEquipment(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Bay".equals(reader.getLocalName())) break;
            }
        }
        return bay;
    }

    private SclConductingEquipment parseConductingEquipment(XMLStreamReader reader) {
        SclConductingEquipment eq = new SclConductingEquipment();
        eq.setName(getAttr(reader, "name"));
        eq.setDesc(getAttr(reader, "desc"));
        eq.setType(getAttr(reader, "type"));
        skipElement(reader);
        return eq;
    }

    private SclCommunication parseCommunication(XMLStreamReader reader) throws Exception {
        SclCommunication comm = new SclCommunication();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("SubNetwork".equals(reader.getLocalName())) {
                    comm.addSubNetwork(parseSubNetwork(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Communication".equals(reader.getLocalName())) break;
            }
        }
        return comm;
    }

    private SclSubNetwork parseSubNetwork(XMLStreamReader reader) throws Exception {
        SclSubNetwork sn = new SclSubNetwork();
        sn.setName(getAttr(reader, "name"));
        sn.setDesc(getAttr(reader, "desc"));
        sn.setType(getAttr(reader, "type"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ConnectedAP".equals(reader.getLocalName())) {
                    sn.addConnectedAP(parseConnectedAP(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SubNetwork".equals(reader.getLocalName())) break;
            }
        }
        return sn;
    }

    private SclConnectedAP parseConnectedAP(XMLStreamReader reader) throws Exception {
        SclConnectedAP cap = new SclConnectedAP();
        cap.setIedName(getAttr(reader, "iedName"));
        cap.setApName(getAttr(reader, "apName"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "Address" -> parseAddressChildren(reader, cap::addAddress);
                    case "GSE" -> cap.addGse(parseGse(reader));
                    case "SMV" -> cap.addSmv(parseSmv(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("ConnectedAP".equals(reader.getLocalName())) break;
            }
        }
        return cap;
    }

    private void parseAddressChildren(XMLStreamReader reader, java.util.function.Consumer<SclAddress> consumer) throws Exception {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                SclAddress addr = new SclAddress();
                addr.setType(getAttr(reader, "type"));
                addr.setValue(reader.getElementText());
                consumer.accept(addr);
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) break;
            }
        }
    }

    private SclGSE parseGse(XMLStreamReader reader) throws Exception {
        SclGSE gse = new SclGSE();
        gse.setLdInst(getAttr(reader, "ldInst"));
        gse.setCbName(getAttr(reader, "cbName"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) {
                    parseAddressChildren(reader, gse::addAddress);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("GSE".equals(reader.getLocalName())) break;
            }
        }
        return gse;
    }

    private SclSMV parseSmv(XMLStreamReader reader) throws Exception {
        SclSMV smv = new SclSMV();
        smv.setLdInst(getAttr(reader, "ldInst"));
        smv.setCbName(getAttr(reader, "cbName"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) {
                    parseAddressChildren(reader, smv::addAddress);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SMV".equals(reader.getLocalName())) break;
            }
        }
        return smv;
    }

    private SclIED parseIed(XMLStreamReader reader) throws Exception {
        SclIED ied = new SclIED();
        ied.setName(getAttr(reader, "name"));
        ied.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "Services" -> ied.setServices(parseServices(reader));
                    case "AccessPoint" -> ied.addAccessPoint(parseAccessPoint(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("IED".equals(reader.getLocalName())) break;
            }
        }
        return ied;
    }

    private SclServices parseServices(XMLStreamReader reader) throws Exception {
        SclServices services = new SclServices();
        services.setDynAssociation(boolAttr(reader, "dynAssociation"));
        services.setGetDirectory(boolAttr(reader, "getDirectory"));
        services.setGetDataObjectDefinition(boolAttr(reader, "getDataObjectDefinition"));
        services.setGetDataSetValue(boolAttr(reader, "getDataSetValue"));
        services.setDataSetDirectory(boolAttr(reader, "dataSetDirectory"));
        services.setReadWrite(boolAttr(reader, "readWrite"));
        services.setFileHandling(boolAttr(reader, "fileHandling"));
        services.setGetCBValues(boolAttr(reader, "getCBValues"));
        services.setGSEDir(boolAttr(reader, "gSEDir"));
        services.setTimerActivatedControl(boolAttr(reader, "timerActivatedControl"));
        services.setConfDataSetMax(intAttr(reader, "confDataSetMax"));
        services.setConfDataSetMaxAttributes(intAttr(reader, "confDataSetMaxAttributes"));
        services.setConfReportControlMax(intAttr(reader, "confReportControlMax"));
        services.setConfLogControlMax(intAttr(reader, "confLogControlMax"));
        services.setGooseMax(intAttr(reader, "gooseMax"));
        services.setGsseMax(intAttr(reader, "gsseMax"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "ReportSettings" -> services.setReportSettings(parseReportSettings(reader));
                    case "GSESettings" -> services.setGseSettings(parseGseSettings(reader));
                    case "ConfLNsFixPrefix" -> services.setConfLNsFixPrefix(true);
                    case "ConfLNsFixLnInst" -> services.setConfLNsFixLnInst(true);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Services".equals(reader.getLocalName())) break;
            }
        }
        return services;
    }

    private SclReportSettings parseReportSettings(XMLStreamReader reader) {
        SclReportSettings rs = new SclReportSettings();
        rs.setBufTime(getAttr(reader, "bufTime"));
        rs.setCbName(getAttr(reader, "cbName"));
        rs.setRptID(getAttr(reader, "rptID"));
        rs.setDatSet(getAttr(reader, "datSet"));
        rs.setIntgPd(getAttr(reader, "intgPd"));
        rs.setOptFields(getAttr(reader, "optFields"));
        skipElement(reader);
        return rs;
    }

    private SclGSESettings parseGseSettings(XMLStreamReader reader) {
        SclGSESettings gs = new SclGSESettings();
        gs.setAppID(getAttr(reader, "appID"));
        gs.setCbName(getAttr(reader, "cbName"));
        gs.setDatSet(getAttr(reader, "datSet"));
        skipElement(reader);
        return gs;
    }

    private SclAccessPoint parseAccessPoint(XMLStreamReader reader) throws Exception {
        SclAccessPoint ap = new SclAccessPoint();
        ap.setName(getAttr(reader, "name"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Server".equals(reader.getLocalName())) {
                    ap.setServer(parseServer(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("AccessPoint".equals(reader.getLocalName())) break;
            }
        }
        return ap;
    }

    private SclServer parseServer(XMLStreamReader reader) throws Exception {
        SclServer server = new SclServer();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("LDevice".equals(reader.getLocalName())) {
                    server.addLDevice(parseLDevice(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Server".equals(reader.getLocalName())) break;
            }
        }
        return server;
    }

    private SclLDevice parseLDevice(XMLStreamReader reader) throws Exception {
        SclLDevice ld = new SclLDevice();
        ld.setInst(getAttr(reader, "inst"));
        ld.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("LN".equals(tag) || "LN0".equals(tag)) {
                    ld.addLn(parseLn(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LDevice".equals(reader.getLocalName())) break;
            }
        }
        return ld;
    }

    private SclLN parseLn(XMLStreamReader reader) throws Exception {
        SclLN ln = new SclLN();
        ln.setLnClass(getAttr(reader, "lnClass"));
        ln.setInst(getAttr(reader, "inst"));
        ln.setPrefix(getAttr(reader, "prefix"));
        ln.setLnType(getAttr(reader, "lnType"));
        ln.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "DOI" -> ln.addDoi(parseDoi(reader));
                    case "DataSet" -> ln.addDataSet(parseDataSet(reader));
                    case "ReportControl" -> ln.addReportControl(parseReportControl(reader));
                    case "LogControl" -> ln.addLogControl(parseLogControl(reader));
                    case "GSEControl" -> ln.addGseControl(parseGseControl(reader));
                    case "SampledValueControl" -> ln.addSvControl(parseSampledValueControl(reader));
                    case "Inputs" -> ln.addInput(parseInput(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                String tag = reader.getLocalName();
                if ("LN".equals(tag) || "LN0".equals(tag)) break;
            }
        }
        return ln;
    }

    private SclDOI parseDoi(XMLStreamReader reader) throws Exception {
        SclDOI doi = new SclDOI();
        doi.setName(getAttr(reader, "name"));
        doi.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "DAI" -> doi.addDai(parseDai(reader));
                    case "SDI" -> doi.addSdi(parseSdi(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DOI".equals(reader.getLocalName())) break;
            }
        }
        return doi;
    }

    private SclDAI parseDai(XMLStreamReader reader) throws Exception {
        SclDAI dai = new SclDAI();
        dai.setName(getAttr(reader, "name"));
        dai.setFc(getAttr(reader, "fc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Val".equals(reader.getLocalName())) {
                    dai.setVal(reader.getElementText());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DAI".equals(reader.getLocalName())) break;
            }
        }
        return dai;
    }

    private SclSDI parseSdi(XMLStreamReader reader) throws Exception {
        SclSDI sdi = new SclSDI();
        sdi.setName(getAttr(reader, "name"));
        sdi.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "DAI" -> sdi.addDai(parseDai(reader));
                    case "SDI" -> sdi.addSdi(parseSdi(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SDI".equals(reader.getLocalName())) break;
            }
        }
        return sdi;
    }

    private SclDataSet parseDataSet(XMLStreamReader reader) throws Exception {
        SclDataSet ds = new SclDataSet();
        ds.setName(getAttr(reader, "name"));
        ds.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("FCDA".equals(reader.getLocalName())) {
                    ds.addFcda(parseFcda(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DataSet".equals(reader.getLocalName())) break;
            }
        }
        return ds;
    }

    private SclFCDA parseFcda(XMLStreamReader reader) {
        SclFCDA fcda = new SclFCDA();
        fcda.setLdInst(getAttr(reader, "ldInst"));
        fcda.setLnClass(getAttr(reader, "lnClass"));
        fcda.setLnInst(getAttr(reader, "lnInst"));
        fcda.setPrefix(getAttr(reader, "prefix"));
        fcda.setDoName(getAttr(reader, "doName"));
        fcda.setDaName(getAttr(reader, "daName"));
        fcda.setFc(getAttr(reader, "fc"));
        skipElement(reader);
        return fcda;
    }

    private SclReportControl parseReportControl(XMLStreamReader reader) throws Exception {
        SclReportControl rc = new SclReportControl();
        rc.setName(getAttr(reader, "name"));
        rc.setDesc(getAttr(reader, "desc"));
        rc.setDatSet(getAttr(reader, "datSet"));
        rc.setRptID(getAttr(reader, "rptID"));
        rc.setConfRev(getAttr(reader, "confRev"));
        rc.setBuffered(getAttr(reader, "buffered"));
        rc.setBufTime(getAttr(reader, "bufTime"));
        rc.setIntgPd(getAttr(reader, "intgPd"));
        rc.setRptEnabled(getAttr(reader, "rptEnabled"));
        skipElement(reader);
        return rc;
    }

    private SclLogControl parseLogControl(XMLStreamReader reader) throws Exception {
        SclLogControl lc = new SclLogControl();
        lc.setName(getAttr(reader, "name"));
        lc.setDesc(getAttr(reader, "desc"));
        lc.setDatSet(getAttr(reader, "datSet"));
        lc.setLogName(getAttr(reader, "logName"));
        lc.setLogEna(getAttr(reader, "logEna"));
        lc.setReasonCode(getAttr(reader, "reasonCode"));
        skipElement(reader);
        return lc;
    }

    private SclGSEControl parseGseControl(XMLStreamReader reader) throws Exception {
        SclGSEControl gc = new SclGSEControl();
        gc.setName(getAttr(reader, "name"));
        gc.setDesc(getAttr(reader, "desc"));
        gc.setDatSet(getAttr(reader, "datSet"));
        gc.setAppID(getAttr(reader, "appID"));
        gc.setConfRev(getAttr(reader, "confRev"));
        gc.setFixedOffs(getAttr(reader, "fixedOffs"));
        skipElement(reader);
        return gc;
    }

    private SclSampledValueControl parseSampledValueControl(XMLStreamReader reader) throws Exception {
        SclSampledValueControl sv = new SclSampledValueControl();
        sv.setName(getAttr(reader, "name"));
        sv.setDesc(getAttr(reader, "desc"));
        sv.setDatSet(getAttr(reader, "datSet"));
        sv.setSvID(getAttr(reader, "svID"));
        sv.setConfRev(getAttr(reader, "confRev"));
        sv.setSmpRate(getAttr(reader, "smpRate"));
        sv.setNofASDU(getAttr(reader, "nofASDU"));
        skipElement(reader);
        return sv;
    }

    private SclInput parseInput(XMLStreamReader reader) throws Exception {
        SclInput input = new SclInput();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ExtRef".equals(reader.getLocalName())) {
                    input.addExtRef(parseExtRef(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Inputs".equals(reader.getLocalName())) break;
            }
        }
        return input;
    }

    private SclExtRef parseExtRef(XMLStreamReader reader) {
        SclExtRef extRef = new SclExtRef();
        extRef.setIedName(getAttr(reader, "iedName"));
        extRef.setLdInst(getAttr(reader, "ldInst"));
        extRef.setLnClass(getAttr(reader, "lnClass"));
        extRef.setLnInst(getAttr(reader, "lnInst"));
        extRef.setPrefix(getAttr(reader, "prefix"));
        extRef.setDoName(getAttr(reader, "doName"));
        extRef.setDaName(getAttr(reader, "daName"));
        extRef.setServiceType(getAttr(reader, "serviceType"));
        extRef.setSrcLDInst(getAttr(reader, "srcLDInst"));
        extRef.setSrcPrefix(getAttr(reader, "srcPrefix"));
        extRef.setSrcLnClass(getAttr(reader, "srcLnClass"));
        extRef.setSrcLnInst(getAttr(reader, "srcLnInst"));
        extRef.setSrcCBName(getAttr(reader, "srcCBName"));
        skipElement(reader);
        return extRef;
    }

    private SclDataTypeTemplates parseDataTypeTemplates(XMLStreamReader reader) throws Exception {
        SclDataTypeTemplates templates = new SclDataTypeTemplates();
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "LNodeType" -> templates.addLNodeType(parseLNodeType(reader));
                    case "DOType" -> templates.addDoType(parseDoType(reader));
                    case "DAType" -> templates.addDaType(parseDaType(reader));
                    case "EnumType" -> templates.addEnumType(parseEnumType(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DataTypeTemplates".equals(reader.getLocalName())) break;
            }
        }
        return templates;
    }

    private SclLNodeType parseLNodeType(XMLStreamReader reader) throws Exception {
        SclLNodeType lnt = new SclLNodeType();
        lnt.setId(getAttr(reader, "id"));
        lnt.setLnClass(getAttr(reader, "lnClass"));
        lnt.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("DO".equals(reader.getLocalName())) {
                    lnt.addDo(parseDo(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LNodeType".equals(reader.getLocalName())) break;
            }
        }
        return lnt;
    }

    private SclDO parseDo(XMLStreamReader reader) {
        SclDO doDef = new SclDO();
        doDef.setName(getAttr(reader, "name"));
        doDef.setType(getAttr(reader, "type"));
        doDef.setDesc(getAttr(reader, "desc"));
        skipElement(reader);
        return doDef;
    }

    private SclDOType parseDoType(XMLStreamReader reader) throws Exception {
        SclDOType doType = new SclDOType();
        doType.setId(getAttr(reader, "id"));
        doType.setDesc(getAttr(reader, "desc"));
        doType.setCdc(getAttr(reader, "cdc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("DA".equals(reader.getLocalName())) {
                    doType.addDa(parseDa(reader));
                } else if ("SDO".equals(reader.getLocalName())) {
                    doType.addSdo(parseSdo(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DOType".equals(reader.getLocalName())) break;
            }
        }
        return doType;
    }

    private SclSDO parseSdo(XMLStreamReader reader) {
        SclSDO sdo = new SclSDO();
        sdo.setName(getAttr(reader, "name"));
        sdo.setType(getAttr(reader, "type"));
        skipElement(reader);
        return sdo;
    }

    private SclDA parseDa(XMLStreamReader reader) {
        SclDA da = new SclDA();
        da.setName(getAttr(reader, "name"));
        da.setFc(getAttr(reader, "fc"));
        da.setBType(getAttr(reader, "bType"));
        da.setType(getAttr(reader, "type"));
        da.setDesc(getAttr(reader, "desc"));
        da.setValKind(getAttr(reader, "valKind"));
        da.setCount(intAttr(reader, "count"));
        skipElement(reader);
        return da;
    }

    private SclDAType parseDaType(XMLStreamReader reader) throws Exception {
        SclDAType daType = new SclDAType();
        daType.setId(getAttr(reader, "id"));
        daType.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("BDA".equals(reader.getLocalName())) {
                    daType.addBda(parseBda(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DAType".equals(reader.getLocalName())) break;
            }
        }
        return daType;
    }

    private SclBDA parseBda(XMLStreamReader reader) {
        SclBDA bda = new SclBDA();
        bda.setName(getAttr(reader, "name"));
        bda.setBType(getAttr(reader, "bType"));
        bda.setType(getAttr(reader, "type"));
        bda.setDesc(getAttr(reader, "desc"));
        bda.setValKind(getAttr(reader, "valKind"));
        bda.setCount(intAttr(reader, "count"));
        skipElement(reader);
        return bda;
    }

    private SclEnumType parseEnumType(XMLStreamReader reader) throws Exception {
        SclEnumType enumType = new SclEnumType();
        enumType.setId(getAttr(reader, "id"));
        enumType.setDesc(getAttr(reader, "desc"));
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("EnumVal".equals(reader.getLocalName())) {
                    enumType.addEnumVal(parseEnumVal(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("EnumType".equals(reader.getLocalName())) break;
            }
        }
        return enumType;
    }

    private SclEnumVal parseEnumVal(XMLStreamReader reader) throws Exception {
        SclEnumVal ev = new SclEnumVal();
        ev.setOrd(intAttr(reader, "ord"));
        ev.setValue(reader.getElementText());
        return ev;
    }

    private static boolean boolAttr(XMLStreamReader reader, String name) {
        String val = getAttr(reader, name);
        return "true".equalsIgnoreCase(val) || "1".equals(val);
    }

    private static int intAttr(XMLStreamReader reader, String name) {
        String val = getAttr(reader, name);
        if (val.isEmpty()) return 0;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static void skipElement(XMLStreamReader reader) {
        try {
            int depth = 1;
            while (depth > 0 && reader.hasNext()) {
                int event = reader.next();
                if (event == XMLStreamConstants.START_ELEMENT) depth++;
                else if (event == XMLStreamConstants.END_ELEMENT) depth--;
            }
        } catch (Exception e) {
            log.warn("Error skipping element", e);
        }
    }
}