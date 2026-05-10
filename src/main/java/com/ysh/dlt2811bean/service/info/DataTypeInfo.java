package com.ysh.dlt2811bean.service.info;

import java.util.HashMap;
import java.util.Map;

public enum DataTypeInfo {

    // ==================== 基本数值类型 (§7.1) ====================
    BOOLEAN          ("BOOLEAN",            "7.1.1", "布尔值",        "BOOLEAN"),
    INT8             ("INT8",               "7.1.1", "有符号8位整数",  "INT8"),
    INT16            ("INT16",              "7.1.1", "有符号16位整数", "INT16"),
    INT32            ("INT32",              "7.1.1", "有符号32位整数", "INT32"),
    INT64            ("INT64",              "7.1.1", "有符号64位整数", "INT64"),
    INT8U            ("INT8U",              "7.1.1", "无符号8位整数",  "INT8U"),
    INT16U           ("INT16U",             "7.1.1", "无符号16位整数", "INT16U"),
    INT32U           ("INT32U",             "7.1.1", "无符号32位整数", "INT32U"),
    INT64U           ("INT64U",             "7.1.1", "无符号64位整数", "INT64U"),
    FLOAT32          ("FLOAT32",            "7.1.2", "32位浮点数",    "FLOAT32"),
    FLOAT64          ("FLOAT64",            "7.1.2", "64位浮点数",    "FLOAT64"),

    // ==================== 字符串类型 (§7.1.5) ====================
    VISIBLE_STRING   ("VisibleString",      "7.1.5", "可见字符串",    "VisibleString ::= ISO 646 (ASCII-compatible)"),
    OCTET_STRING     ("OCTET STRING",       "7.1.5", "字节串",        "OCTET STRING"),
    UTF8_STRING      ("UTF8String",         "7.1.5", "UTF-8字符串",   "UTF8String"),
    BIT_STRING       ("BIT STRING",         "7.1.5", "比特串",        "BIT STRING"),

    // ==================== 对象引用类型 (§7.3) ====================
    OBJECT_NAME      ("ObjectName",         "7.3.1", "对象名称",      "ObjectName ::= VisibleString (SIZE(0..64))"),
    OBJECT_REFERENCE ("ObjectReference",    "7.3.2", "对象引用",      "ObjectReference ::= VisibleString (SIZE(0..129))"),
    SUB_REFERENCE    ("SubReference",       "7.3.3", "子引用",        "SubReference ::= VisibleString (SIZE(0..129))"),
    ENTRY_ID         ("EntryID",            "7.3.9", "日志条目标识",  "EntryID ::= OCTET STRING (SIZE(8))"),
    FC               ("FunctionalConstraint", "7.4", "功能约束",      "FunctionalConstraint ::= VisibleString (SIZE(2))"),

    // ==================== 时间类型 (§7.3) ====================
    UTC_TIME         ("UtcTime",            "7.3.5", "UTC时间",       "UtcTime ::= SEQUENCE {\n"
                                                                   + "  secondsSinceEpoch  [0] IMPLICIT INT32U,\n"
                                                                   + "  fractionOfSecond   [1] IMPLICIT INT24U,\n"
                                                                   + "  timeQuality        [2] IMPLICIT TimeQuality\n"
                                                                   + "}"),
    TIME_STAMP       ("TimeStamp",          "7.3.4", "时间戳",        "TimeStamp ::= UtcTime"),
    BINARY_TIME      ("BinaryTime",         "7.3.6", "二进制时间",    "BinaryTime ::= INT32U"),
    ENTRY_TIME       ("EntryTime",          "7.3.9", "日志时间",      "EntryTime ::= UtcTime"),

    // ==================== 复合类型 (§7.3) ====================
    ORIGINATOR       ("Originator",         "7.5.2", "发起方",        "Originator ::= SEQUENCE {\n"
                                                                   + "  orCat    [0] IMPLICIT INTEGER (0..8),\n"
                                                                   + "  orIdent  [1] IMPLICIT OCTET STRING (SIZE(0..64))\n"
                                                                   + "}"),
    CHECK            ("Check",              "7.5.3", "校验标志",      "Check ::= BIT STRING {\n"
                                                                   + "  synchrocheck    (0),\n"
                                                                   + "  interlock-check (1)\n"
                                                                   + "} (SIZE(2))"),
    QUALITY          ("Quality",            "7.5.1", "品质",          "Quality ::= BIT STRING (SIZE(13))"),
    DBPOS            ("Dbpos",              "7.5.4", "继电位置",      "Dbpos ::= CODED ENUM (2 bits)"),
    TCMD             ("Tcmd",               "7.5.5", "命令类型",      "Tcmd ::= CODED ENUM (2 bits)"),

