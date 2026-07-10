package com.ysh.jcms.info;

import java.util.HashMap;
import java.util.Map;

/**
 * DL/T 2811 data type definitions derived from the ASN.1 module. Each entry
 * maps to a type defined in cms.asn1 with its PER encoding semantics.
 */
public enum CmsDataTypeInfo {

    // ==================== Primitive numeric types (§7.1) ====================
    BOOLEAN("BOOLEAN", "7.1.1", "Boolean", "布尔值", "INTEGER (0..1) — 1 bit constrained"), INT8("INT8", "7.1.2", "Signed 8-bit int",
            "有符号8位整数", "INTEGER (-128..127) — 8 bits aligned"), INT16("INT16", "7.1.2", "Signed 16-bit int", "有符号16位整数",
                    "INTEGER (-32768..32767) — 16 bits aligned"), INT32("INT32", "7.1.2", "Signed 32-bit int", "有符号32位整数",
                            "INTEGER (-2147483648..2147483647) — 32 bits aligned"), INT64("INT64", "7.1.2", "Signed 64-bit int", "有符号64位整数",
                                    "INTEGER — length + content bytes"), INT8U("INT8U", "7.1.2", "Unsigned 8-bit int", "无符号8位整数",
                                            "INTEGER (0..255) — 8 bits aligned"), INT16U("INT16U", "7.1.2", "Unsigned 16-bit int",
                                                    "无符号16位整数", "INTEGER (0..65535) — 16 bits aligned"), INT32U("INT32U", "7.1.2",
                                                            "Unsigned 32-bit int", "无符号32位整数",
                                                            "INTEGER (0..4294967295) — 32 bits aligned"), INT64U("INT64U", "7.1.2",
                                                                    "Unsigned 64-bit int", "无符号64位整数",
                                                                    "INTEGER — length + content bytes"), FLOAT32("FLOAT32", "7.1.4",
                                                                            "32-bit float", "32位浮点数",
                                                                            "OCTET STRING (SIZE(4))"), FLOAT64("FLOAT64", "7.1.4",
                                                                                    "64-bit float", "64位浮点数", "OCTET STRING (SIZE(8))"),

    // ==================== String types (§7.1.5) ====================
    VISIBLE_STRING("VisibleString", "7.1.5", "Visible string", "可见字符串", "VisibleString — length + 8-bit chars"), OCTET_STRING(
            "OCTET STRING", "7.1.5", "Octet string", "字节串", "OCTET STRING — length + raw bytes"), UTF8_STRING("UTF8String", "7.1.5",
                    "UTF-8 string", "UTF-8字符串", "UTF8String — length + UTF-8 bytes"), BIT_STRING("BIT STRING", "7.1.5", "Bit string", "比特串",
                            "BIT STRING — length + bits"),

    // ==================== Object reference types (§7.3) ====================
    OBJECT_NAME("ObjectName", "7.3.1", "Object name", "对象名称", "VisibleString (SIZE(0..64))"), OBJECT_REFERENCE("ObjectReference", "7.3.2",
            "Object reference", "对象引用", "VisibleString (SIZE(0..129))"), SUB_REFERENCE("SubReference", "7.3.3", "Sub reference", "子引用",
                    "VisibleString (SIZE(0..129))"), ENTRY_ID("EntryID", "7.3.8", "Entry ID", "日志条目标识", "OCTET STRING (SIZE(8))"), FC(
                            "FunctionalConstraint", "7.4", "Functional constraint", "功能约束", "VisibleString (SIZE(2))"),

    // ==================== Time types (§7.2) ====================
    UTC_TIME("UtcTime", "7.2.1", "UTC time", "UTC时间", "OCTET STRING (SIZE(8))"), TIME_STAMP("TimeStamp", "7.3.4", "Time stamp", "时间戳",
            "TimeStamp ::= UtcTime"), BINARY_TIME("BinaryTime", "7.2.2", "Binary time", "二进制时间", "OCTET STRING (SIZE(6))"), ENTRY_TIME(
                    "EntryTime", "7.3.9", "Entry time", "日志时间",
                    "BinaryTime"), TIME_QUALITY("TimeQuality", "7.2.1", "Time quality", "时间品质", "BIT STRING (SIZE(8))"),

