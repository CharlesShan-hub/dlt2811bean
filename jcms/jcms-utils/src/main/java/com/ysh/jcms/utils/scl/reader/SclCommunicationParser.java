package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.communication.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

public class SclCommunicationParser {

    public static SclCommunication parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclCommunication communication = new SclCommunication();

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("SubNetwork".equals(reader.getLocalName())) {
                    communication.addSubNetwork(parseSubNetwork(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return communication;
    }

    private static SclSubNetwork parseSubNetwork(XMLStreamReader reader) throws XMLStreamException {
        SclSubNetwork sn = new SclSubNetwork();
        sn.name(getAttr(reader, "name"));
        sn.desc(getAttr(reader, "desc"));
        sn.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Text" :
                        sn.text(parseTextChild(reader));
                        break;
                    case "BitRate" :
                        sn.bitRateUnit(getAttr(reader, "unit"));
                        sn.bitRate(parseSimpleElementText(reader));
                        break;
                    case "ConnectedAP" :
                        sn.addConnectedAP(parseConnectedAP(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return sn;
    }

    private static SclConnectedAP parseConnectedAP(XMLStreamReader reader) throws XMLStreamException {
        SclConnectedAP cap = new SclConnectedAP();
        cap.iedName(getAttr(reader, "iedName"));
        cap.apName(getAttr(reader, "apName"));
        cap.redProt(getAttr(reader, "redProt"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Address" :
                        parseAddressChildren(reader, cap);
                        break;
                    case "GSE" :
                        cap.addGse(parseGSE(reader));
                        break;
                    case "SMV" :
                        cap.addSmv(parseSMV(reader));
                        break;
                    case "PhysConn" :
                        cap.addPhysConn(parsePhysConn(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return cap;
    }

    private static void parseAddressChildren(XMLStreamReader reader, SclConnectedAP cap) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    cap.addAddress(parseP(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    private static SclAddress parseP(XMLStreamReader reader) throws XMLStreamException {
        SclAddress addr = new SclAddress();
        addr.type(getAttr(reader, "type"));
        addr.value(parseSimpleElementText(reader));
        return addr;
    }

    private static SclGSE parseGSE(XMLStreamReader reader) throws XMLStreamException {
        SclGSE gse = new SclGSE();
        gse.ldInst(getAttr(reader, "ldInst"));
        gse.cbName(getAttr(reader, "cbName"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Address" :
                        parseGseAddresses(reader, gse);
                        break;
                    case "MinTime" :
                        gse.minTime(parseSimpleElementText(reader));
                        break;
                    case "MaxTime" :
                        gse.maxTime(parseSimpleElementText(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return gse;
    }

    private static void parseGseAddresses(XMLStreamReader reader, SclGSE gse) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    gse.addAddress(parseP(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    private static SclSMV parseSMV(XMLStreamReader reader) throws XMLStreamException {
        SclSMV smv = new SclSMV();
        smv.ldInst(getAttr(reader, "ldInst"));
        smv.cbName(getAttr(reader, "cbName"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Address".equals(reader.getLocalName())) {
                    parseSmvAddresses(reader, smv);
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return smv;
    }

    private static void parseSmvAddresses(XMLStreamReader reader, SclSMV smv) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    smv.addAddress(parseP(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    private static SclPhysConn parsePhysConn(XMLStreamReader reader) throws XMLStreamException {
        SclPhysConn physConn = new SclPhysConn();
        physConn.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("P".equals(reader.getLocalName())) {
                    SclAddress p = new SclAddress();
                    p.type(getAttr(reader, "type"));
                    p.value(parseSimpleElementText(reader));
                    physConn.addP(p);
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return physConn;
    }
}
