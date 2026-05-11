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
            "a) 客户通过 serverAccessPointReference 指定所关联的访问点;\n"
          + "b) 需要安全通信时 authenticationParameter 中应携带数字证书相关信息;\n"
          + "c) 服务器校验成功返回 associationId,校验失败返回 serviceError",
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
            "a) referenceAfter 是不正确的引用名时,应返回 Response-;\n"
          + "b) referenceAfter 正确但返回的 reference 数量为 0 时,应返回 Response+;\n"
          + "c) objectClass 应始终为 logical-device",
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
            "a) 请求时指定了 ldName 的情况下,响应的 lnReference 应为逻辑节点的名称。未指定 ldName 的情况下,应读取所有逻辑设备的逻辑节点,响应的 lnReference 应为逻辑节点的引用;\n"
          + "b) referenceAfter 用于连续多次请求时,宜设为上一次响应的最后一个 lnReference;\n"
          + "c) referenceAfter 用于单次请求时,应直接从指定的 referenceAfter 之后返回结果;\n"
          + "d) lnReference 是 SubReference 类型,应补齐 reference 的内容",
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
            "acsiClass 为 DataObject 时,请求逻辑节点下所有数据对象及其子数据对象的引用名,引用名应按模型定义的顺序排序。如 LD/LN.DO1,LD/LN.DO1.SDO1,LD/LN.DO1.SDO2",
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
            "a) 数据不包含指定 fc 的内容时,返回的结果中应不包含该数据;\n"
          + "b) 参数 fc 为空时,应返回指定逻辑设备或逻辑节点内全部数据属性的值(不包括功能约束 SE)。仅当参数 fc 明确指定为 SE 时,服务器才返回功能约束 SE 的数据属性值。仅当选择编辑定值组服务后,功能约束 SE 的数据属性值有效",
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
            "a) 数据不包含指定 fc 的内容时,返回的结果中应不包含该数据;\n"
          + "b) 参数 fc 为空时,应返回指定逻辑设备或逻辑节点内全部数据属性的定义(不包括功能约束 SE);\n"
          + "c) 仅当参数 fc 明确指定为 SE 时,服务器返回功能约束 SE 的数据属性定义。功能约束 SE 的数据属性定义应与功能约束 SG 完全相同",
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
            "a) 一帧报文无法返回所有数据的值时,服务器应按顺序返回其中的部分结果,返回的每一个 value 应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数队列,再次发起新的读数据值请求;\n"
          + "b) 请求队列中的某一个数据无法访问时,应返回错误原因,并继续处理下一个数据;\n"
          + "c) 数据不包含指定 fc 的内容时,应返回错误原因",
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
    SET_DATA_VALUES(
            "set-data-values",
            "8.4.2",
            0x31,
            "写数据值",
            "set-data-values <ref> <value>",
            "a) 每一个数据由 Reference 唯一索引,当包含 fc(功能约束)时,表示数据值为 FCD 的值;不包含 fc(功能约束)时,表示数据值为所有数据属性的值;\n"
          + "b) 所有数据值设置成功时返回 Response+,部分或全部失败时返回 Response-。在 Response-中,依次返回每个数据值的设置结果",
            "SetDataValues-RequestPDU::= SEQUENCE {\n"
          + "  data    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference [0] IMPLICIT ObjectReference,\n"
          + "      value     [1] IMPLICIT Data\n"
          + "  }\n"
          + "}\n"
          + "SetDataValues-ResponsePDU::= SEQUENCE {}\n"
          + "SetDataValues-ErrorPDU::= SEQUENCE {\n"
          + "  result  [0] IMPLICIT SEQUENCE OF ServiceError\n"
          + "}"
    ),
    GET_DATA_DIRECTORY(
            "get-data-dir",
            "8.4.3",
            0x32,
            "读数据目录",
            "get-data-dir <ref>",
            "a) 子数据对象应不包含 fc,数据属性应包含 fc;\n"
          + "b) 嵌套结构的数据属性,应按深度优先的顺序逐层返回数据属性引用;\n"
          + "c) SCL 定义的 DA 对象含有 fc 定义,因此结果中应包含 fc;DO 对象和 BDA 对象不含有 fc 定义,因此结果中应不包含 fc",
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
            "a) data 是数据对象的情况下,响应时应设置 cdcType 为对应的 CDC 类型。data 是数据属性的情况下,响应时应设置 cdcType 为空;\n"
          + "b) 一帧报文无法返回所有数据的定义时,服务器应按顺序返回其中的部分结果,返回的每一组定义应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数队列,再次发起新的读数据定义请求;\n"
          + "c) 请求队列中的某一个数据无法访问时,应返回错误原因,并继续处理下一个数据;\n"
          + "d) 数据不包含指定 fc 的内容时,应返回错误原因",
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
    GET_DATA_SET_VALUES(
            "get-dataset-values",
            "8.5.1",
            0x3A,
            "读数据集值",
            "get-dataset-values <dsRef>",
            "a) 未指定 referenceAfter 时,应从数据集的第一个成员开始按顺序返回数据值。指定了 referenceAfter 时,应从 referenceAfter 成员之后按顺序返回数据集中的数据值;\n"
          + "b) 一个 ASDU 无法返回所有数据值时,应设置 moreFollows 为 TRUE。数据集不存在或数据集中的某一个数据无法访问时,应返回错误响应",
            "GetDataSetValues-RequestPDU::= SEQUENCE {\n"
          + "  datasetReference   [0] IMPLICIT ObjectReference,\n"
          + "  referenceAfter     [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetDataSetValues-ResponsePDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF Data,\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetDataSetValues-ErrorPDU::= ServiceError"),
    SET_DATA_SET_VALUES(
            "set-dataset-values",
            "8.5.2",
            0x3B,
            "写数据集值",
            "set-dataset-values <dsRef> <value>",
            "a) 服务的每一个数据应按数据集内的索引顺序排列;\n"
          + "b) 未指定 referenceAfter 的情况下,应从数据集的第一个成员开始设置数据值;\n"
          + "c) 指定了 referenceAfter 的情况下,应从 referenceAfter 成员之后按顺序设置数据值;\n"
          + "d) 所有数据集值设置成功时返回 Response+,部分或全部失败时返回 Response-。在 Response- 中,依次返回每个数据集值的设置结果",
            "SetDataSetValues-RequestPDU::= SEQUENCE {\n"
          + "  datasetReference   [0] IMPLICIT ObjectReference,\n"
          + "  referenceAfter     [1] IMPLICIT ObjectReference OPTIONAL,\n"
          + "  data               [2] IMPLICIT SEQUENCE OF Data\n"
          + "}\n"
          + "SetDataSetValues-ResponsePDU::= SEQUENCE {}\n"
          + "SetDataSetValues-ErrorPDU::= SEQUENCE OF ServiceError"),
    CREATE_DATA_SET(
            "create-dataset",
            "8.5.3",
            0x36,
            "创建数据集",
            "create-dataset <dsRef>",
            "a) 动态创建的数据集应支持持久数据集和非持久数据集两类。非持久性数据集在关联释放后自动删除。持久数据集即使服务器重新启动也应不自动删除;\n"
          + "b) 接收到的请求中未指定 referenceAfter 时,应创建一个新的数据集。接收到的请求中指定了 referenceAfter 时,应在现有数据集之后增加新的成员,referenceAfter 为现有数据集的最后一个成员。预定义的数据集或已关联报告控制块的数据集应不允许增加新成员;\n"
          + "c) 数据集成员为 FCD 或 FDCA",
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
            "a) 接收到的请求中未指定 referenceAfter 时,应从第一个成员开始读数据集目录;\n"
          + "b) 接收到的请求中指定了 referenceAfter 时,应从数据集的指定成员之后读数据集目录",
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
    SET_EDIT_SG_VALUE(
            "set-edit-sg-value",
            "8.6.3",
            0x56,
            "设置编辑定值值",
            "set-edit-sg-value <ref> <value>",
            "a) 设置编辑定值组值的功能约束自动识别为 SE;\n"
          + "b) 所有编辑定值组值设置成功时返回 Response+,部分或全部失败时返回 Response-。在 Response- 中,依次返回每个编辑定值组值的设置结果",
            "SetEditSGValue-RequestPDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT ObjectReference,\n"
          + "      value            [1] IMPLICIT Data\n"
          + "  }\n"
          + "}\n"
          + "SetEditSGValue-ResponsePDU::= SEQUENCE {}\n"
          + "SetEditSGValue-ErrorPDU::= SEQUENCE OF ServiceError"),
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
    GET_EDIT_SG_VALUE(
            "get-edit-sg-value",
            "8.6.5",
            0x58,
            "读编辑定值值",
            "get-edit-sg-value <ref>",
            "功能约束 fc 的值为 SG 或 SE",
            "GetEditSGValue-RequestPDU::= SEQUENCE {\n"
          + "  sgcbReference      [0] IMPLICIT ObjectReference,\n"
          + "  fc                 [1] IMPLICIT FunctionalConstraint\n"
          + "}\n"
          + "GetEditSGValue-ResponsePDU::= SEQUENCE {\n"
          + "  data               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      reference        [0] IMPLICIT ObjectReference,\n"
          + "      value            [1] IMPLICIT Data\n"
          + "  }\n"
          + "}\n"
          + "GetEditSGValue-ErrorPDU::= ServiceError"),
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
    REPORT(
            "report",
            "8.7.1",
            0x5A,
            "推送报告",
            "report",
            "a) 使用非缓存报告时,应不出现 bufOvfl、entryID 属性,而不是可选项;\n"
          + "b) 非缓存报告的 sqNum、subSqNum 表示为 INT16U 类型,取值范围在 0~255 之间;\n"
          + "c) sqNum、subSqNum、moreSegmentsFollow、datSet、bufOvfl、confRev、timeOfEntry、entryID、reference、reason 是否出现在报告中由报告控制块的选项域 OptFlds 确定。subSqNum、moreSegmentsFollow 应同时出现或不出现;\n"
          + "d) entryData 可采用 reference 索引方式,也可采用 id 索引方式。reference 和 fc 是报告数据的引用名和功能约束,id 是报告数据在数据集中的索引号,从 1 开始编号,0 为保留项",
            "Report-NotificationPDU::= SEQUENCE {\n"
          + "  sqNum              [0] IMPLICIT INT16U OPTIONAL,\n"
          + "  subSqNum           [1] IMPLICIT INT16U OPTIONAL,\n"
          + "  moreSegmentsFollow [2] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "  datSet             [3] IMPLICIT ObjectReference OPTIONAL,\n"
          + "  bufOvfl            [4] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "  confRev            [5] IMPLICIT INT32U OPTIONAL,\n"
          + "  timeOfEntry        [6] IMPLICIT EntryTime OPTIONAL,\n"
          + "  entryID            [7] IMPLICIT EntryID OPTIONAL,\n"
          + "  reference          [8] IMPLICIT SEQUENCE OF SubReference OPTIONAL,\n"
          + "  reason             [9] IMPLICIT SEQUENCE OF BIT STRING OPTIONAL,\n"
          + "  entryData          [10] IMPLICIT CHOICE {\n"
          + "      reference      [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "          reference   [0] IMPLICIT ObjectReference,\n"
          + "          fc          [1] IMPLICIT FunctionalConstraint\n"
          + "      },\n"
          + "      id             [1] IMPLICIT SEQUENCE OF INT16U\n"
          + "  } OPTIONAL\n"
          + "}"),
    GET_BRCB_VALUES(
            "get-brcb-values",
            "8.7.2",
            0x5B,
            "读缓存报告控制块",
            "get-brcb-values <brcbRef>",
            "a) 一帧报文无法返回所有缓存报告控制块的值时,服务器应按顺序返回其中的部分结果,返回的每一个控制块的值应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数队列,再次发起新的请求;\n"
          + "b) 请求队列中的某一个控制块无法访问时,应返回错误原因,并继续处理下一个控制块",
            "GetBRCBValues-RequestPDU::= SEQUENCE {\n"
          + "  brcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference\n"
          + "}\n"
          + "GetBRCBValues-ResponsePDU::= SEQUENCE {\n"
          + "  brcb               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      brcbRef         [0] IMPLICIT ObjectReference,\n"
          + "      ...\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetBRCBValues-ErrorPDU::= ServiceError"),
    SET_BRCB_VALUES(
            "set-brcb-values",
            "8.7.3",
            0x5C,
            "写缓存报告控制块",
            "set-brcb-values <brcbRef>",
            "a) 除 rptEna 之外,其他属性之间没有顺序要求,某一个属性设置失败应不影响其他属性设置;\n"
          + "b) 设置序列中含有 rptEna 且其值为 False 时,应先设置 rptEna 为 False 再设置其他属性。rptEna 值为 True 时,应先设置其他属性再设置 rptEna。属性设置未全部成功的情况下,应不继续设置 rptEna 值为 True;\n"
          + "c) 设置序列为空时,应返回 Response+,不对缓存报告控制块做任何修改;\n"
          + "d) 所有控制块均设置成功时返回 Response+,部分或全部失败时返回 Response-。在 Response- 中,无论设置成功或失败,应返回每个控制块的设置结果;\n"
          + "e) 某控制块的所有属性均设置成功的情况下,该控制块 result 的内容为空。某控制块的部分属性设置失败的情况下,该控制块 result 中应包含设置失败的属性,设置成功的属性不需列入",
            "SetBRCBValues-RequestPDU::= SEQUENCE {\n"
          + "  brcb               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      brcbRef         [0] IMPLICIT ObjectReference,\n"
          + "      rptEna          [1] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "      ...\n"
          + "  }\n"
          + "}\n"
          + "SetBRCBValues-ResponsePDU::= SEQUENCE {}\n"
          + "SetBRCBValues-ErrorPDU::= SEQUENCE OF SEQUENCE {\n"
          + "  reference          [0] IMPLICIT ObjectReference,\n"
          + "  result             [1] IMPLICIT SEQUENCE OF ServiceError OPTIONAL\n"
          + "}"),
    GET_URCB_VALUES(
            "get-urcb-values",
            "8.7.4",
            0x5D,
            "读非缓存报告控制块",
            "get-urcb-values <urcbRef>",
            "a) 一帧报文无法返回所有非缓存报告控制块的值时,服务器应按顺序返回其中的部分结果,返回的每一个控制块的值应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数队列,再次发起新的请求;\n"
          + "b) 请求队列中的某一个控制块无法访问时,应返回错误原因,并继续处理下一个控制块",
            "GetURCBValues-RequestPDU::= SEQUENCE {\n"
          + "  urcbReference      [0] IMPLICIT SEQUENCE OF ObjectReference\n"
          + "}\n"
          + "GetURCBValues-ResponsePDU::= SEQUENCE {\n"
          + "  urcb               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      urcbRef         [0] IMPLICIT ObjectReference,\n"
          + "      ...\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetURCBValues-ErrorPDU::= ServiceError"),
    SET_URCB_VALUES(
            "set-urcb-values",
            "8.7.5",
            0x5E,
            "写非缓存报告控制块",
            "set-urcb-values <urcbRef>",
            "a) 除 rptEna 外,其他属性之间没有顺序要求,某一个属性设置失败应不影响其他属性设置;\n"
          + "b) 设置序列中含有 rptEna 且其值为 False 时,应先设置 rptEna 为 False 再设置其他属性。rptEna 值为 True 时,应先设置其他属性再设置 rptEna。属性设置未全部成功的情况下,应不继续设置 rptEna 值为 True;\n"
          + "c) 设置序列为空时,应返回 Response+,不对非缓存报告控制块做任何修改;\n"
          + "d) 所有控制块均设置成功时返回 Response+,部分或全部失败时返回 Response-。在 Response- 中,无论设置成功或失败,应返回每个控制块的设置结果;\n"
          + "e) 某控制块的所有属性均设置成功的情况下,该控制块 result 的内容应为空。某控制块的部分属性设置失败的情况下,该控制块 result 中应包含设置失败的属性,设置成功的属性不需列入",
            "SetURCBValues-RequestPDU::= SEQUENCE {\n"
          + "  urcb               [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      urcbRef         [0] IMPLICIT ObjectReference,\n"
          + "      rptEna          [1] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "      ...\n"
          + "  }\n"
          + "}\n"
          + "SetURCBValues-ResponsePDU::= SEQUENCE {}\n"
          + "SetURCBValues-ErrorPDU::= SEQUENCE OF SEQUENCE {\n"
          + "  reference          [0] IMPLICIT ObjectReference,\n"
          + "  result             [1] IMPLICIT SEQUENCE OF ServiceError OPTIONAL\n"
          + "}"),

    // ==================== 8.8 日志服务 ====================
    GET_LCB_VALUES          ("get-lcb-values",      "8.8.2", 0x5F, "读日志控制块",      "get-lcb-values <lcbRef>"),
    SET_LCB_VALUES(
            "set-lcb-values",
            "8.8.3",
            0x60,
            "写日志控制块",
            "set-lcb-values <lcbRef>",
            "a) 除 logEna 外,其他属性之间没有顺序要求,某一个属性设置失败应不影响其他属性设置;\n"
          + "b) 设置序列中含有 logEna 且其值为 False 时,应先设置 logEna 为 False 再设置其他属性。logEna 值为 True 时,应先设置其他属性再设置 logEna。属性设置未全部成功的情况下,应不继续设置 logEna 值为 True;\n"
          + "c) 设置序列为空时,应返回 Response+,不对日志控制块做任何修改",
            "SetLCBValues-RequestPDU::= SEQUENCE {\n"
          + "  lcb                [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      lcbRef          [0] IMPLICIT ObjectReference,\n"
          + "      logEna          [1] IMPLICIT BOOLEAN OPTIONAL,\n"
          + "      ...\n"
          + "  }\n"
          + "}\n"
          + "SetLCBValues-ResponsePDU::= SEQUENCE {}\n"
          + "SetLCBValues-ErrorPDU::= ServiceError"),
    QUERY_LOG_BY_TIME(
            "query-log-by-time",
            "8.8.4",
            0x61,
            "按时间查询日志",
            "query-log-by-time <lcbRef> <start> <stop>",
            "查询得到的日志条目过多且无法在一次响应中返回时,服务器应设置 moreFollows 为 TRUE,以通知客户未能返回全部查询结果。客户可以用时间和 ID 再一次发起查询请求",
            "QueryLogByTime-RequestPDU::= SEQUENCE {\n"
          + "  lcbRef             [0] IMPLICIT ObjectReference,\n"
          + "  entryTimeStart     [1] IMPLICIT EntryTime,\n"
          + "  entryTimeStop      [2] IMPLICIT EntryTime\n"
          + "}\n"
          + "QueryLogByTime-ResponsePDU::= SEQUENCE {\n"
          + "  logEntry           [0] IMPLICIT SEQUENCE OF LogEntry,\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "QueryLogByTime-ErrorPDU::= ServiceError"),
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
            "因为不同控制对象的数据类型不同,所以 ctlVal 的类型由模型定义。客户可通过解析模型文件获取 ctlVal 的类型,也可通过 8.4.4 的 GetDataDefinition 服务获取 ctlVal 的类型。以下控制类服务中的 ctlVal 与此相同",
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

    // ==================== 8.13 远程过程调用 ====================
    GET_RPC_INTERFACE_DIRECTORY     ("iface-dir",   "8.13.1", 0x6E, "获取RPC接口目录",      "iface-dir [after]"),
    GET_RPC_METHOD_DIRECTORY(
            "method-dir",
            "8.13.2",
            0x6F,
            "获取RPC方法目录",
            "method-dir [iface] [after]",
            "a) 没有指定 interface 时,表示需要获取所有调用接口的所有方法的名称,参数 referenceAfter 和 reference 应使用完整的引用名,格式见 8.13.1;\n"
          + "b) DataDefinition、Data 仅用于描述实例化后的数据,所以应不使用 OPTIONAL 和 Default 语法,请求和响应参数均应是明确定义的结构"),
    GET_RPC_INTERFACE_DEFINITION(
            "iface-def",
            "8.13.4",
            0x70,
            "获取RPC接口定义",
            "iface-def [iface] [after]",
            "a) version 用于客户和服务器间的版本适配。客户应提供向前兼容的能力,服务器提供方法的版本低于客户时,应能正确适配并进行调用;\n"
          + "b) 若客户版本低于服务器版本,应及时对客户程序进行升级,而应不强行发起调用请求;\n"
          + "c) timeout 用于说明服务器执行该方法的超时时间。超过 timeout 定义的时间仍未收到服务器响应的情况下,客户可认为该请求失败;\n"
          + "d) 读远程过程调用接口定义服务应返回指定接口的所有方法的定义。一帧报文无法返回所有方法时,服务器应按顺序返回其中的部分结果,返回的每一个方法应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数 referenceAfter,再次发起新的读远程过程调用接口定义请求",
            "GetRpcInterfaceDefinition-RequestPDU::= SEQUENCE {\n"
          + "  interfaceName      [0] IMPLICIT VisibleString,\n"
          + "  referenceAfter     [1] IMPLICIT ObjectReference OPTIONAL\n"
          + "}\n"
          + "GetRpcInterfaceDefinition-ResponsePDU::= SEQUENCE {\n"
          + "  method             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
          + "      name            [0] IMPLICIT VisibleString,\n"
          + "      version         [1] IMPLICIT INT32U,\n"
          + "      timeout         [2] IMPLICIT INT32U\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetRpcInterfaceDefinition-ErrorPDU::= ServiceError"),
    GET_RPC_METHOD_DEFINITION(
            "method-def",
            "8.13.5",
            0x71,
            "获取RPC方法定义",
            "method-def <refs>",
            "a) 一帧报文无法返回所有方法的定义时,服务器应按顺序返回其中的部分结果,返回的每一组定义应是完整的,同时设置 moreFollows 参数,通知客户数据未能完全响应。客户应根据响应的结果,修改参数队列,再次发起新的读远程过程调用方法定义请求;\n"
          + "b) 请求队列中的某一个方法无法访问时,应返回错误原因,并继续处理下一个方法",
            "GetRpcMethodDefinition-RequestPDU::= SEQUENCE {\n"
          + "  methodReference    [0] IMPLICIT SEQUENCE OF ObjectReference\n"
          + "}\n"
          + "GetRpcMethodDefinition-ResponsePDU::= SEQUENCE {\n"
          + "  method             [0] IMPLICIT SEQUENCE OF CHOICE {\n"
          + "      error           [0] IMPLICIT ServiceError,\n"
          + "      method          [1] IMPLICIT SEQUENCE {\n"
          + "          timeout     [0] IMPLICIT INT32U,\n"
          + "          version     [1] IMPLICIT INT32U\n"
          + "      }\n"
          + "  },\n"
          + "  moreFollows        [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
          + "}\n"
          + "GetRpcMethodDefinition-ErrorPDU::= ServiceError"),
    RPC_CALL(
            "rpc",
            "8.13.6",
            0x72,
            "远程过程调用",
            "rpc <methodRef> [data]",
            "a) 一帧报文无法返回所有结果时,服务器应按顺序返回其中的部分结果,返回的每一组结果应是完整的,同时设置 nextCallID 参数,通知客户数据未能完全响应;\n"
          + "b) nextCallID 是一组十六进制串,其含义由服务器定义,服务器应能根据这一组十六进制串直接定位到上一次调用的位置并继续执行。客户识别出响应结果中含有 nextCallID 时,应再次发起新的调用请求,参数 method 应与前一次调用相同,参数 callID 设置为前一次响应返回的 nextCallID。服务器继续执行未完成的调用,直至调用全部完成",
            "RpcCall-RequestPDU::= SEQUENCE {\n"
          + "  method             [0] IMPLICIT VisibleString,\n"
          + "  callID             [1] IMPLICIT OCTET STRING OPTIONAL,\n"
          + "  reqData            [2] IMPLICIT Data OPTIONAL\n"
          + "}\n"
          + "RpcCall-ResponsePDU::= SEQUENCE {\n"
          + "  rspData            [0] IMPLICIT Data OPTIONAL,\n"
          + "  nextCallID         [1] IMPLICIT OCTET STRING OPTIONAL\n"
          + "}\n"
          + "RpcCall-ErrorPDU::= ServiceError"),

    // ==================== 8.12 文件服务 ====================
    GET_FILE(
            "file-get",
            "8.12.1",
            0x80,
            "读文件",
            "file-get <fileName> [startPosition]",
            "a) fileName 应使用完整路径,以\"/\"起始;\n"
          + "b) 每一个 GetFile 请求,服务器只返回一个响应。客户应重复请求不同起始位置的数据,直到文件结束。文件读取结束时,服务器可关闭所读的文件。客户长时间未读后续数据的情况下,服务器应具有超时机制,自动关闭相关文件;\n"
          + "c) startPosition 等于 0 时,表示客户放弃读取后续数据,服务器应关闭所读的文件",
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
            "8.12.2",
            0x81,
            "写文件",
            "file-set <fileName> <start> <data> [eof]",
            "a) 根据写入文件的长度,客户应发起多个 SetFile 请求。第一个请求的 startPosition 为 1,后续每个请求的 startPosition 和 fileData 应是连续的;\n"
          + "b) startPosition 等于 0 时,表示客户放弃写入后续数据,服务器应关闭并删除未完成的文件;\n"
          + "c) 文件写入结束时,服务器应保存所写的文件。客户长时间未写入后续数据的情况下,服务器应具有超时机制,自动关闭并删除不完整的文件",
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
            "8.12.3",
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
            "8.12.4",
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
            "8.12.5",
            0x84,
            "列文件目录",
            "file-dir [path] [after]",
            "a) startTime 和 stopTime 表示文件目录的起始和截止时间,返回结果应在起始和截止时间之间,包含起始和截止时间;\n"
          + "b) pathName 应采用完整路径名,格式为\"/××××××\"",
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
    TEST(
            "test",
            "8.14.1",
            0x99,
            "测试",
            "test",
            "测试服务用于通信连接的双方测试链路是否正常、对方是否能正确响应请求。当接收到 Test 帧时,应立刻返回一个 Test 帧。测试服务仅有 APCH 头,不含 ASDU 部分,帧长度 FL=0"),

    // ==================== 8.15 协商服务 ====================
    ASSOCIATE_NEGOTIATE(
            "negotiate",
            "8.15.1",
            0x9A,
            "协商通信参数",
            "negotiate",
            "a) 服务器应根据客户该参数,结合自身 APDU 帧大小,返回可支持的 APDU 帧大小,作为协商结果。通信双方后续通信服务中 APDU 帧大小应采用服务器响应的该参数;\n"
          + "b) apduSize 大于 asduSize 表示支持分帧传输,小于 apduSize 表示不能实现分帧传输数据。通信双方应记录对侧所能支持的 ASDU 大小,并据此组织 ASDU 数据帧,确保不超出对侧的能力;\n"
          + "c) 接收方应检查自身是否能够支持对端协议的版本,并采用对应版本的协议报文进行通信。无法支持该版本时,应返回协商失败",
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

    ServiceInfo(String cliName, String section, int serviceCode, String description, String usage, String descriptionDetail) {
        this(cliName, section, serviceCode, description, usage, descriptionDetail, "");
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