    // ==================== Composite types (§7.3) ====================
    ORIGINATOR("Originator", "7.5.2", "Originator", "发起方", "SEQUENCE { orCat INTEGER(0..8), orIdent OCTET STRING(0..64) }"), CHECK("Check",
            "7.5.3", "Check", "校验标志", "BIT STRING { syncheck, interlock } (SIZE(2))"), QUALITY("Quality", "7.3.6", "Quality", "品质",
                    "BIT STRING (SIZE(13))"), DBPOS("Dbpos", "7.3.5", "Dbpos", "继电位置", "BIT STRING (SIZE(2))"), TCMD("Tcmd", "7.3.7",
                            "Tcmd", "命令类型", "BIT STRING (SIZE(2))"), PHY_COM_ADDR("PhyComAddr", "7.3.12", "Physical com addr", "物理通信地址",
                                    "SEQUENCE { addr OCTET STRING(6), priority Int8U, vid Int16U, appid Int16U }"),

    // ==================== Data value types (§7.7) ====================
    DATA("Data", "7.7", "Data", "数据值", "CHOICE of 24 alternatives"), DATA_DEFINITION("DataDefinition", "7.8", "Data definition", "数据定义",
            "CHOICE of 24 alternatives"),

    // ==================== Block types (§7.6) ====================
    BRCB("BRCB", "7.6.1", "Buffered Report CB", "缓存报告控制块", "SEQUENCE { ... }"), URCB("URCB", "7.6.1", "Unbuffered Report CB", "非缓存报告控制块",
            "SEQUENCE { ... }"), LCB("LCB", "7.6.1", "Log Control Block", "日志控制块", "SEQUENCE { ... }"), SGCB("SGCB", "7.6.1",
                    "Setting Group CB", "定值组控制块", "SEQUENCE { ... }"), GOCB("GoCB", "7.6.1", "GOOSE Control Block", "GOOSE控制块",
                            "SEQUENCE { ... }"), MSVCB("MSVCB", "7.6.1", "Multicast SV CB", "采样值控制块", "SEQUENCE { ... }"), FILE_ENTRY(
                                    "FileEntry", "7.3.10", "File Entry", "文件条目", "SEQUENCE { fileName, fileSize, lastModified, checkSum }"),

    // ==================== Error / enum types ====================
    SERVICE_ERROR("ServiceError", "7.3.11", "Service error", "服务错误码", "INTEGER (0..12)"), ADD_CAUSE("AddCause", "7.5.4", "Additional cause",
            "附加原因", "INTEGER (0..27)"), SMP_MOD("SmpMod", "7.6.7", "Sample mode", "采样模式", "INTEGER (0..2)"), TRIGGER_CONDITIONS(
                    "TriggerConditions", "7.6.2", "Trigger conditions", "触发条件",
                    "BIT STRING (SIZE(6))"), REASON_CODE("ReasonCode", "7.6.3", "Reason code", "原因码", "BIT STRING (SIZE(7))"), RCB_OPT_FLDS(
                            "RcbOptFlds", "7.6.4", "RCB optional fields", "报告可选域", "BIT STRING (SIZE(10))"), LCB_OPT_FLDS("LcbOptFlds",
                                    "7.6.5", "LCB optional fields", "日志可选域", "BIT STRING (SIZE(1))"), MSVCB_OPT_FLDS("MsvcbOptFlds",
                                            "7.6.6", "MSVCB optional fields", "采样值可选域", "BIT STRING (SIZE(5))"), ENUMERATED("ENUMERATED",
                                                    "7.1.6", "Enumerated", "枚举", "Int8 (0..127)"), CODED_ENUM("CODEDENUM", "7.1.7",
                                                            "Coded enum", "编码枚举", "BIT STRING (SIZE(0..n))"), PACKED_LIST("PackedList",
                                                                    "7.1.8", "Packed list", "打包列表", "BIT STRING (SIZE(0..n))");

    private static final Map<String, CmsDataTypeInfo> BY_TYPE_NAME = new HashMap<>();

    static {
        for (CmsDataTypeInfo info : values()) {
            BY_TYPE_NAME.put(info.typeName, info);
        }
    }

    private final String typeName;
    private final String section;
    private final String enDescription;
    private final String cnDescription;
    private final String asn1Summary;

    CmsDataTypeInfo(String typeName, String section, String enDescription, String cnDescription, String asn1Summary) {
        this.typeName = typeName;
        this.section = section;
        this.enDescription = enDescription;
        this.cnDescription = cnDescription;
        this.asn1Summary = asn1Summary;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getSection() {
        return section;
    }
    public String getEnDescription() {
        return enDescription;
    }
    public String getCnDescription() {
        return cnDescription;
    }
    public String getAsn1Summary() {
        return asn1Summary;
    }

    public static CmsDataTypeInfo byTypeName(String typeName) {
        return BY_TYPE_NAME.get(typeName);
    }
}
