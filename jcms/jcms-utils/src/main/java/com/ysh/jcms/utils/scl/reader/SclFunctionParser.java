package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.model.substation.SclEqFunction;
import com.ysh.jcms.utils.scl.model.substation.SclEqSubFunction;
import com.ysh.jcms.utils.scl.model.substation.SclFunction;
import com.ysh.jcms.utils.scl.model.substation.SclSubFunction;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/** Parses functional nodes under Substation: Function / SubFunction / EqFunction / EqSubFunction. */
public class SclFunctionParser {

    private SclFunctionParser() {
    }

    public static SclFunction parseFunction(XMLStreamReader reader) throws XMLStreamException {
        SclFunction func = new SclFunction();
        func.name(getAttr(reader, "name"));
        func.desc(getAttr(reader, "desc"));
        func.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        func.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "SubFunction" :
                        func.addSubFunction(parseSubFunction(reader));
                        break;
                    case "GeneralEquipment" :
                        func.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment" :
                        func.addConductingEquipment(SclEquipmentParser.parseConductingEquipment(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return func;
    }

    public static SclSubFunction parseSubFunction(XMLStreamReader reader) throws XMLStreamException {
        SclSubFunction subFunc = new SclSubFunction();
        subFunc.name(getAttr(reader, "name"));
        subFunc.desc(getAttr(reader, "desc"));
        subFunc.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        subFunc.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "GeneralEquipment" :
                        subFunc.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "ConductingEquipment" :
                        subFunc.addConductingEquipment(SclEquipmentParser.parseConductingEquipment(reader));
                        break;
                    case "SubFunction" :
                        subFunc.addSubFunction(parseSubFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return subFunc;
    }

    public static SclEqFunction parseEqFunction(XMLStreamReader reader) throws XMLStreamException {
        SclEqFunction eqFunc = new SclEqFunction();
        eqFunc.name(getAttr(reader, "name"));
        eqFunc.desc(getAttr(reader, "desc"));
        eqFunc.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        eqFunc.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "GeneralEquipment" :
                        eqFunc.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "EqSubFunction" :
                        eqFunc.addEqSubFunction(parseEqSubFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return eqFunc;
    }

    public static SclEqSubFunction parseEqSubFunction(XMLStreamReader reader) throws XMLStreamException {
        SclEqSubFunction eqSub = new SclEqSubFunction();
        eqSub.name(getAttr(reader, "name"));
        eqSub.desc(getAttr(reader, "desc"));
        eqSub.type(getAttr(reader, "type"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNode" :
                        eqSub.addLNode(SclSubstationParser.parseLNode(reader));
                        break;
                    case "GeneralEquipment" :
                        eqSub.addGeneralEquipment(SclEquipmentParser.parseGeneralEquipment(reader));
                        break;
                    case "EqSubFunction" :
                        eqSub.addEqSubFunction(parseEqSubFunction(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return eqSub;
    }
}
