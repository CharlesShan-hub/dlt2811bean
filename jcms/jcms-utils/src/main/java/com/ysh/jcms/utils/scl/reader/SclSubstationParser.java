package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.substation.SclBay;
import com.ysh.jcms.utils.scl.model.substation.SclConnectivityNode;
import com.ysh.jcms.utils.scl.model.substation.SclLNode;
import com.ysh.jcms.utils.scl.model.substation.SclSubstation;
import com.ysh.jcms.utils.scl.model.substation.SclTerminal;
import com.ysh.jcms.utils.scl.model.substation.SclVoltage;
import com.ysh.jcms.utils.scl.model.substation.SclVoltageLevel;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/**
 * Parses the container structure of {@code <Substation>}: VoltageLevel / Bay /
 * Terminal / ConnectivityNode / LNode.
 */
public class SclSubstationParser {

    private SclSubstationParser() {
    }

    public static SclSubstation parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclSubstation substation = new SclSubstation();
        substation.name(getAttr(reader, "name"));
        substation.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        substation.addLNode(parseLNode(reader));
                        break;
                    case "PowerTransformer" :
                        substation.addTransformer(SclEquipmentParser.parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment" :
                        substation.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "VoltageLevel" :
                        substation.addVoltageLevel(parseVoltageLevel(reader));
                        break;
                    case "Function" :
                        substation.addFunction(SclFunctionParser.parseFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return substation;
    }

    // ==================== LNode ====================

    public static SclLNode parseLNode(XMLStreamReader reader) throws XMLStreamException {
        SclLNode lNode = new SclLNode();
        lNode.ldInst(getAttr(reader, "ldInst"));
        lNode.lnClass(getAttr(reader, "lnClass"));
        lNode.lnInst(getAttr(reader, "lnInst"));
        lNode.iedName(getAttr(reader, "iedName"));
        lNode.prefix(getAttr(reader, "prefix"));
        lNode.desc(getAttr(reader, "desc"));
        lNode.lnType(getAttr(reader, "lnType"));
        skipElement(reader);
        return lNode;
    }

    // ==================== VoltageLevel ====================

    private static SclVoltageLevel parseVoltageLevel(XMLStreamReader reader) throws XMLStreamException {
        SclVoltageLevel vl = new SclVoltageLevel();
        vl.name(getAttr(reader, "name"));
        vl.desc(getAttr(reader, "desc"));
        vl.nomFreq(getAttr(reader, "nomFreq"));
        vl.numPhases(intAttr(reader, "numPhases"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        vl.addLNode(parseLNode(reader));
                        break;
                    case "Voltage" :
                        vl.voltage(parseVoltage(reader));
                        break;
                    case "PowerTransformer" :
                        vl.addTransformer(SclEquipmentParser.parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment" :
                        vl.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "Bay" :
                        vl.addBay(parseBay(reader));
                        break;
                    case "Function" :
                        vl.addFunction(SclFunctionParser.parseFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return vl;
    }

    // ==================== Voltage ====================

    private static SclVoltage parseVoltage(XMLStreamReader reader) throws XMLStreamException {
        SclVoltage voltage = new SclVoltage();
        voltage.multiplier(getAttr(reader, "multiplier"));
        voltage.unit(getAttr(reader, "unit"));
        voltage.value(parseSimpleElementText(reader));
        return voltage;
    }

    // ==================== Bay ====================

    private static SclBay parseBay(XMLStreamReader reader) throws XMLStreamException {
        SclBay bay = new SclBay();
        bay.name(getAttr(reader, "name"));
        bay.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        bay.addLNode(parseLNode(reader));
                        break;
                    case "PowerTransformer" :
                        bay.addTransformer(SclEquipmentParser.parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment" :
                        bay.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment" :
                        bay.addEquipment(SclEquipmentParser.parseConductingEquipment(reader));
                        break;
                    case "ConnectivityNode" :
                        bay.addConnectivityNode(parseConnectivityNode(reader));
                        break;
                    case "Function" :
                        bay.addFunction(SclFunctionParser.parseFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return bay;
    }

    // ==================== Terminal ====================

    public static SclTerminal parseTerminal(XMLStreamReader reader) throws XMLStreamException {
        SclTerminal terminal = new SclTerminal();
        terminal.name(getAttr(reader, "name"));
        terminal.connectivityNode(getAttr(reader, "connectivityNode"));
        terminal.processName(getAttr(reader, "processName"));
        terminal.substationName(getAttr(reader, "substationName"));
        terminal.voltageLevelName(getAttr(reader, "voltageLevelName"));
        terminal.bayName(getAttr(reader, "bayName"));
        terminal.cNodeName(getAttr(reader, "cNodeName"));
        terminal.lineName(getAttr(reader, "lineName"));
        skipElement(reader);
        return terminal;
    }

    // ==================== ConnectivityNode ====================

    private static SclConnectivityNode parseConnectivityNode(XMLStreamReader reader) throws XMLStreamException {
        SclConnectivityNode cn = new SclConnectivityNode();
        cn.name(getAttr(reader, "name"));
        cn.pathName(getAttr(reader, "pathName"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("LNode".equals(reader.getLocalName())) {
                    cn.addLNode(parseLNode(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return cn;
    }
}
