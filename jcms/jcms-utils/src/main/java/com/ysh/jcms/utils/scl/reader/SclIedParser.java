package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.ied.SclAccessControl;
import com.ysh.jcms.utils.scl.model.ied.SclAccessPoint;
import com.ysh.jcms.utils.scl.model.ied.SclAssociation;
import com.ysh.jcms.utils.scl.model.ied.SclCertificate;
import com.ysh.jcms.utils.scl.model.ied.SclIED;
import com.ysh.jcms.utils.scl.model.ied.SclLDevice;
import com.ysh.jcms.utils.scl.model.ied.SclLN;
import com.ysh.jcms.utils.scl.model.ied.SclServer;
import com.ysh.jcms.utils.scl.model.ied.SclServerAt;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/**
 * Parses the structural skeleton of {@code <IED>}: AccessPoint / Server /
 * LDevice / LN.
 */
public class SclIedParser {

    private SclIedParser() {
    }

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
                        ied.services(SclServicesParser.parseServices(reader));
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
                    case "GOOSESecurity" :
                        ap.addGooseSecurity(parseCertificate(reader));
                        break;
                    case "SMVSecurity" :
                        ap.addSmvSecurity(parseCertificate(reader));
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

    /**
     * Parses {@code <GOOSESecurity>} / {@code <SMVSecurity>} certificates
     * (tCertificate).
     */
    private static SclCertificate parseCertificate(XMLStreamReader reader) throws XMLStreamException {
        SclCertificate cert = new SclCertificate();
        cert.xferNumber(getAttr(reader, "xferNumber"));
        cert.serialNumber(getAttr(reader, "serialNumber"));
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Subject" :
                        cert.subjectCommonName(getAttr(reader, "commonName"));
                        cert.subjectIdHierarchy(getAttr(reader, "idHierarchy"));
                        skipElement(reader);
                        break;
                    case "IssuerName" :
                        cert.issuerCommonName(getAttr(reader, "commonName"));
                        cert.issuerIdHierarchy(getAttr(reader, "idHierarchy"));
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
        return cert;
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
        ld.confRev(getAttr(reader, "confRev"));

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

    // ==================== LN0 / LN ====================

    private static SclLN parseLN0(XMLStreamReader reader) throws XMLStreamException {
        SclLN ln = parseLN(reader);
        // LN0 has additional elements: GSEControl, SampledValueControl, SettingControl
        // These are parsed inside parseLN -> parseAnyLN
        return ln;
    }

    private static SclLN parseLN(XMLStreamReader reader) throws XMLStreamException {
        SclLN ln = new SclLN();
        ln.lnType(getAttr(reader, "lnType"));
        ln.iedType(getAttr(reader, "iedType"));
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
                        ln.addDataSet(SclInstanceParser.parseDataSet(reader));
                        break;
                    case "ReportControl" :
                        ln.addReportControl(SclControlBlockParser.parseReportControl(reader));
                        break;
                    case "LogControl" :
                        ln.addLogControl(SclControlBlockParser.parseLogControl(reader));
                        break;
                    case "DOI" :
                        ln.addDoi(SclInstanceParser.parseDOI(reader));
                        break;
                    case "Inputs" :
                        ln.addInput(SclInstanceParser.parseInputs(reader));
                        break;
                    case "GSEControl" :
                        ln.addGseControl(SclControlBlockParser.parseGSEControl(reader));
                        break;
                    case "SampledValueControl" :
                        ln.addSvControl(SclControlBlockParser.parseSampledValueControl(reader));
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
}
