package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.model.input.SclDataSet;
import com.ysh.jcms.utils.scl.model.input.SclExtRef;
import com.ysh.jcms.utils.scl.model.input.SclFCDA;
import com.ysh.jcms.utils.scl.model.input.SclInput;
import com.ysh.jcms.utils.scl.model.instance.SclDAI;
import com.ysh.jcms.utils.scl.model.instance.SclDOI;
import com.ysh.jcms.utils.scl.model.instance.SclSDI;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

/**
 * Parses data content under LN: DataSet / FCDA / Inputs / ExtRef / DOI / SDI /
 * DAI.
 */
public class SclInstanceParser {

    private SclInstanceParser() {
    }

    // ==================== DataSet / FCDA ====================

    public static SclDataSet parseDataSet(XMLStreamReader reader) throws XMLStreamException {
        SclDataSet ds = new SclDataSet();
        ds.name(getAttr(reader, "name"));
        ds.desc(getAttr(reader, "desc"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("FCDA".equals(reader.getLocalName())) {
                    ds.addFcda(parseFCDA(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return ds;
    }

    private static SclFCDA parseFCDA(XMLStreamReader reader) throws XMLStreamException {
        SclFCDA fcda = new SclFCDA();
        fcda.ldInst(getAttr(reader, "ldInst"));
        fcda.prefix(getAttr(reader, "prefix"));
        fcda.lnClass(getAttr(reader, "lnClass"));
        fcda.lnInst(getAttr(reader, "lnInst"));
        fcda.doName(getAttr(reader, "doName"));
        fcda.daName(getAttr(reader, "daName"));
        fcda.fc(getAttr(reader, "fc"));
        fcda.ix(intAttr(reader, "ix"));
        skipElement(reader);
        return fcda;
    }

    // ==================== Inputs / ExtRef ====================

    public static SclInput parseInputs(XMLStreamReader reader) throws XMLStreamException {
        SclInput inputs = new SclInput();

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("ExtRef".equals(reader.getLocalName())) {
                    inputs.addExtRef(parseExtRef(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return inputs;
    }

    private static SclExtRef parseExtRef(XMLStreamReader reader) throws XMLStreamException {
        SclExtRef extRef = new SclExtRef();
        extRef.desc(getAttr(reader, "desc"));
        extRef.iedName(getAttr(reader, "iedName"));
        extRef.ldInst(getAttr(reader, "ldInst"));
        extRef.prefix(getAttr(reader, "prefix"));
        extRef.lnClass(getAttr(reader, "lnClass"));
        extRef.lnInst(getAttr(reader, "lnInst"));
        extRef.doName(getAttr(reader, "doName"));
        extRef.daName(getAttr(reader, "daName"));
        extRef.intAddr(getAttr(reader, "intAddr"));
        extRef.serviceType(getAttr(reader, "serviceType"));
        extRef.srcLDInst(getAttr(reader, "srcLDInst"));
        extRef.srcPrefix(getAttr(reader, "srcPrefix"));
        extRef.srcLnClass(getAttr(reader, "srcLNClass"));
        extRef.srcLnInst(getAttr(reader, "srcLNInst"));
        extRef.srcCBName(getAttr(reader, "srcCBName"));
        extRef.pServT(getAttr(reader, "pServT"));
        extRef.pLN(getAttr(reader, "pLN"));
        extRef.pDO(getAttr(reader, "pDO"));
        extRef.pDA(getAttr(reader, "pDA"));
        skipElement(reader);
        return extRef;
    }

    // ==================== DOI / SDI / DAI ====================

    public static SclDOI parseDOI(XMLStreamReader reader) throws XMLStreamException {
        SclDOI doi = new SclDOI();
        doi.name(getAttr(reader, "name"));
        doi.desc(getAttr(reader, "desc"));
        doi.ix(intAttr(reader, "ix"));
        doi.accessControl(getAttr(reader, "accessControl"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SDI" :
                        doi.addSdi(parseSDI(reader));
                        break;
                    case "DAI" :
                        doi.addDai(parseDAI(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return doi;
    }

    private static SclSDI parseSDI(XMLStreamReader reader) throws XMLStreamException {
        SclSDI sdi = new SclSDI();
        sdi.name(getAttr(reader, "name"));
        sdi.desc(getAttr(reader, "desc"));
        sdi.ix(intAttr(reader, "ix"));
        sdi.sAddr(getAttr(reader, "sAddr"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "SDI" :
                        sdi.addSdi(parseSDI(reader));
                        break;
                    case "DAI" :
                        sdi.addDai(parseDAI(reader));
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return sdi;
    }

    private static SclDAI parseDAI(XMLStreamReader reader) throws XMLStreamException {
        SclDAI dai = new SclDAI();
        dai.name(getAttr(reader, "name"));
        dai.sAddr(getAttr(reader, "sAddr"));
        dai.valKind(getAttr(reader, "valKind"));
        dai.ix(intAttr(reader, "ix"));
        dai.valImport(boolAttr(reader, "valImport"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Val".equals(reader.getLocalName())) {
                    dai.addVal(SclReader.parseValChild(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return dai;
    }
}
