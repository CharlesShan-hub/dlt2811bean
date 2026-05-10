package com.ysh.dlt2811bean.service.info;

import java.util.HashMap;
import java.util.Map;

public enum ServiceInfo {

    // ==================== 8.2 关联服务 ====================
    ASSOCIATE(
            "associate",
            "8.2.1",
            0x01,
            "建立应用层关联",
            "associate",
            "Associate 服务用于客户与服务器之间进行连接认证。\n"
          + "客户通过 serverAccessPointReference 指定所关联的访问点。\n"
          + "需要安全通信时 authenticationParameter 中应携带数字证书相关信息。\n"
          + "服务器校验成功返回 associationId，校验失败返回 serviceError。",
            "Associate-RequestPDU ::= SEQUENCE {\n"
          + "  serverAccessPointReference  [0] IMPLICIT VisibleString129 OPTIONAL,\n"
          + "  authenticationParameter     [1] IMPLICIT SEQUENCE {\n"
          + "      signatureCertificate      [0] IMPLICIT OCTET STRING,\n"
          + "      signedTime                [1] IMPLICIT UtcTime,\n"
          + "      signedValue               [2] IMPLICIT OCTET STRING\n"
          + "  } OPTIONAL\n"
          + "}\n"
          + "Associate-ResponsePDU ::= SEQUENCE {\n"
          + "  associationId               [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
          + "  serviceError                [1] IMPLICIT ServiceError,\n"
          + "  authenticationParameter     [2] IMPLICIT SEQUENCE {\n"
          + "      signatureCertificate      [0] IMPLICIT OCTET STRING,\n"
          + "      signedTime                [1] IMPLICIT UtcTime,\n"
          + "      signedValue               [2] IMPLICIT OCTET STRING\n"
          + "  } OPTIONAL\n"
          + "}\n"
          + "Associate-ErrorPDU ::= ServiceError"
    ),
    ABORT(
            "abort",
            "8.2.2",
            0x02,
            "中止关联",
            "abort",
            "",
            "Abort-RequestPDU:: = SEQUENCE {\n"
          + "  associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
          + "  reason           [1] IMPLICIT INTEGER {\n"
          + "      other                     (0),\n"
          + "      unrecognized-service      (1),\n"
          + "      invalid-reqID             (2),\n"
          + "      invalid-argument          (3),\n"
          + "      invalid-result            (4),\n"
          + "      max-serv-outstanding-exceed (5)\n"
          + "  } (0..5)\n"
          + "}"
    ),
    RELEASE(
            "release",
            "8.2.3",
            0x03,
            "释放应用层关联",
            "release",
            "",
            "Release-RequestPDU:: = SEQUENCE {\n"
          + "  associationId       [0] IMPLICIT OCTET STRING (SIZE (0..64))\n"
          + "}\n"
          + "Release-ResponsePDU:: = SEQUENCE {\n"
          + "  associationId       [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
          + "  serviceError        [1] IMPLICIT ServiceError\n"
          + "}\n"
          + "Release-ErrorPDU:: = ServiceError"
    ),

