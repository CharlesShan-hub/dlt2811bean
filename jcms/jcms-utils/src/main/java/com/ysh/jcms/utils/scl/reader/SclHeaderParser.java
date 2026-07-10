package com.ysh.jcms.utils.scl.reader;

import com.ysh.jcms.utils.scl.SclParseException;
import com.ysh.jcms.utils.scl.model.header.SclHeader;
import com.ysh.jcms.utils.scl.model.header.SclHitem;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static com.ysh.jcms.utils.scl.reader.SclReader.*;

public class SclHeaderParser {

    public static SclHeader parse(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclHeader header = new SclHeader();
        header.id(getAttr(reader, "id"));
        header.version(getAttr(reader, "version"));
        header.revision(getAttr(reader, "revision"));
        header.toolId(getAttr(reader, "toolID"));
        header.nameStructure(getAttr(reader, "nameStructure"));

        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Text" :
                        header.text(parseTextChild(reader));
                        break;
                    case "History" :
                        parseHistory(reader, header);
                        break;
                    default :
                        skipElement(reader);
                        break;
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
        return header;
    }

    private static void parseHistory(XMLStreamReader reader, SclHeader header) throws XMLStreamException, SclParseException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == XMLStreamConstants.START_ELEMENT) {
                if ("Hitem".equals(reader.getLocalName())) {
                    header.addHitem(parseHitem(reader));
                } else {
                    skipElement(reader);
                }
            } else if (event == XMLStreamConstants.END_ELEMENT) {
                break;
            }
        }
    }

    private static SclHitem parseHitem(XMLStreamReader reader) throws XMLStreamException {
        SclHitem hitem = new SclHitem();
        hitem.version(getAttr(reader, "version"));
        hitem.revision(getAttr(reader, "revision"));
        hitem.when(getAttr(reader, "when"));
        hitem.who(getAttr(reader, "who"));
        hitem.what(getAttr(reader, "what"));
        hitem.why(getAttr(reader, "why"));
        // skip text content (tHItem is mixed content, we ignore non-attribute data)
        skipElement(reader);
        return hitem;
    }
}