    // ==================== 数据值类型 (§7.7) ====================
    DATA             ("Data",               "7.7.1", "数据值",        "Data ::= CHOICE {\n"
                                                                   + "  error          [0]  IMPLICIT ServiceError,\n"
                                                                   + "  array          [1]  IMPLICIT SEQUENCE OF Data,\n"
                                                                   + "  structure      [2]  IMPLICIT SEQUENCE OF Data,\n"
                                                                   + "  boolean        [3]  IMPLICIT NULL,\n"
                                                                   + "  int8           [4]  IMPLICIT NULL,\n"
                                                                   + "  int16          [5]  IMPLICIT NULL,\n"
                                                                   + "  int32          [6]  IMPLICIT NULL,\n"
                                                                   + "  int64          [7]  IMPLICIT NULL,\n"
                                                                   + "  int8u          [8]  IMPLICIT NULL,\n"
                                                                   + "  int16u         [9]  IMPLICIT NULL,\n"
                                                                   + "  int32u         [10] IMPLICIT NULL,\n"
                                                                   + "  int64u         [11] IMPLICIT NULL,\n"
                                                                   + "  float32        [12] IMPLICIT NULL,\n"
                                                                   + "  float64        [13] IMPLICIT NULL,\n"
                                                                   + "  bit-string     [14] IMPLICIT INTEGER,\n"
                                                                   + "  octet-string   [15] IMPLICIT INTEGER,\n"
                                                                   + "  visible-string [16] IMPLICIT INTEGER,\n"
                                                                   + "  unicode-string [17] IMPLICIT INTEGER,\n"
                                                                   + "  utc-time       [18] IMPLICIT NULL,\n"
                                                                   + "  binary-time    [19] IMPLICIT NULL,\n"
                                                                   + "  quality        [20] IMPLICIT NULL,\n"
                                                                   + "  dbpos          [21] IMPLICIT NULL,\n"
                                                                   + "  tcmd           [22] IMPLICIT NULL,\n"
                                                                   + "  check          [23] IMPLICIT NULL\n"
                                                                   + "}"),
    DATA_DEFINITION  ("DataDefinition",     "7.7.2", "数据定义",      "DataDefinition ::= CHOICE {\n"
                                                                   + "  error          [0]  IMPLICIT ServiceError,\n"
                                                                   + "  array          [1]  IMPLICIT SEQUENCE {\n"
                                                                   + "      numberOfElement [1] IMPLICIT INT32,\n"
                                                                   + "      elementType     [2] DataDefinition\n"
                                                                   + "  },\n"
                                                                   + "  structure      [2]  IMPLICIT SEQUENCE OF SEQUENCE {\n"
                                                                   + "      name            [0] IMPLICIT ObjectName,\n"
                                                                   + "      fc              [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
                                                                   + "      type            [2] DataDefinition\n"
                                                                   + "  },\n"
                                                                   + "  boolean        [3]  IMPLICIT NULL,\n"
                                                                   + "  int8           [4]  IMPLICIT NULL,\n"
                                                                   + "  int16          [5]  IMPLICIT NULL,\n"
                                                                   + "  int32          [6]  IMPLICIT NULL,\n"
                                                                   + "  int64          [7]  IMPLICIT NULL,\n"
                                                                   + "  int8u          [8]  IMPLICIT NULL,\n"
                                                                   + "  int16u         [9]  IMPLICIT NULL,\n"
                                                                   + "  int32u         [10] IMPLICIT NULL,\n"
                                                                   + "  int64u         [11] IMPLICIT NULL,\n"
                                                                   + "  float32        [12] IMPLICIT NULL,\n"
                                                                   + "  float64        [13] IMPLICIT NULL,\n"
                                                                   + "  bit-string     [14] IMPLICIT INTEGER,\n"
                                                                   + "  octet-string   [15] IMPLICIT INTEGER,\n"
                                                                   + "  visible-string [16] IMPLICIT INTEGER,\n"
                                                                   + "  unicode-string [17] IMPLICIT INTEGER,\n"
                                                                   + "  utc-time       [18] IMPLICIT NULL,\n"
                                                                   + "  binary-time    [19] IMPLICIT NULL,\n"
                                                                   + "  quality        [20] IMPLICIT NULL,\n"
                                                                   + "  dbpos          [21] IMPLICIT NULL,\n"
                                                                   + "  tcmd           [22] IMPLICIT NULL,\n"
                                                                   + "  check          [23] IMPLICIT NULL\n"
                                                                   + "}"),