    // ==================== 8.3 目录服务 ====================
    GET_SERVER_DIRECTORY(
            "server-dir",
            "8.3.1",
            0x50,
            "读服务器目录",
            "server-dir",
            "",
            "GetServerDirectory-RequestPDU:: = SEQUENCE {\n"
          + "  objectClass        [0] IMPLICIT INTEGER {\n"
          + "      reserved         (0),\n"
          + "      logical-device   (1),\n"
          + "      file-system      (2)\n"
          + "  } (0..2),\n"
          + "  referenceAfter     [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetServerDirectory-ResponsePDU:: = SEQUENCE {\n"
          + "  reference          [0] IMPLICIT SEQUENCE OF ObjectReference,\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetServerDirectory-ErrorPDU:: = ServiceError"
    ),
    GET_LOGIC_DEVICE_DIRECTORY(
            "ld-dir",
            "8.3.2",
            0x51,
            "读逻辑设备目录",
            "ld-dir",
            "",
            "GetLogicalDeviceDirectory-RequestPDU::= SEQUENCE {\n"
          + "  ldName            [0] IMPLICIT ObjectName OPTIONAL,\n"
          + "  referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetLogicalDeviceDirectory-ResponsePDU::= SEQUENCE {\n"
          + "  lnReference       [0] IMPLICIT SEQUENCE OF SubReference,\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetLogicalDeviceDirectory-ErrorPDU::= ServiceError"
    ),
    GET_LOGIC_NODE_DIRECTORY(
            "ln-dir",
            "8.3.3",
            0x52,
            "读逻辑节点目录",
            "ln-dir",
            "",
            "GetLogicalNodeDirectory-RequestPDU::= SEQUENCE {\n"
          + "  reference          [0] IMPLICIT CHOICE {\n"
          + "      ldName           [0] IMPLICIT ObjectName,\n"
          + "      lnReference      [1] IMPLICIT ObjectReference\n"
          + "  },\n"
          + "  acsiClass          [1] IMPLICIT ACSIClass,\n"
          + "  referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetLogicalNodeDirectory-ResponsePDU::= SEQUENCE {\n"
          + "  reference          [0] IMPLICIT SEQUENCE OF SubReference,\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetLogicalNodeDirectory-ErrorPDU::= ServiceError"
    ),
    GET_ALL_DATA_VALUES(
            "get-all-values",
            "8.3.4",
            0x53,
            "读所有数据值",
            "get-all-values",
            "",
            "GetAllDataValues-RequestPDU::= SEQUENCE {\n"
          + "  reference          [0] IMPLICIT CHOICE {\n"
          + "      ldName           [0] IMPLICIT ObjectName,\n"
          + "      lnReference      [1] IMPLICIT ObjectReference\n"
          + "  },\n"
          + "  fc                 [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
          + "  referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetAllDataValues-ResponsePDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT SubReference,\n"
          + "      value            [1] IMPLICIT Data\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetAllDataValues-ErrorPDU::= ServiceError"
    ),
    GET_ALL_DATA_DEFINITION(
            "get-all-def",
            "8.3.5",
            0x9B,
            "读所有数据定义",
            "get-all-def",
            "",
            "GetAllDataDefinition-RequestPDU::= SEQUENCE {\n"
          + "  reference          [0] IMPLICIT CHOICE {\n"
          + "      ldName           [0] IMPLICIT ObjectName,\n"
          + "      lnReference      [1] IMPLICIT ObjectReference\n"
          + "  },\n"
          + "  fc                 [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
          + "  referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetAllDataDefinition-ResponsePDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT SubReference,\n"
          + "      cdcType          [1] IMPLICIT VisibleString OPTIONAL,\n"
          + "      definition       [2] IMPLICIT DataDefinition\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetAllDataDefinition-ErrorPDU::= ServiceError"
    ),
    GET_ALL_CB_VALUES(
            "get-all-cb",
            "8.3.6",
            0x9C,
            "读所有控制块",
            "get-all-cb",
            "",
            "GetAllCBValues-RequestPDU::= SEQUENCE {\n"
          + "  reference          [0] IMPLICIT CHOICE {\n"
          + "      ldName           [0] IMPLICIT ObjectName,\n"
          + "      lnReference      [1] IMPLICIT ObjectReference\n"
          + "  },\n"
          + "  acsiClass          [1] IMPLICIT ACSIClass,\n"
          + "  referenceAfter     [2] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetAllCBValues-ResponsePDU::= SEQUENCE {\n"
          + "  cbValue            [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT SubReference,\n"
          + "      value            [1] IMPLICIT CHOICE {\n"
          + "          brcb           [0] IMPLICIT BRCB,\n"
          + "          urcb           [1] IMPLICIT URCB,\n"
          + "          lcb            [2] IMPLICIT LCB,\n"
          + "          sgb            [3] IMPLICIT SGCB,\n"
          + "          gocb           [4] IMPLICIT GoCB,\n"
          + "          msvcb          [5] IMPLICIT MSVCB\n"
          + "      }\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetAllCBValues-ErrorPDU::= ServiceError"
    ),

