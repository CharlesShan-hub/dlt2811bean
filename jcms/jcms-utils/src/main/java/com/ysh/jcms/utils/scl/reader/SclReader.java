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
 * SCL XML 文件解析器。
 * <p>
 * 使用 StAX (XMLStreamReader) 流式解析 SCL/ICD/CID/SSD 文件， 将 XML 转换为纯 SclDocument
 * POJO 模型。
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
            XMLStreamReader reader = createSafeFactory().createXMLStreamReader(inputStream);
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

        // 按内容结构判定文件类型（2007B 只是 IEC 61850 Ed.2 版本号，不能区分文件种类）：
        // 含 <Substation> → SCD；单 IED 且无 Substation → ICD（CID 同构，暂归 ICD）；其余 UNKNOWN
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

    /** 解析过程中的内容统计（用于文件类型判定）。 */
    private static final class ParseStats {
        boolean hasSubstation;
        int iedCount;
    }

    // ======================== 轻量扫描（AccessPoint / LD-LN 目录）
    // ========================

    /** 创建禁用 DTD + 外部实体的安全 XML 工厂（防 XXE）。 */
    private static XMLInputFactory createSafeFactory() {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        factory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        factory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        return factory;
    }

    /**
     * 轻量扫描 SCL 文件中的 IED → AccessPoint 名，不构建完整模型。
     * <p>
     * 只读取 IED / AccessPoint 元素的 name 属性，跳过其余所有内容， 因此即使是几百个 IED、几十 MB 的 SCD
     * 也能秒级完成，不会因完整 模型解析（LNodeType/DO/DA/模板等）而卡住。
     *
     * @param path
     *            SCL 文件路径
     * @return 有序的 IED 名 → AccessPoint 名列表
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
     * 轻量扫描 SCL 文件中的 IED → AP → (LD, LN) 目录，不构建完整模型。
     * <p>
     * 只读取 IED / AccessPoint / Server / LDevice / LN 的 name 属性， 跳过实例数据、控制块、模板等全部细节，
     * 适合为前端候选列表（数据集/控制块下拉的 LD/LN 来源）提供秒级目录。
     *
     * @param path
     *            SCL 文件路径
     * @return 有序的 "IED/AP" → "LD/LN" 完整引用列表
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
            String ldInst = null; // 当前 LD 实例名
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
                            // 落入 LDevice/LN，无需额外处理
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

    // ======================== 共享工具方法 ========================

    /** 获取属性值，不存在返回 null */
    public static String getAttr(XMLStreamReader reader, String name) {
        return reader.getAttributeValue(null, name);
    }

    /** 获取布尔属性，不存在返回 null */
    public static Boolean boolAttr(XMLStreamReader reader, String name) {
        String val = reader.getAttributeValue(null, name);
        if (val == null)
            return null;
        return "true".equals(val) || "1".equals(val);
    }

    /** 获取整型属性，不存在返回 null */
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
            if (event == START_ELEMENT)
                depth++;
            else if (event == END_ELEMENT)
                depth--;
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
