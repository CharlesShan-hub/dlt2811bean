package com.ysh.jcms.utils.scl2.reader;

import com.ysh.jcms.utils.scl2.SclDocument;
import com.ysh.jcms.utils.scl2.SclParseException;
import com.ysh.jcms.utils.scl2.model.SclText;
import com.ysh.jcms.utils.scl2.model.SclVal;
import com.ysh.jcms.utils.scl2.model.communication.*;
import com.ysh.jcms.utils.scl2.model.control.*;
import com.ysh.jcms.utils.scl2.model.header.SclHeader;
import com.ysh.jcms.utils.scl2.model.header.SclHitem;
import com.ysh.jcms.utils.scl2.model.ied.*;
import com.ysh.jcms.utils.scl2.model.input.*;
import com.ysh.jcms.utils.scl2.model.instance.*;
import com.ysh.jcms.utils.scl2.model.substation.*;
import com.ysh.jcms.utils.scl2.model.template.*;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import static javax.xml.stream.XMLStreamConstants.*;

/**
 * SCL XML 文件解析器。
 * <p>
 * 使用 StAX (XMLStreamReader) 流式解析 SCL/ICD/CID/SSD 文件，
 * 将 XML 转换为纯 SclDocument POJO 模型。
 * <p>
 * 职责单一：只做 XML → POJO 映射，不含任何业务逻辑。
 */
public class SclReader {

    // ======================== 公共入口 ========================

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
            XMLInputFactory factory = XMLInputFactory.newInstance();
            factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);

            XMLStreamReader reader = factory.createXMLStreamReader(inputStream);
            SclDocument document = parseDocument(reader);
            reader.close();
            return document;
        } catch (XMLStreamException e) {
            throw new SclParseException("Failed to parse SCL XML", e);
        }
    }

    // ======================== 主解析入口 ========================

    private SclDocument parseDocument(XMLStreamReader reader) throws XMLStreamException, SclParseException {
        SclDocument document = new SclDocument();

        while (reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT) {
                String localName = reader.getLocalName();
                if ("SCL".equals(localName)) {
                    // 检测文件类型
                    String version = getAttr(reader, "version");
                    String revision = getAttr(reader, "revision");
                    if ("2007".equals(version) && "B".equals(revision)) {
                        document.fileType(SclDocument.SclFileType.SCD);
                    }
                    // 解析子元素
                    parseSclChildren(reader, document);
                }
                break;
            }
        }
        return document;
    }

    private void parseSclChildren(XMLStreamReader reader, SclDocument document) throws XMLStreamException, SclParseException {
        while (reader.hasNext()) {
            int event = reader.nextTag();
            if (event == START_ELEMENT) {
                switch (reader.getLocalName()) {
                    case "Header":
                        document.header(SclHeaderParser.parse(reader));
                        break;
                    case "Substation":
                        document.substation(SclSubstationParser.parse(reader));
                        break;
                    case "Communication":
                        document.communication(SclCommunicationParser.parse(reader));
                        break;
                    case "IED":
                        document.addIed(SclIedParser.parse(reader));
                        break;
                    case "DataTypeTemplates":
                        document.dataTypeTemplates(SclTemplateParser.parse(reader));
                        break;
                    default:
                        document.addUnsupportedElement(reader.getLocalName());
                        skipElement(reader);
                        break;
                }
            } else if (event == END_ELEMENT) {
                break;
            }
        }
    }

    // ======================== 共享工具方法 ========================

    /** 获取属性值，不存在返回 null */
    public static String getAttr(XMLStreamReader reader, String name) {
        return reader.getAttributeValue(null, name);
    }

    /** 获取布尔属性，不存在返回 null */
    public static Boolean boolAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        if (val == null) return null;
        return "true".equals(val) || "1".equals(val);
    }

    /** 获取整型属性，不存在返回 null */
    public static Integer intAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        if (val == null) return null;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** 获取整型属性，不存在返回默认值 */
    public static int intAttr(XMLStreamReader reader, String name, int defaultValue) {
        Integer val = intAttr(reader, name);
        return val != null ? val : defaultValue;
    }

    /** 读取元素的文本内容 */
    public static String elementText(XMLStreamReader reader) throws XMLStreamException {
        return reader.getElementText();
    }

    /** 跳过未知元素（递归跳过所有子元素直到遇到 END_ELEMENT） */
    public static void skipElement(XMLStreamReader reader) throws XMLStreamException {
        int depth = 1;
        while (depth > 0 && reader.hasNext()) {
            int event = reader.next();
            if (event == START_ELEMENT) depth++;
            else if (event == END_ELEMENT) depth--;
        }
    }

    /** 读取当前元素的文本子元素（如 <Val>内容</Val>） */
    public static String parseSimpleElementText(XMLStreamReader reader) throws XMLStreamException {
        String text = reader.getElementText();
        return text != null ? text.trim() : null;
    }

    /** 解析可选的文本子元素 */
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

    /** 解析 tVal 子元素 */
    public static SclVal parseValChild(XMLStreamReader reader) throws XMLStreamException {
        SclVal val = new SclVal();
        val.sGroup(intAttr(reader, "sGroup"));
        String content = reader.getElementText();
        val.value(content != null ? content.trim() : null);
        return val;
    }
}