    // ==================== 8.4 数据类服务 ====================
    GET_DATA_VALUES(
            "get-data-values",
            "8.4.1",
            0x30,
            "读数据值",
            "get-data-values <ref>",
            "",
            "GetDataValues-RequestPDU::= SEQUENCE {\n"
          + "  data    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference [0] IMPLICIT ObjectReference,\n"
          + "      fc        [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
          + "  }\n"
          + "}\n"
          + "GetDataValues-ResponsePDU::= SEQUENCE {\n"
          + "  value        [0] IMPLICIT SEQUENCE OF Data,\n"
          + "  moreFollows  [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetDataValues-ErrorPDU::= ServiceError"
    ),
    SET_DATA_VALUES     ("set-data-values",   "8.4.2",  0x31, "写数据值",    "set-data-values <ref> <value>"),
    GET_DATA_DIRECTORY(
            "get-data-dir",
            "8.4.3",
            0x32,
            "读数据目录",
            "get-data-dir <ref>",
            "",
            "GetDataDirectory-RequestPDU::= SEQUENCE {\n"
          + "  dataReference     [0] IMPLICIT ObjectReference,\n"
          + "  referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetDataDirectory-ResponsePDU::= SEQUENCE {\n"
          + "  dataAttribute     [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference       [0] IMPLICIT SubReference,\n"
          + "      fc              [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
          + "  },\n"
          + "  moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetDataDirectory-ErrorPDU::= ServiceError"
    ),
    GET_DATA_DEFINITION(
            "get-data-def",
            "8.4.4",
            0x33,
            "读数据定义",
            "get-data-def <ref>",
            "",
            "GetDataDefinition-RequestPDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT ObjectReference,\n"
          + "      fc               [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
          + "  }\n"
          + "}\n"
          + "GetDataDefinition-ResponsePDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      cdcType          [0] IMPLICIT VisibleString OPTIONAL,\n"
          + "      definition       [1] IMPLICIT DataDefinition\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetDataDefinition-ErrorPDU::= ServiceError"
    ),

    // ==================== 8.5 数据集服务 ====================
    GET_DATA_SET_VALUES     ("get-dataset-values",  "8.5.1", 0x3A, "读数据集值",      "get-dataset-values <dsRef>"),
    SET_DATA_SET_VALUES     ("set-dataset-values",  "8.5.2", 0x3B, "写数据集值",      "set-dataset-values <dsRef> <value>"),
    CREATE_DATA_SET(
            "create-dataset",
            "8.5.3",
            0x36,
            "创建数据集",
            "create-dataset <dsRef>",
            "",
            "CreateDataSet-RequestPDU::= SEQUENCE {\n"
          + "  datasetReference  [0] IMPLICIT ObjectReference,\n"
          + "  referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL,\n"
          + "  memberData        [2] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference       [0] IMPLICIT ObjectReference,\n"
          + "      fc              [1] IMPLICIT FunctionalConstraint\n"
          + "  }\n"
          + "}\n"
          + "CreateDataSet-ResponsePDU::= SEQUENCE {}\n"
          + "CreateDataSet-ErrorPDU::= ServiceError"
    ),
    DELETE_DATA_SET(
            "delete-dataset",
            "8.5.4",
            0x37,
            "删除数据集",
            "delete-dataset <dsRef>",
            "",
            "DeleteDataSet-RequestPDU::= SEQUENCE {\n"
          + "  datasetReference  [0] IMPLICIT ObjectReference\n"
          + "}\n"
          + "DeleteDataSet-ResponsePDU::= SEQUENCE {}\n"
          + "DeleteDataSet-ErrorPDU::= ServiceError"
    ),
    GET_DATA_SET_DIRECTORY(
            "get-dataset-dir",
            "8.5.5",
            0x39,
            "读数据集目录",
            "get-dataset-dir <ldName>",
            "",
            "GetDataSetDirectory-RequestPDU::= SEQUENCE {\n"
          + "  datasetReference  [0] IMPLICIT ObjectReference,\n"
          + "  referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetDataSetDirectory-ResponsePDU::= SEQUENCE {\n"
          + "  memberData        [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference       [0] IMPLICIT ObjectReference,\n"
          + "      fc              [1] IMPLICIT FunctionalConstraint\n"
          + "  },\n"
          + "  moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetDataSetDirectory-ErrorPDU::= ServiceError"
    ),

