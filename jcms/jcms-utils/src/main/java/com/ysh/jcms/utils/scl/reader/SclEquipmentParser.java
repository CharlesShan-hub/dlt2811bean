package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.model.substation.SclConductingEquipment;
import com.ysh.jcms.utils.scl.model.substation.SclGeneralEquipment;
import com.ysh.jcms.utils.scl.model.substation.SclPowerTransformer;
import com.ysh.jcms.utils.scl.model.substation.SclSubEquipment;
import com.ysh.jcms.utils.scl.model.substation.SclTapChanger;
import com.ysh.jcms.utils.scl.model.substation.SclTransformerWinding;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/**
 * Parses primary equipment under Substation: ConductingEquipment / SubEquipment
 * / PowerTransformer / TransformerWinding / TapChanger / GeneralEquipment.
 */
public class SclEquipmentParser {

    private SclEquipmentParser() {
    }

    public static SclConductingEquipment parseConductingEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclConductingEquipment ce = new SclConductingEquipment();
        ce.name(getAttr(reader, "name"));
        ce.desc(getAttr(reader, "desc"));
        ce.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        ce.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "Terminal" :
                        ce.addTerminal(SclSubstationParser.parseTerminal(reader));
                        break;
                    case "SubEquipment" :
                        ce.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction" :
                        ce.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ce;
    }

    public static SclSubEquipment parseSubEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclSubEquipment se = new SclSubEquipment();
        se.name(getAttr(reader, "name"));
        se.desc(getAttr(reader, "desc"));
        se.phase(getAttr(reader, "phase"));
        se.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        se.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "EqFunction" :
                        se.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return se;
    }

    public static SclPowerTransformer parsePowerTransformer(XMLStreamReader reader) throws XMLStreamException {
        SclPowerTransformer ptr = new SclPowerTransformer();
        ptr.name(getAttr(reader, "name"));
        ptr.desc(getAttr(reader, "desc"));
        ptr.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        ptr.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "TransformerWinding" :
                        ptr.addWinding(parseTransformerWinding(reader));
                        break;
                    case "SubEquipment" :
                        ptr.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction" :
                        ptr.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ptr;
    }

    public static SclTransformerWinding parseTransformerWinding(XMLStreamReader reader) throws XMLStreamException {
        SclTransformerWinding ptw = new SclTransformerWinding();
        ptw.name(getAttr(reader, "name"));
        ptw.desc(getAttr(reader, "desc"));
        ptw.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        ptw.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "Terminal" :
                        ptw.addTerminal(SclSubstationParser.parseTerminal(reader));
                        break;
                    case "SubEquipment" :
                        ptw.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "TapChanger" :
                        ptw.tapChanger(parseTapChanger(reader));
                        break;
                    case "NeutralPoint" :
                        ptw.neutralPoint(SclSubstationParser.parseTerminal(reader));
                        break;
                    case "EqFunction" :
                        ptw.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ptw;
    }

    public static SclTapChanger parseTapChanger(XMLStreamReader reader) throws XMLStreamException {
        SclTapChanger tap = new SclTapChanger();
        tap.name(getAttr(reader, "name"));
        tap.desc(getAttr(reader, "desc"));
        tap.type(getAttr(reader, "type"));
        tap.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        tap.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "SubEquipment" :
                        tap.addSubEquipment(parseSubEquipment(reader));
                        break;
                    case "EqFunction" :
                        tap.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return tap;
    }

    public static SclGeneralEquipment parseGeneralEquipment(XMLStreamReader reader) throws XMLStreamException {
        SclGeneralEquipment ge = new SclGeneralEquipment();
        ge.name(getAttr(reader, "name"));
        ge.desc(getAttr(reader, "desc"));
        ge.type(getAttr(reader, "type"));
        ge.virtual(boolAttr(reader, "virtual"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        ge.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "EqFunction" :
                        ge.addEqFunction(SclFunctionParser.parseEqFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ge;
    }
}
