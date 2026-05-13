package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.*;
import lombok.extern.slf4j.Slf4j;
import com.ysh.dlt2811bean.scl.model.SclCommunication.*;
import com.ysh.dlt2811bean.scl.model.SclIED.*;
import com.ysh.dlt2811bean.scl.model.SclSubstation.*;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SclReader {

    private boolean strictMode = false;

    public SclReader setStrictMode(boolean strictMode) {
        this.strictMode = strictMode;
        return this;
    }

    public boolean isStrictMode() {
        return strictMode;
    }

    public SclDocument read(String filePath) throws Exception {
        return read(Paths.get(filePath));
    }

    public SclDocument read(Path filePath) throws Exception {
        XMLInputFactory factory = newFactory();
        try (InputStream is = new FileInputStream(filePath.toFile())) {
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            SclDocument scl = parseDocument(reader);
            scl.setOriginalFilePath(filePath.toString());
            return scl;
        }
    }

    public SclDocument read(InputStream inputStream) throws Exception {
        XMLInputFactory factory = newFactory();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
        return parseDocument(reader);
    }

    public SclDocument read(InputStream inputStream, String pathHint) throws Exception {
        XMLInputFactory factory = newFactory();
        XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
        SclDocument scl = parseDocument(reader);
        scl.setOriginalFilePath(pathHint);
        return scl;
    }

    private static XMLInputFactory newFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        return factory;
    }

    private SclDocument parseDocument(XMLStreamReader reader) throws Exception {
        SclDocument scl = new SclDocument();
        boolean hasSubstation = false;
        boolean hasCommunication = false;
        int iedCount = 0;

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "SCL" -> {
                        scl.setXmlns(getAttr(reader, "xmlns"));
                        String schemaLocation = getAttr(reader, "xsi:schemaLocation");
                        if (schemaLocation != null && !schemaLocation.isEmpty()) {
                            scl.setXsiSchemaLocation(schemaLocation);
                        }
                    }
                    case "Header" -> scl.setHeader(parseHeader(reader));
                    case "Substation" -> {
                        hasSubstation = true;
                        scl.setSubstation(parseSubstation(reader));
                    }
                    case "Communication" -> {
                        hasCommunication = true;
                        scl.setCommunication(parseCommunication(reader));
                    }
                    case "IED" -> {
                        iedCount++;
                        scl.addIed(parseIED(reader));
                    }
                    case "DataTypeTemplates" -> scl.setDataTypeTemplates(parseDataTypeTemplates(reader));
                    case "Line", "Process" -> {
                        String name = getAttr(reader, "name");
                        log.warn("Unsupported SCL element <{} name=\"{}\"> encountered, it will be ignored", tag, name);
                        scl.addUnsupportedElement(tag);
                        skipElement(reader);
                    }
                }
            }
        }

        if (scl.getFileType() == SclDocument.SclFileType.UNKNOWN) {
            scl.setFileType(detectFileType(hasSubstation, hasCommunication, iedCount));
        }
        if (strictMode) {
            validateStrict(scl, hasSubstation, hasCommunication, iedCount);
        }
        return scl;
    }

    // ──────────────────────────────────────────────
    //  Header
    // ──────────────────────────────────────────────

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
                    header.setText(reader.getElementText().trim());
                } else if ("History".equals(tag)) {
                    parseHistory(reader, header);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Header".equals(reader.getLocalName())) break;
            }
        }
        return header;
    }

    private void parseHistory(XMLStreamReader reader, SclHeader header) throws Exception {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Hitem".equals(reader.getLocalName())) {
                    header.addHitem(parseHitem(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("History".equals(reader.getLocalName())) break;
            }
        }
    }

    private SclHitem parseHitem(XMLStreamReader reader) throws Exception {
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

    // ──────────────────────────────────────────────
    //  Substation
    // ──────────────────────────────────────────────

    private SclSubstation parseSubstation(XMLStreamReader reader) throws Exception {
        SclSubstation substation = new SclSubstation();
        substation.setName(getAttr(reader, "name"));
        substation.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("VoltageLevel".equals(tag)) {
                    substation.addVoltageLevel(parseVoltageLevel(reader));
                } else if ("PowerTransformer".equals(tag)) {
                    if (!substation.getVoltageLevels().isEmpty()) {
                        substation.getVoltageLevels().get(0).addPowerTransformer(parsePowerTransformer(reader));
                    }
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

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("Voltage".equals(tag)) {
                    String unit = getAttr(reader, "unit");
                    String multiplier = getAttr(reader, "multiplier");
                    vl.setVoltage(Double.parseDouble(reader.getElementText().trim()));
                    vl.setVoltageUnit(unit);
                    vl.setVoltageMultiplier(multiplier);
                } else if ("Bay".equals(tag)) {
                    vl.addBay(parseBay(reader));
                } else if ("PowerTransformer".equals(tag)) {
                    vl.addPowerTransformer(parsePowerTransformer(reader));
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
                String tag = reader.getLocalName();
                if ("ConductingEquipment".equals(tag)) {
                    bay.addConductingEquipment(parseConductingEquipment(reader));
                } else if ("ConnectivityNode".equals(tag)) {
                    bay.addConnectivityNode(parseConnectivityNode(reader));
                } else if ("LNode".equals(tag)) {
                    bay.addLNode(parseLNode(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Bay".equals(reader.getLocalName())) break;
            }
        }
        return bay;
    }

    private SclPowerTransformer parsePowerTransformer(XMLStreamReader reader) throws Exception {
        SclPowerTransformer pt = new SclPowerTransformer();
        pt.setName(getAttr(reader, "name"));
        pt.setType(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("TransformerWinding".equals(tag)) {
                    pt.addWinding(parseTransformerWinding(reader));
                } else if ("LNode".equals(tag)) {
                    pt.addLNode(parseLNode(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("PowerTransformer".equals(reader.getLocalName())) break;
            }
        }
        return pt;
    }

    private SclTransformerWinding parseTransformerWinding(XMLStreamReader reader) throws Exception {
        SclTransformerWinding tw = new SclTransformerWinding();
        tw.setName(getAttr(reader, "name"));
        tw.setType(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Terminal".equals(reader.getLocalName())) {
                    tw.addTerminal(parseTerminal(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("TransformerWinding".equals(reader.getLocalName())) break;
            }
        }
        return tw;
    }

    private SclConductingEquipment parseConductingEquipment(XMLStreamReader reader) throws Exception {
        SclConductingEquipment ce = new SclConductingEquipment();
        ce.setName(getAttr(reader, "name"));
        ce.setType(getAttr(reader, "type"));
        ce.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("Terminal".equals(tag)) {
                    ce.addTerminal(parseTerminal(reader));
                } else if ("SubEquipment".equals(tag)) {
                    ce.addSubEquipment(parseSubEquipment(reader));
                } else if ("LNode".equals(tag)) {
                    ce.addLNode(parseLNode(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("ConductingEquipment".equals(reader.getLocalName())) break;
            }
        }
        return ce;
    }

    private SclSubEquipment parseSubEquipment(XMLStreamReader reader) throws Exception {
        SclSubEquipment se = new SclSubEquipment();
        se.setName(getAttr(reader, "name"));
        se.setPhase(getAttr(reader, "phase"));
        se.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("LNode".equals(reader.getLocalName())) {
                    se.addLNode(parseLNode(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SubEquipment".equals(reader.getLocalName())) break;
            }
        }
        return se;
    }

    private SclTerminal parseTerminal(XMLStreamReader reader) throws Exception {
        SclTerminal terminal = new SclTerminal();
        terminal.setConnectivityNode(getAttr(reader, "connectivityNode"));
        terminal.setSubstationName(getAttr(reader, "substationName"));
        terminal.setVoltageLevelName(getAttr(reader, "voltageLevelName"));
        terminal.setBayName(getAttr(reader, "bayName"));
        terminal.setCNodeName(getAttr(reader, "cNodeName"));
        skipElement(reader);
        return terminal;
    }

    private SclConnectivityNode parseConnectivityNode(XMLStreamReader reader) throws Exception {
        SclConnectivityNode cn = new SclConnectivityNode();
        cn.setName(getAttr(reader, "name"));
        cn.setPathName(getAttr(reader, "pathName"));
        skipElement(reader);
        return cn;
    }

    private SclLNode parseLNode(XMLStreamReader reader) throws Exception {
        SclLNode lNode = new SclLNode();
        lNode.setIedName(getAttr(reader, "iedName"));
        lNode.setLdInst(getAttr(reader, "ldInst"));
        lNode.setLnClass(getAttr(reader, "lnClass"));
        lNode.setLnInst(getAttr(reader, "lnInst"));
        lNode.setPrefix(getAttr(reader, "prefix"));
        lNode.setDesc(getAttr(reader, "desc"));
        skipElement(reader);
        return lNode;
    }

    // ──────────────────────────────────────────────
    //  Communication
    // ──────────────────────────────────────────────

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
        sn.setType(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("Text".equals(tag)) {
                    sn.setDesc(reader.getElementText().trim());
                } else if ("BitRate".equals(tag)) {
                    sn.setBitRate(reader.getElementText().trim());
                } else if ("ConnectedAP".equals(tag)) {
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
                if ("Address".equals(tag)) {
                    cap.setAddress(parseAddress(reader));
                } else if ("GSE".equals(tag)) {
                    cap.addGse(parseGSE(reader));
                } else if ("SMV".equals(tag)) {
                    cap.addSmv(parseSMV(reader));
                } else if ("PhysConn".equals(tag)) {
                    cap.setPhysConn(parsePhysConn(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("ConnectedAP".equals(reader.getLocalName())) break;
            }
        }
        return cap;
    }

    private SclAddress parseAddress(XMLStreamReader reader) throws Exception {
        SclAddress addr = new SclAddress();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    String type = getAttr(reader, "type");
                    String value = reader.getElementText().trim();
                    addr.addParam(type, value);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) break;
            }
        }
        return addr;
    }

    private SclGSE parseGSE(XMLStreamReader reader) throws Exception {
        SclGSE gse = new SclGSE();
        gse.setLdInst(getAttr(reader, "ldInst"));
        gse.setCbName(getAttr(reader, "cbName"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) {
                    gse.setAddress(parseAddress(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("GSE".equals(reader.getLocalName())) break;
            }
        }
        return gse;
    }

    private SclSMV parseSMV(XMLStreamReader reader) throws Exception {
        SclSMV smv = new SclSMV();
        smv.setLdInst(getAttr(reader, "ldInst"));
        smv.setCbName(getAttr(reader, "cbName"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) {
                    smv.setAddress(parseAddress(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SMV".equals(reader.getLocalName())) break;
            }
        }
        return smv;
    }

    private SclPhysConn parsePhysConn(XMLStreamReader reader) throws Exception {
        SclPhysConn pc = new SclPhysConn();
        pc.setType(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    String type = getAttr(reader, "type");
                    String value = reader.getElementText().trim();
                    if ("Type".equals(type)) pc.setPlugType(value);
                    else if ("Plug".equals(type)) pc.setPlug(value);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("PhysConn".equals(reader.getLocalName())) break;
            }
        }
        return pc;
    }

    // ──────────────────────────────────────────────
    //  IED
    // ──────────────────────────────────────────────

    private SclIED parseIED(XMLStreamReader reader) throws Exception {
        SclIED ied = new SclIED();
        ied.setName(getAttr(reader, "name"));
        ied.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("Services".equals(tag)) {
                    ied.setServices(parseServices(reader));
                } else if ("AccessPoint".equals(tag)) {
                    ied.addAccessPoint(parseAccessPoint(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("IED".equals(reader.getLocalName())) break;
            }
        }
        return ied;
    }

    private SclServices parseServices(XMLStreamReader reader) throws Exception {
        SclServices svc = new SclServices();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "DynAssociation" -> svc.setDynAssociation(true);
                    case "GetDirectory" -> svc.setGetDirectory(true);
                    case "GetDataObjectDefinition" -> svc.setGetDataObjectDefinition(true);
                    case "GetDataSetValue" -> svc.setGetDataSetValue(true);
                    case "DataSetDirectory" -> svc.setDataSetDirectory(true);
                    case "ReadWrite" -> svc.setReadWrite(true);
                    case "FileHandling" -> svc.setFileHandling(true);
                    case "GetCBValues" -> svc.setGetCBValues(true);
                    case "GSEDir" -> svc.setGSEDir(true);
                    case "TimerActivatedControl" -> svc.setTimerActivatedControl(true);
                    case "ConfDataSet" -> {
                        String max = getAttr(reader, "max");
                        if (!max.isEmpty()) svc.setConfDataSetMax(Integer.parseInt(max));
                        String maxAttr = getAttr(reader, "maxAttributes");
                        if (!maxAttr.isEmpty()) svc.setConfDataSetMaxAttributes(Integer.parseInt(maxAttr));
                    }
                    case "ConfReportControl" -> {
                        String max = getAttr(reader, "max");
                        if (!max.isEmpty()) svc.setConfReportControlMax(Integer.parseInt(max));
                    }
                    case "ConfLogControl" -> {
                        String max = getAttr(reader, "max");
                        if (!max.isEmpty()) svc.setConfLogControlMax(Integer.parseInt(max));
                    }
                    case "GOOSE" -> {
                        String max = getAttr(reader, "max");
                        if (!max.isEmpty()) svc.setGooseMax(Integer.parseInt(max));
                    }
                    case "GSSE" -> {
                        String max = getAttr(reader, "max");
                        if (!max.isEmpty()) svc.setGsseMax(Integer.parseInt(max));
                    }
                    case "ConfLNs" -> {
                        String fixPrefix = getAttr(reader, "fixPrefix");
                        if (!fixPrefix.isEmpty()) svc.setConfLNsFixPrefix(Boolean.parseBoolean(fixPrefix));
                        String fixLnInst = getAttr(reader, "fixLnInst");
                        if (!fixLnInst.isEmpty()) svc.setConfLNsFixLnInst(Boolean.parseBoolean(fixLnInst));
                    }
                    case "ReportSettings" -> {
                        SclReportSettings rs = new SclReportSettings();
                        rs.setBufTime(getAttr(reader, "bufTime"));
                        rs.setCbName(getAttr(reader, "cbName"));
                        rs.setRptID(getAttr(reader, "rptID"));
                        rs.setDatSet(getAttr(reader, "datSet"));
                        rs.setIntgPd(getAttr(reader, "intgPd"));
                        rs.setOptFields(getAttr(reader, "optFields"));
                        svc.setReportSettings(rs);
                    }
                    case "GSESettings" -> {
                        SclGSESettings gs = new SclGSESettings();
                        gs.setAppID(getAttr(reader, "appID"));
                        gs.setCbName(getAttr(reader, "cbName"));
                        gs.setDatSet(getAttr(reader, "datSet"));
                        svc.setGseSettings(gs);
                    }
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Services".equals(reader.getLocalName())) break;
            }
        }
        return svc;
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
                if ("LN0".equals(tag)) {
                    ld.setLn0(parseLN0(reader));
                } else if ("LN".equals(tag)) {
                    ld.addLn(parseLN(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LDevice".equals(reader.getLocalName())) break;
            }
        }
        return ld;
    }

    private SclLN0 parseLN0(XMLStreamReader reader) throws Exception {
        SclLN0 ln0 = new SclLN0();
        ln0.setLnType(getAttr(reader, "lnType"));
        ln0.setLnClass(getAttr(reader, "lnClass"));
        ln0.setInst(getAttr(reader, "inst"));
        ln0.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                switch (tag) {
                    case "DataSet" -> ln0.addDataSet(parseDataSet(reader));
                    case "ReportControl" -> ln0.addReportControl(parseReportControl(reader));
                    case "LogControl" -> ln0.addLogControl(parseLogControl(reader));
                    case "GSEControl" -> ln0.addGseControl(parseGSEControl(reader));
                    case "SampledValueControl" -> ln0.addSampledValueControl(parseSampledValueControl(reader));
                    case "DOI" -> ln0.addDoi(parseDOI(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LN0".equals(reader.getLocalName())) break;
            }
        }
        return ln0;
    }

    private SclLN parseLN(XMLStreamReader reader) throws Exception {
        SclLN ln = new SclLN();
        ln.setLnType(getAttr(reader, "lnType"));
        ln.setLnClass(getAttr(reader, "lnClass"));
        ln.setInst(getAttr(reader, "inst"));
        ln.setPrefix(getAttr(reader, "prefix"));
        ln.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("DOI".equals(tag)) {
                    ln.addDoi(parseDOI(reader));
                } else if ("Inputs".equals(tag)) {
                    List<SclInputs> inputsList = new ArrayList<>();
                    inputsList.add(parseInputs(reader));
                    ln.setInputs(inputsList);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LN".equals(reader.getLocalName())) break;
            }
        }
        return ln;
    }

    private SclDataSet parseDataSet(XMLStreamReader reader) throws Exception {
        SclDataSet ds = new SclDataSet();
        ds.setName(getAttr(reader, "name"));
        ds.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("FCDA".equals(reader.getLocalName())) {
                    ds.addFcda(parseFCDA(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DataSet".equals(reader.getLocalName())) break;
            }
        }
        return ds;
    }

    private SclFCDA parseFCDA(XMLStreamReader reader) throws Exception {
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
        rc.setRptID(getAttr(reader, "rptID"));
        rc.setDatSet(getAttr(reader, "datSet"));
        rc.setConfRev(getAttr(reader, "confRev"));
        rc.setBuffered("true".equals(getAttr(reader, "buffered")));
        rc.setBufTime(getAttr(reader, "bufTime"));
        rc.setIntgPd(getAttr(reader, "intgPd"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("TrgOps".equals(tag)) {
                    rc.setTrgOps(parseTrgOps(reader));
                } else if ("OptFields".equals(tag)) {
                    rc.setOptFields(parseOptFields(reader));
                } else if ("RptEnabled".equals(tag)) {
                    rc.setRptEnabled(parseRptEnabled(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("ReportControl".equals(reader.getLocalName())) break;
            }
        }
        return rc;
    }

    private SclTrgOps parseTrgOps(XMLStreamReader reader) throws Exception {
        SclTrgOps trgOps = new SclTrgOps();
        trgOps.setDchg("true".equals(getAttr(reader, "dchg")));
        trgOps.setQchg("true".equals(getAttr(reader, "qchg")));
        trgOps.setDupd("true".equals(getAttr(reader, "dupd")));
        trgOps.setPeriod("true".equals(getAttr(reader, "period")));
        trgOps.setGi("true".equals(getAttr(reader, "gi")));
        skipElement(reader);
        return trgOps;
    }

    private SclOptFields parseOptFields(XMLStreamReader reader) throws Exception {
        SclOptFields opt = new SclOptFields();
        opt.setDataSet("true".equals(getAttr(reader, "dataSet")));
        opt.setBufOvfl("true".equals(getAttr(reader, "bufOvfl")));
        opt.setConfigRef("true".equals(getAttr(reader, "configRef")));
        opt.setDataRef("true".equals(getAttr(reader, "dataRef")));
        opt.setEntryID("true".equals(getAttr(reader, "entryID")));
        opt.setReasonCode("true".equals(getAttr(reader, "reasonCode")));
        opt.setTimeStamp("true".equals(getAttr(reader, "timeStamp")));
        opt.setSeqNum("true".equals(getAttr(reader, "seqNum")));
        skipElement(reader);
        return opt;
    }

    private SclRptEnabled parseRptEnabled(XMLStreamReader reader) throws Exception {
        SclRptEnabled rptEnabled = new SclRptEnabled();
        String max = getAttr(reader, "max");
        if (!max.isEmpty()) {
            rptEnabled.setMax(Integer.parseInt(max));
        }

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ClientLN".equals(reader.getLocalName())) {
                    SclClientLN clientLN = new SclClientLN();
                    clientLN.setIedName(getAttr(reader, "iedName"));
                    clientLN.setLdInst(getAttr(reader, "ldInst"));
                    clientLN.setLnClass(getAttr(reader, "lnClass"));
                    clientLN.setLnInst(getAttr(reader, "lnInst"));
                    rptEnabled.addClientLN(clientLN);
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("RptEnabled".equals(reader.getLocalName())) break;
            }
        }
        return rptEnabled;
    }

    private SclLogControl parseLogControl(XMLStreamReader reader) throws Exception {
        SclLogControl lc = new SclLogControl();
        lc.setName(getAttr(reader, "name"));
        lc.setDatSet(getAttr(reader, "datSet"));
        lc.setLogName(getAttr(reader, "logName"));
        lc.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("TrgOps".equals(reader.getLocalName())) {
                    lc.setTrgOps(parseTrgOps(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LogControl".equals(reader.getLocalName())) break;
            }
        }
        return lc;
    }

    private SclGSEControl parseGSEControl(XMLStreamReader reader) throws Exception {
        SclGSEControl gse = new SclGSEControl();
        gse.setName(getAttr(reader, "name"));
        gse.setDatSet(getAttr(reader, "datSet"));
        gse.setAppID(getAttr(reader, "appID"));
        gse.setConfRev(getAttr(reader, "confRev"));
        gse.setType(getAttr(reader, "type"));
        skipElement(reader);
        return gse;
    }

    private SclSampledValueControl parseSampledValueControl(XMLStreamReader reader) throws Exception {
        SclSampledValueControl sv = new SclSampledValueControl();
        sv.setName(getAttr(reader, "name"));
        sv.setDatSet(getAttr(reader, "datSet"));
        sv.setSmvID(getAttr(reader, "smvID"));
        String smpRate = getAttr(reader, "smpRate");
        if (!smpRate.isEmpty()) sv.setSmpRate(Integer.parseInt(smpRate));
        String nofASDU = getAttr(reader, "nofASDU");
        if (!nofASDU.isEmpty()) sv.setNofASDU(Integer.parseInt(nofASDU));
        sv.setMulticast("true".equals(getAttr(reader, "multicast")));
        sv.setConfRev(getAttr(reader, "confRev"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("SmvOpts".equals(reader.getLocalName())) {
                    sv.setSmvOpts(parseSmvOpts(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SampledValueControl".equals(reader.getLocalName())) break;
            }
        }
        return sv;
    }

    private SclSmvOpts parseSmvOpts(XMLStreamReader reader) throws Exception {
        SclSmvOpts opts = new SclSmvOpts();
        opts.setSampleRate("true".equals(getAttr(reader, "sampleRate")));
        opts.setRefreshTime("true".equals(getAttr(reader, "refreshTime")));
        opts.setSampleSynchronized("true".equals(getAttr(reader, "sampleSynchronized")));
        skipElement(reader);
        return opts;
    }

    private SclDOI parseDOI(XMLStreamReader reader) throws Exception {
        SclDOI doi = new SclDOI();
        doi.setName(getAttr(reader, "name"));
        doi.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("DAI".equals(tag)) {
                    doi.addDai(parseDAI(reader));
                } else if ("SDI".equals(tag)) {
                    doi.addSdi(parseSDI(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DOI".equals(reader.getLocalName())) break;
            }
        }
        return doi;
    }

    private SclDAI parseDAI(XMLStreamReader reader) throws Exception {
        SclDAI dai = new SclDAI();
        dai.setName(getAttr(reader, "name"));
        dai.setSAddr(getAttr(reader, "sAddr"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Val".equals(reader.getLocalName())) {
                    dai.setValue(reader.getElementText().trim());
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DAI".equals(reader.getLocalName())) break;
            }
        }
        return dai;
    }

    private SclSDI parseSDI(XMLStreamReader reader) throws Exception {
        SclSDI sdi = new SclSDI();
        sdi.setName(getAttr(reader, "name"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("DAI".equals(reader.getLocalName())) {
                    sdi.addDai(parseDAI(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("SDI".equals(reader.getLocalName())) break;
            }
        }
        return sdi;
    }

    private SclInputs parseInputs(XMLStreamReader reader) throws Exception {
        SclInputs inputs = new SclInputs();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ExtRef".equals(reader.getLocalName())) {
                    inputs.addExtRef(parseExtRef(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("Inputs".equals(reader.getLocalName())) break;
            }
        }
        return inputs;
    }

    private SclExtRef parseExtRef(XMLStreamReader reader) throws Exception {
        SclExtRef extRef = new SclExtRef();
        extRef.setIntAddr(getAttr(reader, "intAddr"));
        extRef.setDesc(getAttr(reader, "desc"));
        extRef.setIedName(getAttr(reader, "iedName"));
        extRef.setLdInst(getAttr(reader, "ldInst"));
        extRef.setLnClass(getAttr(reader, "lnClass"));
        extRef.setLnInst(getAttr(reader, "lnInst"));
        extRef.setDoName(getAttr(reader, "doName"));
        extRef.setDaName(getAttr(reader, "daName"));
        extRef.setServiceType(getAttr(reader, "serviceType"));
        skipElement(reader);
        return extRef;
    }

    // ──────────────────────────────────────────────
    //  DataTypeTemplates
    // ──────────────────────────────────────────────

    private SclDataTypeTemplates parseDataTypeTemplates(XMLStreamReader reader) throws Exception {
        if (reader == null) return null;
        SclDataTypeTemplates templates = new SclDataTypeTemplates();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("LNodeType".equals(tag)) {
                    templates.addLNodeType(parseLNodeType(reader));
                } else if ("DOType".equals(tag)) {
                    templates.addDoType(parseDOType(reader));
                } else if ("DAType".equals(tag)) {
                    templates.addDaType(parseDAType(reader));
                } else if ("EnumType".equals(tag)) {
                    templates.addEnumType(parseEnumType(reader));
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
                    SclDO doObj = new SclDO();
                    doObj.setName(getAttr(reader, "name"));
                    doObj.setType(getAttr(reader, "type"));
                    lnt.addDo(doObj);
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("LNodeType".equals(reader.getLocalName())) break;
            }
        }
        return lnt;
    }

    private SclDOType parseDOType(XMLStreamReader reader) throws Exception {
        SclDOType dot = new SclDOType();
        dot.setId(getAttr(reader, "id"));
        dot.setCdc(getAttr(reader, "cdc"));
        dot.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                String tag = reader.getLocalName();
                if ("DA".equals(tag)) {
                    dot.addDa(parseDA(reader));
                } else if ("SDO".equals(tag)) {
                    dot.addSdo(parseSDO(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DOType".equals(reader.getLocalName())) break;
            }
        }
        return dot;
    }

    private SclDA parseDA(XMLStreamReader reader) throws Exception {
        SclDA da = new SclDA();
        da.setName(getAttr(reader, "name"));
        da.setType(getAttr(reader, "type"));
        da.setBType(getAttr(reader, "bType"));
        da.setFc(getAttr(reader, "fc"));
        da.setDchg("true".equals(getAttr(reader, "dchg")));
        da.setQchg("true".equals(getAttr(reader, "qchg")));
        da.setDupd("true".equals(getAttr(reader, "dupd")));
        String count = getAttr(reader, "count");
        if (!count.isEmpty()) da.setCount(Integer.parseInt(count));
        skipElement(reader);
        return da;
    }

    private SclSDO parseSDO(XMLStreamReader reader) throws Exception {
        SclSDO sdo = new SclSDO();
        sdo.setName(getAttr(reader, "name"));
        sdo.setType(getAttr(reader, "type"));
        skipElement(reader);
        return sdo;
    }

    private SclDAType parseDAType(XMLStreamReader reader) throws Exception {
        SclDAType dat = new SclDAType();
        dat.setId(getAttr(reader, "id"));
        dat.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("BDA".equals(reader.getLocalName())) {
                    dat.addBda(parseBDA(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("DAType".equals(reader.getLocalName())) break;
            }
        }
        return dat;
    }

    private SclBDA parseBDA(XMLStreamReader reader) throws Exception {
        SclBDA bda = new SclBDA();
        bda.setName(getAttr(reader, "name"));
        bda.setType(getAttr(reader, "type"));
        bda.setBType(getAttr(reader, "bType"));
        String count = getAttr(reader, "count");
        if (!count.isEmpty()) bda.setCount(Integer.parseInt(count));
        skipElement(reader);
        return bda;
    }

    private SclEnumType parseEnumType(XMLStreamReader reader) throws Exception {
        SclEnumType et = new SclEnumType();
        et.setId(getAttr(reader, "id"));
        et.setDesc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("EnumVal".equals(reader.getLocalName())) {
                    et.addEnumVal(parseEnumVal(reader));
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                if ("EnumType".equals(reader.getLocalName())) break;
            }
        }
        return et;
    }

    private SclEnumVal parseEnumVal(XMLStreamReader reader) throws Exception {
        SclEnumVal ev = new SclEnumVal();
        String ord = getAttr(reader, "ord");
        if (!ord.isEmpty()) ev.setOrd(Integer.parseInt(ord));
        ev.setValue(reader.getElementText().trim());
        return ev;
    }

    // ──────────────────────────────────────────────
    //  Validation & detection (no more DOM needed)
    // ──────────────────────────────────────────────

    private SclDocument.SclFileType detectFileType(boolean hasSubstation, boolean hasCommunication, int iedCount) {
        if (hasSubstation && hasCommunication && iedCount >= 1) {
            return SclDocument.SclFileType.SCD;
        }
        if (!hasSubstation && hasCommunication && iedCount == 1) {
            return SclDocument.SclFileType.CID;
        }
        if (!hasSubstation && !hasCommunication && iedCount == 1) {
            return SclDocument.SclFileType.ICD;
        }
        return SclDocument.SclFileType.UNKNOWN;
    }

    private void validateStrict(SclDocument scl, boolean hasSubstation, boolean hasCommunication, int iedCount) {
        switch (scl.getFileType()) {
            case ICD:
                if (hasSubstation) {
                    throw new IllegalArgumentException(
                        "ICD file must not contain a Substation element");
                }
                if (iedCount != 1) {
                    throw new IllegalArgumentException(
                        "ICD file must contain exactly 1 IED, found " + iedCount);
                }
                break;
            case CID:
                if (iedCount != 1) {
                    throw new IllegalArgumentException(
                        "CID file must contain exactly 1 IED, found " + iedCount);
                }
                break;
            case SCD:
                if (iedCount < 1) {
                    throw new IllegalArgumentException(
                        "SCD file must contain at least 1 IED");
                }
                break;
            case UNKNOWN:
                break;
        }
    }

    // ──────────────────────────────────────────────
    //  Helpers
    // ──────────────────────────────────────────────

    private static String getAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        return val != null ? val : "";
    }

    private static void skipElement(XMLStreamReader reader) throws Exception {
        int depth = 1;
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == XMLStreamConstants.START_ELEMENT) depth++;
            else if (event == XMLStreamConstants.END_ELEMENT) depth--;
        }
    }
}