    // ==================== 8.6 定值组服务 ====================
    SELECT_ACTIVE_SG        ("select-active-sg",    "8.6.1", 0x54, "选择激活定值组",        "select-active-sg <sgRef> <sgNum>"),
    SELECT_EDIT_SG          ("select-edit-sg",      "8.6.2", 0x55, "选择编辑定值组",        "select-edit-sg <sgRef>"),
    SET_EDIT_SG_VALUE       ("set-edit-sg-value",   "8.6.3", 0x56, "设置编辑定值值",        "set-edit-sg-value <ref> <value>"),
    CONFIRM_EDIT_SG_VALUES(
            "confirm-edit-sg",
            "8.6.4",
            0x57,
            "确认编辑定值",
            "confirm-edit-sg <sgRef>",
            "",
            "ConfirmEditSGValues-RequestPDU::= SEQUENCE {\n"
          + "  sgcbReference          [0] IMPLICIT ObjectReference\n"
          + "}\n"
          + "ConfirmEditSGValues-ResponsePDU::= NULL\n"
          + "ConfirmEditSGValues-ErrorPDU::= ServiceError"
    ),
    GET_EDIT_SG_VALUE       ("get-edit-sg-value",   "8.6.5", 0x58, "读编辑定值值",          "get-edit-sg-value <ref>"),
    GET_SGCB_VALUES(
            "get-sgcb-values",
            "8.6.6",
            0x59,
            "读定值组控制块",
            "get-sgcb-values <sgcbRef>",
            "",
            "GetSGCBValues-RequestPDU::= SEQUENCE {\n"
          + "  sgcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference\n"
          + "}\n"
          + "GetSGCBValues-ResponsePDU::= SEQUENCE {\n"
          + "  errorSgcb         [0] IMPLICIT SEQUENCE OF CHOICE {\n"
          + "      error            [0] IMPLICIT ServiceError,\n"
          + "      sgcb             [1] IMPLICIT SGCB\n"
          + "  }\n"
          + "}"
    ),

    // ==================== 8.7 报告服务 ====================
    REPORT              ("report",            "8.7.1",  0x5A, "推送报告",                  "report"),
    GET_BRCB_VALUES     ("get-brcb-values",   "8.7.2",  0x5B, "读缓存报告控制块",          "get-brcb-values <brcbRef>"),
    SET_BRCB_VALUES     ("set-brcb-values",   "8.7.3",  0x5C, "写缓存报告控制块",          "set-brcb-values <brcbRef>"),
    GET_URCB_VALUES     ("get-urcb-values",   "8.7.4",  0x5D, "读非缓存报告控制块",        "get-urcb-values <urcbRef>"),
    SET_URCB_VALUES     ("set-urcb-values",   "8.7.5",  0x5E, "写非缓存报告控制块",        "set-urcb-values <urcbRef>"),

    // ==================== 8.8 日志服务 ====================
    GET_LCB_VALUES          ("get-lcb-values",      "8.8.2", 0x5F, "读日志控制块",      "get-lcb-values <lcbRef>"),
    SET_LCB_VALUES          ("set-lcb-values",      "8.8.3", 0x60, "写日志控制块",      "set-lcb-values <lcbRef>"),
    QUERY_LOG_BY_TIME       ("query-log-by-time",   "8.8.4", 0x61, "按时间查询日志",    "query-log-by-time <lcbRef> <start> <stop>"),
    QUERY_LOG_AFTER         ("query-log-after",     "8.8.5", 0x62, "按参考点查询日志",  "query-log-after <lcbRef> <entry>"),
    GET_LOG_STATUS_VALUES   ("get-log-status",      "8.8.6", 0x63, "读日志状态",        "get-log-status <lcbRef>"),

