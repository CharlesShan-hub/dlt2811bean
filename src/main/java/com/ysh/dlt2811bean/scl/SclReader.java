package com.ysh.dlt2811bean.scl;

import com.ysh.dlt2811bean.scl.model.*;
import com.ysh.dlt2811bean.scl.model.SclCommunication.*;
import com.ysh.dlt2811bean.scl.model.SclIED.*;
import com.ysh.dlt2811bean.scl.model.SclSubstation.*;
import com.ysh.dlt2811bean.scl.model.SclDataTypeTemplates.*;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class SclReader {

    public SclDocument read(String filePath) throws Exception {
        return read(Paths.get(filePath));
    }

    public SclDocument read(Path filePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(filePath.toFile());
        return parseDocument(doc);
    }

    public SclDocument read(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(inputStream);
        return parseDocument(doc);
    }

    private SclDocument parseDocument(Document doc) {
        SclDocument scl = new SclDocument();
        Element root = doc.getDocumentElement();
        scl.setXmlns(root.getAttribute("xmlns"));
        String schemaLocation = root.getAttribute("xsi:schemaLocation");
        if (schemaLocation != null && !schemaLocation.isEmpty()) {
            scl.setXsiSchemaLocation(schemaLocation);
        }
        scl.setHeader(parseHeader(getChild(root, "Header")));
        scl.setSubstation(parseSubstation(getChild(root, "Substation")));
        scl.setCommunication(parseCommunication(getChild(root, "Communication")));
        NodeList iedNodes = root.getElementsByTagName("IED");
        for (int i = 0; i < iedNodes.getLength(); i++) {
            Element iedElem = (Element) iedNodes.item(i);
            scl.addIed(parseIED(iedElem));
        }
        scl.setDataTypeTemplates(parseDataTypeTemplates(getChild(root, "DataTypeTemplates")));
        return scl;
    }

    private SclHeader parseHeader(Element elem) {
        if (elem == null) return null;
        SclHeader header = new SclHeader();
        header.setId(elem.getAttribute("id"));
        header.setVersion(elem.getAttribute("version"));
        header.setRevision(elem.getAttribute("revision"));
        header.setToolId(elem.getAttribute("toolID"));
        header.setNameStructure(elem.getAttribute("nameStructure"));
        return header;
    }

    private SclSubstation parseSubstation(Element elem) {
        if (elem == null) return null;
        SclSubstation substation = new SclSubstation();
        substation.setName(elem.getAttribute("name"));
        substation.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("VoltageLevel".equals(tag)) {
                substation.addVoltageLevel(parseVoltageLevel(child));
            } else if ("PowerTransformer".equals(tag)) {
                substation.getVoltageLevels().stream().findFirst().ifPresent(
                    vl -> vl.addPowerTransformer(parsePowerTransformer(child)));
            }
        }
        return substation;
    }

    private SclVoltageLevel parseVoltageLevel(Element elem) {
        SclVoltageLevel vl = new SclVoltageLevel();
        vl.setName(elem.getAttribute("name"));
        vl.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("Voltage".equals(tag)) {
                vl.setVoltage(Double.parseDouble(child.getTextContent().trim()));
                vl.setVoltageUnit(child.getAttribute("unit"));
                vl.setVoltageMultiplier(child.getAttribute("multiplier"));
            } else if ("Bay".equals(tag)) {
                vl.addBay(parseBay(child));
            } else if ("PowerTransformer".equals(tag)) {
                vl.addPowerTransformer(parsePowerTransformer(child));
            }
        }
        return vl;
    }

    private SclBay parseBay(Element elem) {
        SclBay bay = new SclBay();
        bay.setName(elem.getAttribute("name"));
        bay.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("ConductingEquipment".equals(tag)) {
                bay.addConductingEquipment(parseConductingEquipment(child));
            } else if ("ConnectivityNode".equals(tag)) {
                bay.addConnectivityNode(parseConnectivityNode(child));
            } else if ("LNode".equals(tag)) {
                bay.addLNode(parseLNode(child));
            }
        }
        return bay;
    }

    private SclPowerTransformer parsePowerTransformer(Element elem) {
        SclPowerTransformer pt = new SclPowerTransformer();
        pt.setName(elem.getAttribute("name"));
        pt.setType(elem.getAttribute("type"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("TransformerWinding".equals(tag)) {
                pt.addWinding(parseTransformerWinding(child));
            } else if ("LNode".equals(tag)) {
                pt.addLNode(parseLNode(child));
            }
        }
        return pt;
    }

    private SclTransformerWinding parseTransformerWinding(Element elem) {
        SclTransformerWinding tw = new SclTransformerWinding();
        tw.setName(elem.getAttribute("name"));
        tw.setType(elem.getAttribute("type"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            if ("Terminal".equals(child.getTagName())) {
                tw.addTerminal(parseTerminal(child));
            }
        }
        return tw;
    }

    private SclConductingEquipment parseConductingEquipment(Element elem) {
        SclConductingEquipment ce = new SclConductingEquipment();
        ce.setName(elem.getAttribute("name"));
        ce.setType(elem.getAttribute("type"));
        ce.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("Terminal".equals(tag)) {
                ce.addTerminal(parseTerminal(child));
            } else if ("SubEquipment".equals(tag)) {
                ce.addSubEquipment(parseSubEquipment(child));
            } else if ("LNode".equals(tag)) {
                ce.addLNode(parseLNode(child));
            }
        }
        return ce;
    }

    private SclSubEquipment parseSubEquipment(Element elem) {
        SclSubEquipment se = new SclSubEquipment();
        se.setName(elem.getAttribute("name"));
        se.setPhase(elem.getAttribute("phase"));
        se.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            if ("LNode".equals(child.getTagName())) {
                se.addLNode(parseLNode(child));
            }
        }
        return se;
    }

    private SclTerminal parseTerminal(Element elem) {
        SclTerminal terminal = new SclTerminal();
        terminal.setConnectivityNode(elem.getAttribute("connectivityNode"));
        terminal.setSubstationName(elem.getAttribute("substationName"));
        terminal.setVoltageLevelName(elem.getAttribute("voltageLevelName"));
        terminal.setBayName(elem.getAttribute("bayName"));
        terminal.setCNodeName(elem.getAttribute("cNodeName"));
        return terminal;
    }

    private SclConnectivityNode parseConnectivityNode(Element elem) {
        SclConnectivityNode cn = new SclConnectivityNode();
        cn.setName(elem.getAttribute("name"));
        cn.setPathName(elem.getAttribute("pathName"));
        return cn;
    }

    private SclLNode parseLNode(Element elem) {
        SclLNode lNode = new SclLNode();
        lNode.setIedName(elem.getAttribute("iedName"));
        lNode.setLdInst(elem.getAttribute("ldInst"));
        lNode.setLnClass(elem.getAttribute("lnClass"));
        lNode.setLnInst(elem.getAttribute("lnInst"));
        lNode.setPrefix(elem.getAttribute("prefix"));
        lNode.setDesc(elem.getAttribute("desc"));
        return lNode;
    }

    private SclCommunication parseCommunication(Element elem) {
        if (elem == null) return null;
        SclCommunication comm = new SclCommunication();
        NodeList subNetworkNodes = elem.getElementsByTagName("SubNetwork");
        for (int i = 0; i < subNetworkNodes.getLength(); i++) {
            comm.addSubNetwork(parseSubNetwork((Element) subNetworkNodes.item(i)));
        }
        return comm;
    }

    private SclSubNetwork parseSubNetwork(Element elem) {
        SclSubNetwork sn = new SclSubNetwork();
        sn.setName(elem.getAttribute("name"));
        sn.setType(elem.getAttribute("type"));
        sn.setDesc(getTextContent(getChild(elem, "Text")));
        Element bitRateElem = getChild(elem, "BitRate");
        if (bitRateElem != null) {
            sn.setBitRate(Double.parseDouble(bitRateElem.getTextContent().trim()));
        }
        NodeList apNodes = elem.getElementsByTagName("ConnectedAP");
        for (int i = 0; i < apNodes.getLength(); i++) {
            sn.addConnectedAP(parseConnectedAP((Element) apNodes.item(i)));
        }
        return sn;
    }

    private SclConnectedAP parseConnectedAP(Element elem) {
        SclConnectedAP cap = new SclConnectedAP();
        cap.setIedName(elem.getAttribute("iedName"));
        cap.setApName(elem.getAttribute("apName"));
        Element addressElem = getChild(elem, "Address");
        if (addressElem != null) {
            cap.setAddress(parseAddress(addressElem));
        }
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("GSE".equals(tag)) {
                cap.addGse(parseGSE(child));
            } else if ("SMV".equals(tag)) {
                cap.addSmv(parseSMV(child));
            } else if ("PhysConn".equals(tag)) {
                cap.setPhysConn(parsePhysConn(child));
            }
        }
        return cap;
    }

    private SclAddress parseAddress(Element elem) {
        SclAddress addr = new SclAddress();
        NodeList pNodes = elem.getElementsByTagName("P");
        for (int i = 0; i < pNodes.getLength(); i++) {
            Element pElem = (Element) pNodes.item(i);
            String type = pElem.getAttribute("type");
            String value = pElem.getTextContent().trim();
            switch (type) {
                case "IP": addr.setIp(value); break;
                case "IP-SUBNET": addr.setSubnet(value); break;
                case "IP-GATEWAY": addr.setGateway(value); break;
                case "OSI-TSEL": addr.setOsiTsel(value); break;
                case "OSI-PSEL": addr.setOsiPsel(value); break;
                case "OSI-SSEL": addr.setOsiSsel(value); break;
            }
        }
        return addr;
    }

    private SclGSE parseGSE(Element elem) {
        SclGSE gse = new SclGSE();
        gse.setLdInst(elem.getAttribute("ldInst"));
        gse.setCbName(elem.getAttribute("cbName"));
        Element addrElem = getChild(elem, "Address");
        if (addrElem != null) {
            gse.setAddress(parseAddress(addrElem));
        }
        return gse;
    }

    private SclSMV parseSMV(Element elem) {
        SclSMV smv = new SclSMV();
        smv.setLdInst(elem.getAttribute("ldInst"));
        smv.setCbName(elem.getAttribute("cbName"));
        Element addrElem = getChild(elem, "Address");
        if (addrElem != null) {
            smv.setAddress(parseAddress(addrElem));
        }
        return smv;
    }

    private SclPhysConn parsePhysConn(Element elem) {
        SclPhysConn pc = new SclPhysConn();
        pc.setType(elem.getAttribute("type"));
        NodeList pNodes = elem.getElementsByTagName("P");
        for (int i = 0; i < pNodes.getLength(); i++) {
            Element pElem = (Element) pNodes.item(i);
            String type = pElem.getAttribute("type");
            String value = pElem.getTextContent().trim();
            if ("Type".equals(type)) pc.setPlugType(value);
            else if ("Plug".equals(type)) pc.setPlug(value);
        }
        return pc;
    }

    private SclIED parseIED(Element elem) {
        SclIED ied = new SclIED();
        ied.setName(elem.getAttribute("name"));
        ied.setDesc(elem.getAttribute("desc"));
        Element servicesElem = getChild(elem, "Services");
        if (servicesElem != null) {
            ied.setServices(parseServices(servicesElem));
        }
        NodeList apNodes = elem.getElementsByTagName("AccessPoint");
        for (int i = 0; i < apNodes.getLength(); i++) {
            ied.addAccessPoint(parseAccessPoint((Element) apNodes.item(i)));
        }
        return ied;
    }

    private SclServices parseServices(Element elem) {
        SclServices svc = new SclServices();
        svc.setDynAssociation(hasChild(elem, "DynAssociation"));
        svc.setGetDirectory(hasChild(elem, "GetDirectory"));
        svc.setGetDataObjectDefinition(hasChild(elem, "GetDataObjectDefinition"));
        svc.setGetDataSetValue(hasChild(elem, "GetDataSetValue"));
        svc.setDataSetDirectory(hasChild(elem, "DataSetDirectory"));
        svc.setReadWrite(hasChild(elem, "ReadWrite"));
        svc.setFileHandling(hasChild(elem, "FileHandling"));
        svc.setGetCBValues(hasChild(elem, "GetCBValues"));
        svc.setGSEDir(hasChild(elem, "GSEDir"));
        svc.setTimerActivatedControl(hasChild(elem, "TimerActivatedControl"));
        Element confDataSet = getChild(elem, "ConfDataSet");
        if (confDataSet != null) {
            String max = confDataSet.getAttribute("max");
            if (!max.isEmpty()) svc.setConfDataSetMax(Integer.parseInt(max));
            String maxAttr = confDataSet.getAttribute("maxAttributes");
            if (!maxAttr.isEmpty()) svc.setConfDataSetMaxAttributes(Integer.parseInt(maxAttr));
        }
        Element confReport = getChild(elem, "ConfReportControl");
        if (confReport != null) {
            String max = confReport.getAttribute("max");
            if (!max.isEmpty()) svc.setConfReportControlMax(Integer.parseInt(max));
        }
        Element confLog = getChild(elem, "ConfLogControl");
        if (confLog != null) {
            String max = confLog.getAttribute("max");
            if (!max.isEmpty()) svc.setConfLogControlMax(Integer.parseInt(max));
        }
        Element goose = getChild(elem, "GOOSE");
        if (goose != null) {
            String max = goose.getAttribute("max");
            if (!max.isEmpty()) svc.setGooseMax(Integer.parseInt(max));
        }
        Element gsse = getChild(elem, "GSSE");
        if (gsse != null) {
            String max = gsse.getAttribute("max");
            if (!max.isEmpty()) svc.setGsseMax(Integer.parseInt(max));
        }
        Element confLNs = getChild(elem, "ConfLNs");
        if (confLNs != null) {
            String fixPrefix = confLNs.getAttribute("fixPrefix");
            if (!fixPrefix.isEmpty()) svc.setConfLNsFixPrefix(Boolean.parseBoolean(fixPrefix));
            String fixLnInst = confLNs.getAttribute("fixLnInst");
            if (!fixLnInst.isEmpty()) svc.setConfLNsFixLnInst(Boolean.parseBoolean(fixLnInst));
        }
        Element rptSettings = getChild(elem, "ReportSettings");
        if (rptSettings != null) {
            SclReportSettings rs = new SclReportSettings();
            rs.setBufTime(rptSettings.getAttribute("bufTime"));
            rs.setCbName(rptSettings.getAttribute("cbName"));
            rs.setRptID(rptSettings.getAttribute("rptID"));
            rs.setDatSet(rptSettings.getAttribute("datSet"));
            rs.setIntgPd(rptSettings.getAttribute("intgPd"));
            rs.setOptFields(rptSettings.getAttribute("optFields"));
            svc.setReportSettings(rs);
        }
        Element gseSettings = getChild(elem, "GSESettings");
        if (gseSettings != null) {
            SclGSESettings gs = new SclGSESettings();
            gs.setAppID(gseSettings.getAttribute("appID"));
            gs.setCbName(gseSettings.getAttribute("cbName"));
            gs.setDatSet(gseSettings.getAttribute("datSet"));
            svc.setGseSettings(gs);
        }
        return svc;
    }

    private SclAccessPoint parseAccessPoint(Element elem) {
        SclAccessPoint ap = new SclAccessPoint();
        ap.setName(elem.getAttribute("name"));
        Element serverElem = getChild(elem, "Server");
        if (serverElem != null) {
            ap.setServer(parseServer(serverElem));
        }
        return ap;
    }

    private SclServer parseServer(Element elem) {
        SclServer server = new SclServer();
        NodeList lDeviceNodes = elem.getElementsByTagName("LDevice");
        for (int i = 0; i < lDeviceNodes.getLength(); i++) {
            server.addLDevice(parseLDevice((Element) lDeviceNodes.item(i)));
        }
        return server;
    }

    private SclLDevice parseLDevice(Element elem) {
        SclLDevice ld = new SclLDevice();
        ld.setInst(elem.getAttribute("inst"));
        ld.setDesc(elem.getAttribute("desc"));
        Element ln0Elem = getChild(elem, "LN0");
        if (ln0Elem != null) {
            ld.setLn0(parseLN0(ln0Elem));
        }
        NodeList lnNodes = elem.getElementsByTagName("LN");
        for (int i = 0; i < lnNodes.getLength(); i++) {
            ld.addLn(parseLN((Element) lnNodes.item(i)));
        }
        return ld;
    }

    private SclLN0 parseLN0(Element elem) {
        SclLN0 ln0 = new SclLN0();
        ln0.setLnType(elem.getAttribute("lnType"));
        ln0.setLnClass(elem.getAttribute("lnClass"));
        ln0.setInst(elem.getAttribute("inst"));
        ln0.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("DataSet".equals(tag)) {
                ln0.addDataSet(parseDataSet(child));
            } else if ("ReportControl".equals(tag)) {
                ln0.addReportControl(parseReportControl(child));
            } else if ("LogControl".equals(tag)) {
                ln0.addLogControl(parseLogControl(child));
            } else if ("GSEControl".equals(tag)) {
                ln0.addGseControl(parseGSEControl(child));
            } else if ("SampledValueControl".equals(tag)) {
                ln0.addSampledValueControl(parseSampledValueControl(child));
            } else if ("DOI".equals(tag)) {
                ln0.addDoi(parseDOI(child));
            }
        }
        return ln0;
    }

    private SclLN parseLN(Element elem) {
        SclLN ln = new SclLN();
        ln.setLnType(elem.getAttribute("lnType"));
        ln.setLnClass(elem.getAttribute("lnClass"));
        ln.setInst(elem.getAttribute("inst"));
        ln.setPrefix(elem.getAttribute("prefix"));
        ln.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("DOI".equals(tag)) {
                ln.addDoi(parseDOI(child));
            } else if ("Inputs".equals(tag)) {
                List<SclInputs> inputsList = new ArrayList<>();
                inputsList.add(parseInputs(child));
                ln.setInputs(inputsList);
            }
        }
        return ln;
    }

    private SclDataSet parseDataSet(Element elem) {
        SclDataSet ds = new SclDataSet();
        ds.setName(elem.getAttribute("name"));
        ds.setDesc(elem.getAttribute("desc"));
        NodeList fcdaNodes = elem.getElementsByTagName("FCDA");
        for (int i = 0; i < fcdaNodes.getLength(); i++) {
            ds.addFcda(parseFCDA((Element) fcdaNodes.item(i)));
        }
        return ds;
    }

    private SclFCDA parseFCDA(Element elem) {
        SclFCDA fcda = new SclFCDA();
        fcda.setLdInst(elem.getAttribute("ldInst"));
        fcda.setLnClass(elem.getAttribute("lnClass"));
        fcda.setLnInst(elem.getAttribute("lnInst"));
        fcda.setPrefix(elem.getAttribute("prefix"));
        fcda.setDoName(elem.getAttribute("doName"));
        fcda.setDaName(elem.getAttribute("daName"));
        fcda.setFc(elem.getAttribute("fc"));
        return fcda;
    }

    private SclReportControl parseReportControl(Element elem) {
        SclReportControl rc = new SclReportControl();
        rc.setName(elem.getAttribute("name"));
        rc.setDesc(elem.getAttribute("desc"));
        rc.setRptID(elem.getAttribute("rptID"));
        rc.setDatSet(elem.getAttribute("datSet"));
        rc.setConfRev(elem.getAttribute("confRev"));
        rc.setBuffered("true".equals(elem.getAttribute("buffered")));
        rc.setBufTime(elem.getAttribute("bufTime"));
        rc.setIntgPd(elem.getAttribute("intgPd"));
        Element trgOpsElem = getChild(elem, "TrgOps");
        if (trgOpsElem != null) {
            rc.setTrgOps(parseTrgOps(trgOpsElem));
        }
        Element optFieldsElem = getChild(elem, "OptFields");
        if (optFieldsElem != null) {
            rc.setOptFields(parseOptFields(optFieldsElem));
        }
        Element rptEnabledElem = getChild(elem, "RptEnabled");
        if (rptEnabledElem != null) {
            rc.setRptEnabled(parseRptEnabled(rptEnabledElem));
        }
        return rc;
    }

    private SclTrgOps parseTrgOps(Element elem) {
        SclTrgOps trgOps = new SclTrgOps();
        trgOps.setDchg("true".equals(elem.getAttribute("dchg")));
        trgOps.setQchg("true".equals(elem.getAttribute("qchg")));
        trgOps.setDupd("true".equals(elem.getAttribute("dupd")));
        trgOps.setPeriod("true".equals(elem.getAttribute("period")));
        trgOps.setGi("true".equals(elem.getAttribute("gi")));
        return trgOps;
    }

    private SclOptFields parseOptFields(Element elem) {
        SclOptFields opt = new SclOptFields();
        opt.setDataSet("true".equals(elem.getAttribute("dataSet")));
        opt.setBufOvfl("true".equals(elem.getAttribute("bufOvfl")));
        opt.setConfigRef("true".equals(elem.getAttribute("configRef")));
        opt.setDataRef("true".equals(elem.getAttribute("dataRef")));
        opt.setEntryID("true".equals(elem.getAttribute("entryID")));
        opt.setReasonCode("true".equals(elem.getAttribute("reasonCode")));
        opt.setTimeStamp("true".equals(elem.getAttribute("timeStamp")));
        opt.setSeqNum("true".equals(elem.getAttribute("seqNum")));
        return opt;
    }

    private SclRptEnabled parseRptEnabled(Element elem) {
        SclRptEnabled rptEnabled = new SclRptEnabled();
        String max = elem.getAttribute("max");
        if (!max.isEmpty()) {
            rptEnabled.setMax(Integer.parseInt(max));
        }
        NodeList clientLNNodes = elem.getElementsByTagName("ClientLN");
        for (int i = 0; i < clientLNNodes.getLength(); i++) {
            Element clientLNElem = (Element) clientLNNodes.item(i);
            SclClientLN clientLN = new SclClientLN();
            clientLN.setIedName(clientLNElem.getAttribute("iedName"));
            clientLN.setLdInst(clientLNElem.getAttribute("ldInst"));
            clientLN.setLnClass(clientLNElem.getAttribute("lnClass"));
            clientLN.setLnInst(clientLNElem.getAttribute("lnInst"));
            rptEnabled.addClientLN(clientLN);
        }
        return rptEnabled;
    }

    private SclLogControl parseLogControl(Element elem) {
        SclLogControl lc = new SclLogControl();
        lc.setName(elem.getAttribute("name"));
        lc.setDatSet(elem.getAttribute("datSet"));
        lc.setLogName(elem.getAttribute("logName"));
        lc.setDesc(elem.getAttribute("desc"));
        Element trgOpsElem = getChild(elem, "TrgOps");
        if (trgOpsElem != null) {
            lc.setTrgOps(parseTrgOps(trgOpsElem));
        }
        return lc;
    }

    private SclGSEControl parseGSEControl(Element elem) {
        SclGSEControl gse = new SclGSEControl();
        gse.setName(elem.getAttribute("name"));
        gse.setDatSet(elem.getAttribute("datSet"));
        gse.setAppID(elem.getAttribute("appID"));
        gse.setConfRev(elem.getAttribute("confRev"));
        gse.setType(elem.getAttribute("type"));
        return gse;
    }

    private SclSampledValueControl parseSampledValueControl(Element elem) {
        SclSampledValueControl sv = new SclSampledValueControl();
        sv.setName(elem.getAttribute("name"));
        sv.setDatSet(elem.getAttribute("datSet"));
        sv.setSmvID(elem.getAttribute("smvID"));
        String smpRate = elem.getAttribute("smpRate");
        if (!smpRate.isEmpty()) sv.setSmpRate(Integer.parseInt(smpRate));
        String nofASDU = elem.getAttribute("nofASDU");
        if (!nofASDU.isEmpty()) sv.setNofASDU(Integer.parseInt(nofASDU));
        sv.setMulticast("true".equals(elem.getAttribute("multicast")));
        sv.setConfRev(elem.getAttribute("confRev"));
        Element smvOptsElem = getChild(elem, "SmvOpts");
        if (smvOptsElem != null) {
            sv.setSmvOpts(parseSmvOpts(smvOptsElem));
        }
        return sv;
    }

    private SclSmvOpts parseSmvOpts(Element elem) {
        SclSmvOpts opts = new SclSmvOpts();
        opts.setSampleRate("true".equals(elem.getAttribute("sampleRate")));
        opts.setRefreshTime("true".equals(elem.getAttribute("refreshTime")));
        opts.setSampleSynchronized("true".equals(elem.getAttribute("sampleSynchronized")));
        return opts;
    }

    private SclDOI parseDOI(Element elem) {
        SclDOI doi = new SclDOI();
        doi.setName(elem.getAttribute("name"));
        doi.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("DAI".equals(tag)) {
                doi.addDai(parseDAI(child));
            } else if ("SDI".equals(tag)) {
                doi.addSdi(parseSDI(child));
            }
        }
        return doi;
    }

    private SclDAI parseDAI(Element elem) {
        SclDAI dai = new SclDAI();
        dai.setName(elem.getAttribute("name"));
        dai.setSAddr(elem.getAttribute("sAddr"));
        Element valElem = getChild(elem, "Val");
        if (valElem != null) {
            dai.setValue(valElem.getTextContent().trim());
        }
        return dai;
    }

    private SclSDI parseSDI(Element elem) {
        SclSDI sdi = new SclSDI();
        sdi.setName(elem.getAttribute("name"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            if ("DAI".equals(child.getTagName())) {
                sdi.addDai(parseDAI(child));
            }
        }
        return sdi;
    }

    private SclInputs parseInputs(Element elem) {
        SclInputs inputs = new SclInputs();
        NodeList extRefNodes = elem.getElementsByTagName("ExtRef");
        for (int i = 0; i < extRefNodes.getLength(); i++) {
            inputs.addExtRef(parseExtRef((Element) extRefNodes.item(i)));
        }
        return inputs;
    }

    private SclExtRef parseExtRef(Element elem) {
        SclExtRef extRef = new SclExtRef();
        extRef.setIntAddr(elem.getAttribute("intAddr"));
        extRef.setDesc(elem.getAttribute("desc"));
        extRef.setIedName(elem.getAttribute("iedName"));
        extRef.setLdInst(elem.getAttribute("ldInst"));
        extRef.setLnClass(elem.getAttribute("lnClass"));
        extRef.setLnInst(elem.getAttribute("lnInst"));
        extRef.setDoName(elem.getAttribute("doName"));
        extRef.setDaName(elem.getAttribute("daName"));
        extRef.setServiceType(elem.getAttribute("serviceType"));
        return extRef;
    }

    private SclDataTypeTemplates parseDataTypeTemplates(Element elem) {
        if (elem == null) return null;
        SclDataTypeTemplates templates = new SclDataTypeTemplates();
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("LNodeType".equals(tag)) {
                templates.addLNodeType(parseLNodeType(child));
            } else if ("DOType".equals(tag)) {
                templates.addDoType(parseDOType(child));
            } else if ("DAType".equals(tag)) {
                templates.addDaType(parseDAType(child));
            } else if ("EnumType".equals(tag)) {
                templates.addEnumType(parseEnumType(child));
            }
        }
        return templates;
    }

    private SclLNodeType parseLNodeType(Element elem) {
        SclLNodeType lnt = new SclLNodeType();
        lnt.setId(elem.getAttribute("id"));
        lnt.setLnClass(elem.getAttribute("lnClass"));
        lnt.setDesc(elem.getAttribute("desc"));
        NodeList doNodes = elem.getElementsByTagName("DO");
        for (int i = 0; i < doNodes.getLength(); i++) {
            Element doElem = (Element) doNodes.item(i);
            SclDO doObj = new SclDO();
            doObj.setName(doElem.getAttribute("name"));
            doObj.setType(doElem.getAttribute("type"));
            lnt.addDo(doObj);
        }
        return lnt;
    }

    private SclDOType parseDOType(Element elem) {
        SclDOType dot = new SclDOType();
        dot.setId(elem.getAttribute("id"));
        dot.setCdc(elem.getAttribute("cdc"));
        dot.setDesc(elem.getAttribute("desc"));
        NodeList children = elem.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (!(children.item(i) instanceof Element)) continue;
            Element child = (Element) children.item(i);
            String tag = child.getTagName();
            if ("DA".equals(tag)) {
                dot.addDa(parseDA(child));
            } else if ("SDO".equals(tag)) {
                dot.addSdo(parseSDO(child));
            }
        }
        return dot;
    }

    private SclDA parseDA(Element elem) {
        SclDA da = new SclDA();
        da.setName(elem.getAttribute("name"));
        da.setType(elem.getAttribute("type"));
        da.setBType(elem.getAttribute("bType"));
        da.setFc(elem.getAttribute("fc"));
        da.setDchg("true".equals(elem.getAttribute("dchg")));
        da.setQchg("true".equals(elem.getAttribute("qchg")));
        da.setDupd("true".equals(elem.getAttribute("dupd")));
        String count = elem.getAttribute("count");
        if (!count.isEmpty()) da.setCount(Integer.parseInt(count));
        return da;
    }

    private SclSDO parseSDO(Element elem) {
        SclSDO sdo = new SclSDO();
        sdo.setName(elem.getAttribute("name"));
        sdo.setType(elem.getAttribute("type"));
        return sdo;
    }

    private SclDAType parseDAType(Element elem) {
        SclDAType dat = new SclDAType();
        dat.setId(elem.getAttribute("id"));
        dat.setDesc(elem.getAttribute("desc"));
        NodeList bdaNodes = elem.getElementsByTagName("BDA");
        for (int i = 0; i < bdaNodes.getLength(); i++) {
            dat.addBda(parseBDA((Element) bdaNodes.item(i)));
        }
        return dat;
    }

    private SclBDA parseBDA(Element elem) {
        SclBDA bda = new SclBDA();
        bda.setName(elem.getAttribute("name"));
        bda.setType(elem.getAttribute("type"));
        bda.setBType(elem.getAttribute("bType"));
        String count = elem.getAttribute("count");
        if (!count.isEmpty()) bda.setCount(Integer.parseInt(count));
        return bda;
    }

    private SclEnumType parseEnumType(Element elem) {
        SclEnumType et = new SclEnumType();
        et.setId(elem.getAttribute("id"));
        et.setDesc(elem.getAttribute("desc"));
        NodeList enumValNodes = elem.getElementsByTagName("EnumVal");
        for (int i = 0; i < enumValNodes.getLength(); i++) {
            et.addEnumVal(parseEnumVal((Element) enumValNodes.item(i)));
        }
        return et;
    }

    private SclEnumVal parseEnumVal(Element elem) {
        SclEnumVal ev = new SclEnumVal();
        String ord = elem.getAttribute("ord");
        if (!ord.isEmpty()) ev.setOrd(Integer.parseInt(ord));
        ev.setValue(elem.getTextContent().trim());
        return ev;
    }

    private Element getChild(Element parent, String tagName) {
        if (parent == null) return null;
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i) instanceof Element) {
                Element child = (Element) children.item(i);
                if (child.getTagName().equals(tagName)) {
                    return child;
                }
            }
        }
        return null;
    }

    private boolean hasChild(Element parent, String tagName) {
        return getChild(parent, tagName) != null;
    }

    private String getTextContent(Element elem) {
        if (elem == null) return null;
        return elem.getTextContent().trim();
    }
}
