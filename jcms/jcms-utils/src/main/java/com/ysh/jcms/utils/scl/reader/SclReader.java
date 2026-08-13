package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclDocument;
import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.SclText;
import com.ysh.jcms.utils.scl.model.SclVal;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static javax.xml.stream.XMLStreamConstants.*;

/**
 * SCL XML file parser.
 * <p>
 * Uses StAX (XMLStreamReader) to parse SCL/ICD/CID/SSD files in a streaming manner, converting the XML into a plain
 * SclDocument POJO model.
 * <p>
 * Single responsibility: only performs XML → POJO mapping, containing no business logic.
 */
public class SclReader {

    // ======================== Public entry ========================

    public SclDocument read(String filePath) throws SclParseException {
        return read(Paths.get(filePath));
    }

    public SclDocument read(Path path) throws SclParseException {
        try (InputStream is = new FileInputStream(path.toFile())) {
            SclDocument doc = read(is);
            doc.originalFilePath(path.toString());
            return doc;
        } catch (IOException e) {
            throw new SclParseException("Failed to read SCL file: " + path, e);
        }
    }

    public SclDocument read(InputStream inputStream) throws SclParseException {
        try {
            XMLStreamReader reader = createSafeFactory().createXMLStreamReader(inputStream);
            SclDocument document = parseDocument(reader);
            reader.close();
            return document;
        } catch (XMLStreamException e) {
            throw new SclParseException("Failed to parse SCL XML", e);
        }
    }

    // ======================== Main parsing entry ========================