    // ==================== 8.9 GOOSE服务 ====================
    GET_GOCB_VALUES     ("get-gocb-values",     "8.9.4",  0x66, "读GOOSE控制块",    "get-gocb-values <gocbRef>"),
    SET_GOCB_VALUES     ("set-gocb-values",     "8.9.5",  0x67, "写GOOSE控制块",    "set-gocb-values <gocbRef>"),

    // ==================== 8.10 采样值服务 ====================
    GET_MSVCB_VALUES(
            "msvcb-val",
            "8.10.2",
            0x69,
            "读采样值控制块",
            "msvcb-val <msvcbRef>",
            "",
            "GetMSVCBValues-RequestPDU::= SEQUENCE {\n"
          + "  msvcbReference    [0] IMPLICIT SEQUENCE OF ObjectReference\n"
          + "}\n"
          + "GetMSVCBValues-ResponsePDU::= SEQUENCE {\n"
          + "  errorMsvcb       [0] IMPLICIT SEQUENCE OF CHOICE {\n"
          + "      error            [0] IMPLICIT ServiceError,\n"
          + "      msvcb            [1] IMPLICIT MSVCB\n"
          + "  },\n"
          + "  moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetMSVCBValues-ErrorPDU::= ServiceError"
    ),
    SET_MSVCB_VALUES(
            "set-msvcb",
            "8.10.3",
            0x6A,
            "写采样值控制块",
            "set-msvcb <msvcbRef>",
            "",
            "SetMSVCBValues-RequestPDU:: = SEQUENCE {\n"
          + "  msvcb    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference [0] IMPLICIT ObjectReference,\n"
          + "      svEna     [1] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "      msvID     [2] IMPLICIT VisibleString129 OPTIONAL,\n"
          + "      datSet    [3] IMPLICIT ObjectReference OPTIONAL,\n"
          + "      smpMod    [5] IMPLICIT SmpMod OPTIONAL,\n"
          + "      smpRate   [6] IMPLICIT INT16U OPTIONAL,\n"
          + "      optFlds   [7] IMPLICIT MSVOptFlds OPTIONAL\n"
          + "  }\n"
          + "}\n"
          + "SetMSVCBValues-ResponsePDU:: = NULL\n"
          + "SetMSVCBValues-ErrorPDU:: = SEQUENCE {\n"
          + "  result   [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      error     [0] IMPLICIT ServiceError OPTIONAL,\n"
          + "      svEna     [1] IMPLICIT ServiceError OPTIONAL,\n"
          + "      msvID     [2] IMPLICIT ServiceError OPTIONAL,\n"
          + "      datSet    [3] IMPLICIT ServiceError OPTIONAL,\n"
          + "      smpMod    [5] IMPLICIT ServiceError OPTIONAL,\n"
          + "      smpRate   [6] IMPLICIT ServiceError OPTIONAL,\n"
          + "      optFlds   [7] IMPLICIT ServiceError OPTIONAL\n"
          + "  }\n"
          + "}"
    ),

