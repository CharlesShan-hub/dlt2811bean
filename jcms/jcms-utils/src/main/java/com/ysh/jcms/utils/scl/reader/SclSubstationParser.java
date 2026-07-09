package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.substation.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

public class SclSubstationParser {

    public static SclSubstation parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclSubstation substation = new SclSubstation();
        substation.name(getAttr(reader, "name"));
        substation.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        substation.addLNode(parseLNode(reader));
                        break;
                    case "PowerTransformer":
                        substation.addTransformer(parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment":
                        substation.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "VoltageLevel":
                        substation.addVoltageLevel(parseVoltageLevel(reader));
                        break;
                    case "Function":
                        substation.addFunction(parseFunction(reader));
                        break;
                    default:
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
                    case "LNode":
                        vl.addLNode(parseLNode(reader));
                        break;
                    case "Voltage":
                        vl.voltage(parseVoltage(reader));
                        break;
                    case "PowerTransformer":
                        vl.addTransformer(parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment":
                        vl.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "Bay":
                        vl.addBay(parseBay(reader));
                        break;
                    case "Function":
                        vl.addFunction(parseFunction(reader));
                        break;
                    default:
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
                    case "LNode":
                        bay.addLNode(parseLNode(reader));
                        break;
                    case "PowerTransformer":
                        bay.addTransformer(parsePowerTransformer(reader));
                        break;
                    case "GeneralEquipment":
                        bay.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment":
                        bay.addEquipment(parseConductingEquipment(reader));
                        break;
                    case "ConnectivityNode":
                        bay.addConnectivityNode(parseConnectivityNode(reader));
                        break;
                    case "Function":
                        bay.addFunction(parseFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return bay;
    }

    // ==================== ConductingEquipment ====================

    private static SclConductingEquipment parseConductingEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclConductingEquipment ce = new SclConductingEquipment();
        ce.name(getAttr(reader, "name"));
        ce.desc(getAttr(reader, "desc"));
        ce.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        ce.addLNode(parseLNode(reader));
                        break;
                    case "Terminal":
                        ce.addTerminal(parseTerminal(reader));
                        break;
                    case "SubEquipment":
                        ce.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction":
                        ce.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ce;
    }

    // ==================== SubEquipment ====================

    private static SclSubEquipment parseSubEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclSubEquipment se = new SclSubEquipment();
        se.name(getAttr(reader, "name"));
        se.desc(getAttr(reader, "desc"));
        se.phase(getAttr(reader, "phase"));
        se.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        se.addLNode(parseLNode(reader));
                        break;
                    case "EqFunction":
                        se.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return se;
    }

    // ==================== PowerTransformer ====================

    private static SclPowerTransformer parsePowerTransformer(XMLStreamReader reader) throws XMLStreamException {
        SclPowerTransformer ptr = new SclPowerTransformer();
        ptr.name(getAttr(reader, "name"));
        ptr.desc(getAttr(reader, "desc"));
        ptr.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        ptr.addLNode(parseLNode(reader));
                        break;
                    case "TransformerWinding":
                        ptr.addWinding(parseTransformerWinding(reader));
                        break;
                    case "SubEquipment":
                        ptr.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction":
                        ptr.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ptr;
    }

    // ==================== TransformerWinding ====================

    private static SclTransformerWinding parseTransformerWinding(XMLStreamReader reader) throws XMLStreamException {
        SclTransformerWinding ptw = new SclTransformerWinding();
        ptw.name(getAttr(reader, "name"));
        ptw.desc(getAttr(reader, "desc"));
        ptw.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        ptw.addLNode(parseLNode(reader));
                        break;
                    case "Terminal":
                        ptw.addTerminal(parseTerminal(reader));
                        break;
                    case "SubEquipment":
                        ptw.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "TapChanger":
                        ptw.tapChanger(parseTapChanger(reader));
                        break;
                    case "NeutralPoint":
                        ptw.neutralPoint(parseTerminal(reader));
                        break;
                    case "EqFunction":
                        ptw.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ptw;
    }

    // ==================== TapChanger ====================

    private static SclTapChanger parseTapChanger(XMLStreamReader reader) throws XMLStreamException {
        SclTapChanger tap = new SclTapChanger();
        tap.name(getAttr(reader, "name"));
        tap.desc(getAttr(reader, "desc"));
        tap.type(getAttr(reader, "type"));
        tap.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        tap.addLNode(parseLNode(reader));
                        break;
                    case "SubEquipment":
                        tap.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction":
                        tap.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return tap;
    }

    // ==================== GeneralEquipment ====================

    private static SclGeneralEquipment parseGeneralEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclGeneralEquipment ge = new SclGeneralEquipment();
        ge.name(getAttr(reader, "name"));
        ge.desc(getAttr(reader, "desc"));
        ge.type(getAttr(reader, "type"));
        ge.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        ge.addLNode(parseLNode(reader));
                        break;
                    case "EqFunction":
                        ge.addEqFunction(parseEqFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ge;
    }

    // ==================== Function / SubFunction ====================

    private static SclFunction parseFunction(XMLStreamReader reader) throws XMLStreamException {
        SclFunction func = new SclFunction();
        func.name(getAttr(reader, "name"));
        func.desc(getAttr(reader, "desc"));
        func.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        func.addLNode(parseLNode(reader));
                        break;
                    case "SubFunction":
                        func.addSubFunction(parseSubFunction(reader));
                        break;
                    case "GeneralEquipment":
                        func.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment":
                        func.addConductingEquipment(parseConductingEquipment(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return func;
    }

    private static SclSubFunction parseSubFunction(XMLStreamReader reader) throws XMLStreamException {
        SclSubFunction subFunc = new SclSubFunction();
        subFunc.name(getAttr(reader, "name"));
        subFunc.desc(getAttr(reader, "desc"));
        subFunc.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        subFunc.addLNode(parseLNode(reader));
                        break;
                    case "GeneralEquipment":
                        subFunc.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment":
                        subFunc.addConductingEquipment(parseConductingEquipment(reader));
                        break;
                    case "SubFunction":
                        subFunc.addSubFunction(parseSubFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return subFunc;
    }

    // ==================== EqFunction / EqSubFunction ====================

    private static SclEqFunction parseEqFunction(XMLStreamReader reader) throws XMLStreamException {
        SclEqFunction eqFunc = new SclEqFunction();
        eqFunc.name(getAttr(reader, "name"));
        eqFunc.desc(getAttr(reader, "desc"));
        eqFunc.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        eqFunc.addLNode(parseLNode(reader));
                        break;
                    case "GeneralEquipment":
                        eqFunc.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "EqSubFunction":
                        eqFunc.addEqSubFunction(parseEqSubFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return eqFunc;
    }

    private static SclEqSubFunction parseEqSubFunction(XMLStreamReader reader) throws XMLStreamException {
        SclEqSubFunction eqSub = new SclEqSubFunction();
        eqSub.name(getAttr(reader, "name"));
        eqSub.desc(getAttr(reader, "desc"));
        eqSub.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode":
                        eqSub.addLNode(parseLNode(reader));
                        break;
                    case "GeneralEquipment":
                        eqSub.addGeneralEquipment(parseGeneralEquipment(reader));
                        break;
                    case "EqSubFunction":
                        eqSub.addEqSubFunction(parseEqSubFunction(reader));
                        break;
                    default:
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return eqSub;
    }

    // ==================== Terminal ====================

    private static SclTerminal parseTerminal(XMLStreamReader reader) throws XMLStreamException {
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