    private SclDocument parseDocument(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclDocument document = new SclDocument();
        ParseStats stats = new ParseStats();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                String localName = reader.getLocalName();
                if ("SCL".equals(localName)) {
                    parseSclChildren(reader, document, stats);
                }
                break;
            }
        }

        // Determine the file type by content structure (2007B is only the IEC 61850 Ed.2 version number and cannot
        // distinguish file kinds): contains <Substation> → SCD; single IED without Substation → ICD (CID is
        // structurally identical, temporarily classified as ICD); otherwise UNKNOWN
        if (stats.hasSubstation) {
            document.fileType(SclDocument.SclFileType.SCD);
        } else if (stats.iedCount == 1) {
            document.fileType(SclDocument.SclFileType.ICD);
        }
        return document;
    }

    private void parseSclChildren(XMLStreamReader reader, SclDocument document, ParseStats stats)
            throws XMLStreamException, SclParseException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Header" :
                        document.header(SclHeaderParser.parse(reader));
                        break;
                    case "Substation" :
                        stats.hasSubstation = true;
                        document.substation(SclSubstationParser.parse(reader));
                        break;
                    case "Communication" :
                        document.communication(SclCommunicationParser.parse(reader));
                        break;
                    case "IED" :
                        stats.iedCount++;
                        document.addIed(SclIedParser.parse(reader));
                        break;
                    case "DataTypeTemplates" :
                        document.dataTypeTemplates(SclTemplateParser.parse(reader));
                        break;
                    default :
                        document.addUnsupportedElement(reader.getLocalName());
                        skipElement(reader);
                        break;
                }
            } else if (event == END_ELEMENT) {
                break;
            }
        }
    }

    /** Content statistics during parsing (used for file type determination). */
    private static final class ParseStats {
        boolean hasSubstation;
        int iedCount;
    }

    // ======================== Lightweight scan (AccessPoint / LD-LN directory)
    // ========================

    /** Creates a secure XML factory with DTD and external entities disabled (XXE protection). */
    private static XMLInputFactory createSafeFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory;
    }

    /**
     * Lightweight scan of the IED → AccessPoint names in an SCL file without building the full model.
     * <p>
     * Only reads the name attributes of IED / AccessPoint elements and skips everything else, so even an SCD with
     * hundreds of IEDs and tens of MB completes in seconds, without getting stuck on full model parsing
     * (LNodeType/DO/DA/templates, etc.).
     *
     * @param path
     *            SCL file path
     * @return ordered list of IED name → AccessPoint names
     */
    public static Map<String, List<String>> scanAccessPoints(Path path) throws SclParseException {
        try (InputStream is = new FileInputStream(path.toFile())) {
            return scanAccessPoints(is);
        } catch (IOException e) {
            throw new SclParseException("Failed to read SCL file: " + path, e);
        }
    }

    public static Map<String, List<String>> scanAccessPoints(InputStream inputStream) throws SclParseException {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            XMLStreamReader reader = createSafeFactory().createXMLStreamReader(inputStream);
            String currentIed = null;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == START_ELEMENT) {
                    String localName = reader.getLocalName();
                    if ("IED".equals(localName)) {
                        currentIed = reader.getAttributeValue(null, "name");
                        if (currentIed != null) {
                            result.put(currentIed, new ArrayList<>());
                        }
                    } else if ("AccessPoint".equals(localName) && currentIed != null) {
                        String ap = reader.getAttributeValue(null, "name");
                        if (ap != null) {
                            result.get(currentIed).add(ap);
                        }
                    }
                } else if (event == END_ELEMENT && "IED".equals(reader.getLocalName())) {
                    currentIed = null;
                }
            }
            reader.close();
            return result;
        } catch (XMLStreamException e) {
            throw new SclParseException("Failed to scan SCL XML for access points", e);
        }
    }

    /**
     * Lightweight scan of the IED → AP → (LD, LN) directory in an SCL file without building the full model.
     * <p>
     * Only reads the name attributes of IED / AccessPoint / Server / LDevice / LN, skipping all details such as
     * instance data, control blocks, and templates; suitable for providing a second-level directory for frontend
     * candidate lists (LD/LN source for data set / control block dropdowns).
     *
     * @param path
     *            SCL file path
     * @return ordered list of "IED/AP" → full "LD/LN" references
     */
    public static Map<String, List<String>> scanLdLns(Path path) throws SclParseException {
        try (InputStream is = new FileInputStream(path.toFile())) {
            return scanLdLns(is);
        } catch (IOException e) {
            throw new SclParseException("Failed to read SCL file: " + path, e);
        }
    }

    public static Map<String, List<String>> scanLdLns(InputStream inputStream) throws SclParseException {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            XMLStreamReader reader = createSafeFactory().createXMLStreamReader(inputStream);
            String apRef = null; // "IED/AP"
            String ldInst = null; // current LD instance name
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == START_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "IED" :
                            apRef = reader.getAttributeValue(null, "name");
                            break;
                        case "AccessPoint" :
                            if (apRef != null) {
                                String ap = reader.getAttributeValue(null, "name");
                                apRef = ap != null ? apRef + "/" + ap : apRef;
                                result.put(apRef, new ArrayList<>());
                            }
                            break;
                        case "Server" :
                            // falls through to LDevice/LN, no extra handling needed
                            break;
                        case "LDevice" :
                            ldInst = reader.getAttributeValue(null, "inst");
                            break;
                        case "LN" :
                        case "LN0" :
                            if (apRef != null && ldInst != null) {
                                String ln = reader.getAttributeValue(null, "lnClass");
                                String inst = reader.getAttributeValue(null, "inst");
                                String prefix = reader.getAttributeValue(null, "prefix");
                                if (ln != null) {
                                    result.get(apRef).add(ldInst + "/" + (prefix != null ? prefix : "") + ln + (inst != null ? inst : ""));
                                }
                            }
                            break;
                        default :
                            break;
                    }
                } else if (event == END_ELEMENT) {
                    switch (reader.getLocalName()) {
                        case "IED" :
                            apRef = null;
                            ldInst = null;
                            break;
                        case "LDevice" :
                            ldInst = null;
                            break;
                        default :
                            break;
                    }
                }
            }
            reader.close();
            return result;
        } catch (XMLStreamException e) {
            throw new SclParseException("Failed to scan SCL XML for LD/LN directory", e);
        }
    }

    // ======================== Shared utility methods ========================

    /** Gets an attribute value, returns null if absent */
    public static String getAttr(XMLStreamReader reader, String name) {
        return reader.getAttributeValue(null, name);
    }

    /** Gets a boolean attribute, returns null if absent */
    public static Boolean boolAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        if (val == null)
            return null;
        return "true".equals(val) || "1".equals(val);
    }

    /** Gets an integer attribute, returns null if absent */
    public static Integer intAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        if (val == null)
            return null;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Gets an integer attribute, returns the default value if absent */
    public static int intAttr(XMLStreamReader reader, String name, int defaultValue) {
        Integer val = intAttr(reader, name);
        return val != null ? val : defaultValue;
    }

    /** Reads the text content of an element */
    public static String elementText(XMLStreamReader reader) throws XMLStreamException {
        return reader.getElementText();
    }

    /** Skips an unknown element (recursively skips all child elements until END_ELEMENT is reached) */
    public static void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT)
                depth++;
            else if (event == END_ELEMENT)
                depth--;
        }
    }

    /** Reads the text child of the current element (e.g. {@code <Val>content</Val>}) */
    public static String parseSimpleElementText(XMLStreamReader reader) throws XMLStreamException {
        String text = reader.getElementText();
        return text != null ? text.trim() : null;
    }

    /** Parses an optional text child element */
    public static SclText parseTextChild(XMLStreamReader reader) throws XMLStreamException {
        SclText text = new SclText();
        text.source(getAttr(reader, "source"));
        String content = reader.getElementText();
        if (content != null) {
            content = content.trim();
            if (!content.isEmpty()) {
                text.value(content);
            }
        }
        return text;
    }

    /** Parses a tVal child element */
    public static SclVal parseValChild(XMLStreamReader reader) throws XMLStreamException {
        SclVal val = new SclVal();
        val.sGroup(intAttr(reader, "sGroup"));
        String content = reader.getElementText();
        val.value(content != null ? content.trim() : null);
        return val;
    }
}