    // ==================== 8.11 控制服务 ====================
    SELECT(
            "select",
            "8.11.1",
            0x44,
            "选择控制对象",
            "select <reference>",
            "",
            "Select-RequestPDU::= SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference\n"
          + "}\n"
          + "Select-ResponsePDU::= SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference\n"
          + "}\n"
          + "Select-ErrorPDU::= SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference\n"
          + "}"
    ),
    SELECT_WITH_VALUE(
            "select-with-value",
            "8.11.2",
            0x45,
            "带值选择控制对象",
            "select-with-value <reference> <value>",
            "",
            "SelectWithValue-RequestPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  operTm       [2] IMPLICIT TimeStamp OPTIONAL,\n"
          + "  origin       [3] IMPLICIT Originator,\n"
          + "  ctlNum       [4] IMPLICIT INT8U,\n"
          + "  t            [5] IMPLICIT TimeStamp,\n"
          + "  test         [6] IMPLICIT BOOLEAN,\n"
          + "  check        [7] IMPLICIT Check\n"
          + "}\n"
          + "SelectWithValue-ErrorPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  operTm       [2] IMPLICIT TimeStamp OPTIONAL,\n"
          + "  origin       [3] IMPLICIT Originator,\n"
          + "  ctlNum       [4] IMPLICIT INT8U,\n"
          + "  t            [5] IMPLICIT TimeStamp,\n"
          + "  test         [6] IMPLICIT BOOLEAN,\n"
          + "  check        [7] IMPLICIT Check,\n"
          + "  addCause     [8] IMPLICIT AddCause\n"
          + "}"
    ),
    OPERATE(
            "operate",
            "8.11.3",
            0x47,
            "操作",
            "operate <reference> [value]",
            "",
            "Operate-RequestPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  origin       [2] IMPLICIT Originator,\n"
          + "  ctlNum       [3] IMPLICIT INT8U,\n"
          + "  t            [4] IMPLICIT TimeStamp,\n"
          + "  test         [5] IMPLICIT BOOLEAN,\n"
          + "  check        [6] IMPLICIT Check\n"
          + "}\n"
          + "Operate-ResponsePDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  origin       [2] IMPLICIT Originator,\n"
          + "  ctlNum       [3] IMPLICIT INT8U,\n"
          + "  t            [4] IMPLICIT TimeStamp,\n"
          + "  test         [5] IMPLICIT BOOLEAN,\n"
          + "  check        [6] IMPLICIT Check\n"
          + "}\n"
          + "Operate-ErrorPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  origin       [2] IMPLICIT Originator,\n"
          + "  ctlNum       [3] IMPLICIT INT8U,\n"
          + "  t            [4] IMPLICIT TimeStamp,\n"
          + "  test         [5] IMPLICIT BOOLEAN,\n"
          + "  check        [6] IMPLICIT Check,\n"
          + "  addCause     [7] IMPLICIT AddCause\n"
          + "}"
    ),
    CANCEL              ("cancel",              "8.11.4", 0x46, "取消操作",                  "cancel <reference>"),
    COMMAND_TERMINATION(
            "cmd-term",
            "8.11.5",
            0x48,
            "命令终止通知",
            "cmd-term",
            "",
            "CommandTermination-RequestPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  operTm       [2] IMPLICIT TimeStamp OPTIONAL,\n"
          + "  origin       [3] IMPLICIT Originator,\n"
          + "  ctlNum       [4] IMPLICIT INT8U,\n"
          + "  t            [5] IMPLICIT TimeStamp,\n"
          + "  test         [6] IMPLICIT BOOLEAN,\n"
          + "  addCause     [7] IMPLICIT AddCause OPTIONAL\n"
          + "}"
    ),
    TIME_ACTIVATED_OPERATE(
            "time-act-operate",
            "8.11.6",
            0x49,
            "定时激活操作",
            "time-act-operate <reference> <value> <time>",
            "",
            "TimeActivatedOperate-RequestPDU:: = SEQUENCE {\n"
          + "  reference        [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal           [1] IMPLICIT Data,\n"
          + "  operTm           [2] IMPLICIT TimeStamp,\n"
          + "  origin           [3] IMPLICIT Originator,\n"
          + "  ctlNum           [4] IMPLICIT INT8U,\n"
          + "  t                [5] IMPLICIT TimeStamp,\n"
          + "  test             [6] IMPLICIT BOOLEAN,\n"
          + "  check            [7] IMPLICIT Check\n"
          + "}\n"
          + "TimeActivatedOperate-ResponsePDU:: = SEQUENCE {\n"
          + "  reference        [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal           [1] IMPLICIT Data,\n"
          + "  operTm           [2] IMPLICIT TimeStamp,\n"
          + "  origin           [3] IMPLICIT Originator,\n"
          + "  ctlNum           [4] IMPLICIT INT8U,\n"
          + "  t                [5] IMPLICIT TimeStamp,\n"
          + "  test             [6] IMPLICIT BOOLEAN,\n"
          + "  check            [7] IMPLICIT Check\n"
          + "}\n"
          + "TimeActivatedOperate-ErrorPDU:: = SEQUENCE {\n"
          + "  reference        [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal           [1] IMPLICIT Data,\n"
          + "  operTm           [2] IMPLICIT TimeStamp,\n"
          + "  origin           [3] IMPLICIT Originator,\n"
          + "  ctlNum           [4] IMPLICIT INT8U,\n"
          + "  t                [5] IMPLICIT TimeStamp,\n"
          + "  test             [6] IMPLICIT BOOLEAN,\n"
          + "  check            [7] IMPLICIT Check,\n"
          + "  addCause         [8] IMPLICIT AddCause\n"
          + "}"
    ),
    TIME_ACTIVATED_OPERATE_TERMINATION(
            "time-act-term",
            "8.11.7",
            0x4A,
            "定时激活终止通知",
            "time-act-term",
            "",
            "TimeActivatedOperateTermination-RequestPDU:: = SEQUENCE {\n"
          + "  reference    [0] IMPLICIT ObjectReference,\n"
          + "  ctlVal       [1] IMPLICIT Data,\n"
          + "  operTm       [2] IMPLICIT TimeStamp,\n"
          + "  origin       [3] IMPLICIT Originator,\n"
          + "  ctlNum       [4] IMPLICIT INT8U,\n"
          + "  t            [5] IMPLICIT TimeStamp,\n"
          + "  test         [6] IMPLICIT BOOLEAN,\n"
          + "  check        [7] IMPLICIT Check,\n"
          + "  addCause     [8] IMPLICIT AddCause OPTIONAL\n"
          + "}"
    ),