    // ==================== 控制块类型 (§8) ====================
    BRCB             ("BRCB",               "7.6.1", "缓存报告控制块", "BUFFERED-REPORT-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  brcbName         ObjectName,\n"
                                                                   + "  brcbRef          ObjectReference,\n"
                                                                   + "  rptID            VISIBLE STRING (SIZE (0..129)),\n"
                                                                   + "  rptEna           BOOLEAN,\n"
                                                                   + "  datSet           ObjectReference,\n"
                                                                   + "  confRev          INT32U,\n"
                                                                   + "  optFlds          PACKED LIST { ... },\n"
                                                                   + "  bufTm            INT32U,\n"
                                                                   + "  sqNum            INT16U,\n"
                                                                   + "  trgOps           TriggerConditions,\n"
                                                                   + "  intgPd           INT32U,\n"
                                                                   + "  gi               BOOLEAN,\n"
                                                                   + "  purgeBuf         BOOLEAN,\n"
                                                                   + "  entryID          EntryID,\n"
                                                                   + "  timeOfEntry      EntryTime,\n"
                                                                   + "  resvTms          INT16 OPTIONAL,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),
    URCB             ("URCB",               "7.6.2", "非缓存报告控制块", "UNBUFFERED-REPORT-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  urcbName         ObjectName,\n"
                                                                   + "  urcbRef          ObjectReference,\n"
                                                                   + "  rptID            VISIBLE STRING (SIZE (0..129)),\n"
                                                                   + "  rptEna           BOOLEAN,\n"
                                                                   + "  datSet           ObjectReference,\n"
                                                                   + "  confRev          INT32U,\n"
                                                                   + "  optFlds          PACKED LIST { ... },\n"
                                                                   + "  trgOps           TriggerConditions,\n"
                                                                   + "  intgPd           INT32U,\n"
                                                                   + "  gi               BOOLEAN,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),
    LCB              ("LCB",                "7.6.3", "日志控制块",     "LOG-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  lcbName          ObjectName,\n"
                                                                   + "  lcbRef           ObjectReference,\n"
                                                                   + "  logEna           BOOLEAN,\n"
                                                                   + "  logRef           ObjectReference OPTIONAL,\n"
                                                                   + "  datSet           ObjectReference,\n"
                                                                   + "  confRev          INT32U,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),
    SGCB             ("SGCB",               "7.6.4", "定值组控制块",   "SETTING-GROUP-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  sgcbName         ObjectName,\n"
                                                                   + "  sgcbRef          ObjectReference,\n"
                                                                   + "  numOfSG          INT8U,\n"
                                                                   + "  actSG            INT8U,\n"
                                                                   + "  editSG           INT8U OPTIONAL,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),
    GOCB             ("GoCB",               "7.6.5", "GOOSE控制块",    "GOOSE-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  goCBName         ObjectName,\n"
                                                                   + "  goCBRef          ObjectReference,\n"
                                                                   + "  goEna            BOOLEAN,\n"
                                                                   + "  datSet           ObjectReference,\n"
                                                                   + "  confRev          INT32U,\n"
                                                                   + "  ndsCom           BOOLEAN,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),
    MSVCB            ("MSVCB",              "7.6.6", "采样值控制块",   "MULTICAST-SAMPLED-VALUE-CONTROL-BLOCK ::= SEQUENCE {\n"
                                                                   + "  msvCBName        ObjectName,\n"
                                                                   + "  msvCBRef         ObjectReference,\n"
                                                                   + "  svEna            BOOLEAN DEFAULT FALSE,\n"
                                                                   + "  msvID            VISIBLE STRING (SIZE (0..129)) OPTIONAL,\n"
                                                                   + "  datSet           ObjectReference OPTIONAL,\n"
                                                                   + "  smpMod           SmpMod OPTIONAL,\n"
                                                                   + "  smpRate          INT16U OPTIONAL,\n"
                                                                   + "  optFlds          MSVOptFlds OPTIONAL,\n"
                                                                   + "  owner            OCTET STRING (SIZE (64))\n"
                                                                   + "}"),

    // ==================== 文件类型 ====================
    FILE_ENTRY       ("FileEntry",          "7.3.10", "文件条目",     ""),

    // ==================== 错误/枚举类型 ====================
    SERVICE_ERROR    ("ServiceError",       "7.5.1", "服务错误码",    ""),
    ADD_CAUSE        ("AddCause",           "7.5.4", "附加原因",      ""),
    SMP_MOD          ("SmpMod",             "7.6.6", "采样模式",      ""),

    // ==================== 编码类型 ====================
    TIME_QUALITY     ("TimeQuality",        "7.3.5", "时间品质",      ""),
    TRIGGER_CONDITIONS("TriggerConditions", "7.6.1", "触发条件",      ""),
    PHY_COM_ADDR     ("PhyComAddr",         "7.1.5", "物理通信地址",  "");

    private final String typeName;
    private final String section;
    private final String description;
    private final String asn1Definition;

    private static final Map<String, DataTypeInfo> BY_TYPE_NAME = new HashMap<>();

    static {
        for (DataTypeInfo info : values()) {
            BY_TYPE_NAME.put(info.typeName, info);
        }
    }

    DataTypeInfo(String typeName, String section, String description, String asn1Definition) {
        this.typeName = typeName;
        this.section = section;
        this.description = description;
        this.asn1Definition = asn1Definition;
    }

    public String getTypeName() { return typeName; }
    public String getSection() { return section; }
    public String getDescription() { return description; }
    public String getAsn1Definition() { return asn1Definition; }

    public static DataTypeInfo byTypeName(String typeName) {
        return BY_TYPE_NAME.get(typeName);
    }
}
