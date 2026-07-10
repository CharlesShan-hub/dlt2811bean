package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.template.*;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

public class SclTemplateParser {

    public static SclDataTypeTemplates parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclDataTypeTemplates templates = new SclDataTypeTemplates();

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "LNodeType" :
                        templates.addLNodeType(parseLNodeType(reader));
                        break;
                    case "DOType" :
                        templates.addDoType(parseDOType(reader));
                        break;
                    case "DAType" :
                        templates.addDaType(parseDAType(reader));
                        break;
                    case "EnumType" :
                        templates.addEnumType(parseEnumType(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return templates;
    }

    private static SclLNodeType parseLNodeType(XMLStreamReader reader) throws XMLStreamException {
        SclLNodeType lnt = new SclLNodeType();
        lnt.id(getAttr(reader, "id"));
        lnt.lnClass(getAttr(reader, "lnClass"));
        lnt.desc(getAttr(reader, "desc"));
        lnt.iedType(getAttr(reader, "iedType"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("DO".equals(reader.getLocalName())) {
                    lnt.addDo(parseDO(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return lnt;
    }

    private static SclDO parseDO(XMLStreamReader reader) throws XMLStreamException {
        SclDO doObj = new SclDO();
        doObj.name(getAttr(reader, "name"));
        doObj.desc(getAttr(reader, "desc"));
        doObj.type(getAttr(reader, "type"));
        doObj.accessControl(getAttr(reader, "accessControl"));
        doObj.transient_(boolAttr(reader, "transient"));
        skipElement(reader);
        return doObj;
    }

    private static SclDOType parseDOType(XMLStreamReader reader) throws XMLStreamException {
        SclDOType doType = new SclDOType();
        doType.id(getAttr(reader, "id"));
        doType.desc(getAttr(reader, "desc"));
        doType.cdc(getAttr(reader, "cdc"));
        doType.iedType(getAttr(reader, "iedType"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SDO" :
                        doType.addSdo(parseSDO(reader));
                        break;
                    case "DA" :
                        doType.addDa(parseDA(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return doType;
    }

    private static SclSDO parseSDO(XMLStreamReader reader) throws XMLStreamException {
        SclSDO sdo = new SclSDO();
        sdo.name(getAttr(reader, "name"));
        sdo.desc(getAttr(reader, "desc"));
        sdo.type(getAttr(reader, "type"));
        sdo.count(intAttr(reader, "count"));
        skipElement(reader);
        return sdo;
    }

    private static SclDA parseDA(XMLStreamReader reader) throws XMLStreamException {
        SclDA da = new SclDA();
        da.name(getAttr(reader, "name"));
        da.desc(getAttr(reader, "desc"));
        da.fc(getAttr(reader, "fc"));
        da.bType(getAttr(reader, "bType"));
        da.type(getAttr(reader, "type"));
        da.valKind(getAttr(reader, "valKind"));
        da.sAddr(getAttr(reader, "sAddr"));
        da.count(intAttr(reader, "count"));
        da.valImport(boolAttr(reader, "valImport"));
        da.dchg(boolAttr(reader, "dchg"));
        da.qchg(boolAttr(reader, "qchg"));
        da.dupd(boolAttr(reader, "dupd"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Val" :
                        da.addVal(parseValChild(reader));
                        break;
                    case "ProtNs" :
                        da.addProtNs(parseProtNs(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return da;
    }

    private static SclDAType parseDAType(XMLStreamReader reader) throws XMLStreamException {
        SclDAType daType = new SclDAType();
        daType.id(getAttr(reader, "id"));
        daType.desc(getAttr(reader, "desc"));
        daType.iedType(getAttr(reader, "iedType"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "BDA" :
                        daType.addBda(parseBDA(reader));
                        break;
                    case "ProtNs" :
                        daType.addProtNs(parseProtNs(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return daType;
    }

    private static SclBDA parseBDA(XMLStreamReader reader) throws XMLStreamException {
        SclBDA bda = new SclBDA();
        bda.name(getAttr(reader, "name"));
        bda.desc(getAttr(reader, "desc"));
        bda.bType(getAttr(reader, "bType"));
        bda.type(getAttr(reader, "type"));
        bda.valKind(getAttr(reader, "valKind"));
        bda.sAddr(getAttr(reader, "sAddr"));
        bda.count(intAttr(reader, "count"));
        bda.valImport(boolAttr(reader, "valImport"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Val".equals(reader.getLocalName())) {
                    bda.addVal(parseValChild(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return bda;
    }

    private static SclEnumType parseEnumType(XMLStreamReader reader) throws XMLStreamException {
        SclEnumType enumType = new SclEnumType();
        enumType.id(getAttr(reader, "id"));
        enumType.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("EnumVal".equals(reader.getLocalName())) {
                    enumType.addEnumVal(parseEnumVal(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return enumType;
    }

    private static SclEnumVal parseEnumVal(XMLStreamReader reader) throws XMLStreamException {
        SclEnumVal enumVal = new SclEnumVal();
        enumVal.ord(intAttr(reader, "ord", 0));
        enumVal.desc(getAttr(reader, "desc"));
        enumVal.value(parseSimpleElementText(reader));
        return enumVal;
    }

    private static SclProtNs parseProtNs(XMLStreamReader reader) throws XMLStreamException {
        SclProtNs protNs = new SclProtNs();
        protNs.type(getAttr(reader, "type"));
        protNs.value(parseSimpleElementText(reader));
        return protNs;
    }
}