    // ==================== 8.12 远程过程调用 ====================
    GET_RPC_INTERFACE_DIRECTORY     ("iface-dir",   "8.12.1", 0x6E, "获取RPC接口目录",      "iface-dir [after]"),
    GET_RPC_METHOD_DIRECTORY        ("method-dir",  "8.12.2", 0x6F, "获取RPC方法目录",      "method-dir [iface] [after]"),
    GET_RPC_INTERFACE_DEFINITION    ("iface-def",   "8.12.3", 0x70, "获取RPC接口定义",      "iface-def [iface] [after]"),
    GET_RPC_METHOD_DEFINITION       ("method-def",  "8.12.4", 0x71, "获取RPC方法定义",      "method-def <refs>"),
    RPC_CALL                        ("rpc",         "8.12.5", 0x72, "远程过程调用",          "rpc <methodRef> [data]"),

    // ==================== 8.13 文件服务 ====================
    GET_FILE(
            "file-get",
            "8.13.1",
            0x80,
            "读文件",
            "file-get <fileName> [startPosition]",
            "",
            "GetFile-RequestPDU:: = SEQUENCE {\n"
          + "  fileName       [0] IMPLICIT VisibleString255,\n"
          + "  startPosition  [1] IMPLICIT INT32U\n"
          + "}\n"
          + "GetFile-ResponsePDU:: = SEQUENCE {\n"
          + "  fileData       [0] IMPLICIT OCTET STRING,\n"
          + "  endOfFile      [1] IMPLICIT BOOLEAN DEFAULT FALSE\n"
          + "}\n"
          + "GetFile-ErrorPDU:: = ServiceError"
    ),
    SET_FILE(
            "file-set",
            "8.13.2",
            0x81,
            "写文件",
            "file-set <fileName> <start> <data> [eof]",
            "",
            "SetFile-RequestPDU:: = SEQUENCE {\n"
          + "  fileName       [0] IMPLICIT VisibleString255,\n"
          + "  startPosition  [1] IMPLICIT INT32U,\n"
          + "  fileData       [2] IMPLICIT OCTET STRING,\n"
          + "  endOfFile      [3] IMPLICIT BOOLEAN DEFAULT FALSE\n"
          + "}\n"
          + "SetFile-ResponsePDU:: = NULL\n"
          + "SetFile-ErrorPDU:: = ServiceError"
    ),
    DELETE_FILE(
            "file-delete",
            "8.13.3",
            0x82,
            "删除文件",
            "file-delete <fileName>",
            "",
            "DeleteFile-RequestPDU:: = SEQUENCE {\n"
          + "  fileName    [0] IMPLICIT VisibleString255\n"
          + "}\n"
          + "DeleteFile-ResponsePDU:: = NULL\n"
          + "DeleteFile-ErrorPDU:: = ServiceError"
    ),
    GET_FILE_ATTRIBUTE_VALUES(
            "file-attr",
            "8.13.4",
            0x83,
            "读文件属性",
            "file-attr <fileName>",
            "",
            "GetFileAttributeValues-RequestPDU:: = SEQUENCE {\n"
          + "  fileName [0] IMPLICIT VisibleString255\n"
          + "}\n"
          + "GetFileAttributeValues-ResponsePDU:: = FileEntry\n"
          + "GetFileAttributeValues-ErrorPDU:: = ServiceError"
    ),
    GET_FILE_DIRECTORY(
            "file-dir",
            "8.13.5",
            0x84,
            "列文件目录",
            "file-dir [path] [after]",
            "",
            "GetFileDirectory-RequestPDU:: = SEQUENCE {\n"
          + "  pathName                     [0] IMPLICIT VisibleString255 OPTIONAL,\n"
          + "  startTime                    [1] IMPLICIT TimeStamp OPTIONAL,\n"
          + "  stopTime                     [2] IMPLICIT TimeStamp OPTIONAL,\n"
          + "  fileAfter                    [3] IMPLICIT VisibleString255 OPTIONAL\n"
          + "}\n"
          + "GetFileDirectory-ResponsePDU:: = SEQUENCE {\n"
          + "  fileEntry                    [0] IMPLICIT SEQUENCE OF FileEntry,\n"
          + "  moreFollows                  [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetFileDirectory-ErrorPDU:: = ServiceError"
    ),

    // ==================== 8.14 测试服务 ====================
    TEST                ("test",          "8.14.1", 0x99, "测试",              "test"),

    // ==================== 8.15 协商服务 ====================
    ASSOCIATE_NEGOTIATE(
            "negotiate",
            "8.15.1",
            0x9A,
            "协商通信参数",
            "negotiate",
            "",
            "AssociateNegotiate-RequestPDU:: = SEQUENCE {\n"
          + "  apduSize               [0] IMPLICIT INT16U,\n"
          + "  asduSize               [1] IMPLICIT INT32U,\n"
          + "  protocolVersion        [2] IMPLICIT INT32U\n"
          + "}\n"
          + "AssociateNegotiate-ResponsePDU:: = SEQUENCE {\n"
          + "  apduSize               [0] IMPLICIT INT16U,\n"
          + "  asduSize               [1] IMPLICIT INT32U,\n"
          + "  protocolVersion        [2] IMPLICIT INT32U,\n"
          + "  modelVersion           [3] IMPLICIT VisibleString\n"
          + "}\n"
          + "AssociateNegotiate-ErrorPDU:: = ServiceError"
    );

    private final String cliName;
    private final String section;
    private final int serviceCode;
    private final String description;
    private final String usage;
    private final String descriptionDetail;
    private final String asn1Definition;

    private static final Map<String, ServiceInfo> BY_CLI_NAME = new HashMap<>();
    private static final Map<String, ServiceInfo> BY_SECTION = new HashMap<>();

    static {
        for (ServiceInfo info : values()) {
            BY_CLI_NAME.put(info.cliName, info);
            BY_SECTION.put(info.section, info);
        }
    }

    ServiceInfo(String cliName, String section, int serviceCode, String description, String usage) {
        this(cliName, section, serviceCode, description, usage, "", "");
    }

    ServiceInfo(String cliName, String section, int serviceCode, String description, String usage, String descriptionDetail, String asn1Definition) {
        this.cliName = cliName;
        this.section = section;
        this.serviceCode = serviceCode;
        this.description = description;
        this.usage = usage;
        this.descriptionDetail = descriptionDetail;
        this.asn1Definition = asn1Definition;
    }

    public String getCliName() { return cliName; }
    public String getSection() { return section; }
    public int getServiceCode() { return serviceCode; }
    public String getDescription() { return description; }
    public String getUsage() { return usage; }
    public String getDescriptionDetail() { return descriptionDetail; }
    public String getAsn1Definition() { return asn1Definition; }

    public static ServiceInfo byCliName(String cliName) {
        return BY_CLI_NAME.get(cliName);
    }

    public static ServiceInfo bySection(String section) {
        return BY_SECTION.get(section);
    }
}
