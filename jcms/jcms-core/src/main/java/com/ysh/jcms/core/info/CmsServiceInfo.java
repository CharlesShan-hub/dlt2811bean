package com.ysh.jcms.core.info;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * DL/T 2811 service definitions according to cms.asn1. Each service has a name,
 * section reference, service code (SC), and corresponding PDU types.
 */
@Getter
@Accessors(fluent = true)
public enum CmsServiceInfo {

    // ==================== 8.2 Association services ====================
    ASSOCIATE("associate", "8.2.1", 0x01, "Associate", "建立应用层关联",
            "Establish an application-layer association between client and server",
            "Associate-RequestPDU ::= SEQUENCE {\n"
                    + "    serverAccessPointReference    [0] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,\n"
                    + "    authenticationParameter       [1] IMPLICIT SEQUENCE {\n"
                    + "        signatureCertificate        [0] IMPLICIT OCTET STRING,\n"
                    + "        signedTime                  [1] IMPLICIT UtcTime,\n"
                    + "        signedValue                 [2] IMPLICIT OCTET STRING\n"
                    + "    } OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "Associate-ResponsePDU ::= SEQUENCE {\n"
                    + "    associationId                  [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
                    + "    serviceError                   [1] IMPLICIT ServiceError,\n"
                    + "    authenticationParameter        [2] IMPLICIT SEQUENCE {\n"
                    + "        signatureCertificate        [0] IMPLICIT OCTET STRING,\n"
                    + "        signedTime                  [1] IMPLICIT UtcTime,\n"
                    + "        signedValue                 [2] IMPLICIT OCTET STRING\n"
                    + "    } OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "Associate-ErrorPDU ::= ServiceError"),
    RELEASE("release", "8.2.2", 0x03, "Release", "释放应用层关联",
            "Release an established application-layer association",
            "Release-RequestPDU ::= SEQUENCE {\n"
                    + "    associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64))\n"
                    + "}\n"
                    + "\n"
                    + "Release-ResponsePDU ::= SEQUENCE {\n"
                    + "    associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
                    + "    serviceError     [1] IMPLICIT ServiceError\n"
                    + "}\n"
                    + "\n"
                    + "Release-ErrorPDU ::= ServiceError"),
    ABORT("abort", "8.2.3", 0x02, "Abort", "中止关联",
            "Abort an application-layer association",
            "Abort-RequestPDU ::= SEQUENCE {\n"
                    + "    associationId    [0] IMPLICIT OCTET STRING (SIZE (0..64)),\n"
                    + "    reason           [1] IMPLICIT INTEGER {\n"
                    + "        other                  (0),\n"
                    + "        unrecognized-service   (1),\n"
                    + "        invalid-reqID          (2),\n"
                    + "        invalid-argument       (3),\n"
                    + "        invalid-result         (4),\n"
                    + "        max-serv-outstanding-exceeded (5)\n"
                    + "    } (0..5)\n"
                    + "}"),
    ASSOCIATE_NEGOTIATE("negotiate", "8.15", 0x9A, "Associate Negotiate", "协商关联参数",
            "Negotiate association parameters (apduSize, asduSize, protocolVersion)",
            "AssociateNegotiate-RequestPDU ::= SEQUENCE {\n"
                    + "    apduSize        [0] IMPLICIT Int16U,\n"
                    + "    asduSize        [1] IMPLICIT Int32U,\n"
                    + "    protocolVersion [2] IMPLICIT Int32U\n"
                    + "}\n"
                    + "\n"
                    + "AssociateNegotiate-ResponsePDU ::= SEQUENCE {\n"
                    + "    apduSize        [0] IMPLICIT Int16U,\n"
                    + "    asduSize        [1] IMPLICIT Int32U,\n"
                    + "    protocolVersion [2] IMPLICIT Int32U,\n"
                    + "    modelVersion    [3] IMPLICIT VisibleString\n"
                    + "}\n"
                    + "\n"
                    + "AssociateNegotiate-ErrorPDU ::= ServiceError"),

    // ==================== 8.3 Directory services ====================
    GET_SERVER_DIRECTORY("server-dir", "8.3.1", 0x50, "Get Server Directory", "读服务器目录",
            "Retrieve the logical device directory from the server",
            "GetServerDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    objectClass      [0] IMPLICIT INTEGER {\n"
                    + "        reserved        (0),\n"
                    + "        logical-device  (1),\n"
                    + "        file-system     (2)\n"
                    + "    } (0..2),\n"
                    + "    referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetServerDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference        [0] IMPLICIT SEQUENCE OF ObjectReference,\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetServerDirectory-ErrorPDU ::= ServiceError"),
    GET_LOGIC_DEVICE_DIRECTORY("ld-dir", "8.3.2", 0x51, "Get Logical Device Directory", "读逻辑设备目录",
            "Retrieve logical node references under a logical device",
            "GetLogicalDeviceDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    ldName            [0] IMPLICIT ObjectName OPTIONAL,\n"
                    + "    referenceAfter    [1] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetLogicalDeviceDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    lnReference       [0] IMPLICIT SEQUENCE OF SubReference,\n"
                    + "    moreFollows       [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetLogicalDeviceDirectory-ErrorPDU ::= ServiceError"),
    GET_LOGIC_NODE_DIRECTORY("ln-dir", "8.3.3", 0x52, "Get Logical Node Directory", "读逻辑节点目录",
            "Retrieve data references under a logical node",
            "GetLogicalNodeDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT CHOICE {\n"
                    + "        ldName         [0] IMPLICIT ObjectName,\n"
                    + "        lnReference    [1] IMPLICIT ObjectReference\n"
                    + "    },\n"
                    + "    acsiClass       [1] IMPLICIT ACSIClass,\n"
                    + "    referenceAfter  [2] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetLogicalNodeDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF SubReference,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetLogicalNodeDirectory-ErrorPDU ::= ServiceError"),
    GET_ALL_DATA_VALUES("get-all-values", "8.3.4", 0x53, "Get All Data Values", "读所有数据值",
            "Retrieve all data values for a logical device or node",
            "GetAllDataValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference        [0] IMPLICIT CHOICE {\n"
                    + "        ldName         [0] IMPLICIT ObjectName,\n"
                    + "        lnReference    [1] IMPLICIT ObjectReference\n"
                    + "    },\n"
                    + "    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
                    + "    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetAllDataValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT SubReference,\n"
                    + "        value         [1] IMPLICIT Data\n"
                    + "    },\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetAllDataValues-ErrorPDU ::= ServiceError"),
    GET_ALL_DATA_DEFINITION("get-all-def", "8.3.5", 0x9B, "Get All Data Definition", "读所有数据定义",
            "Retrieve all data type definitions for a logical device or node",
            "GetAllDataDefinition-RequestPDU ::= SEQUENCE {\n"
                    + "    reference        [0] IMPLICIT CHOICE {\n"
                    + "        ldName         [0] IMPLICIT ObjectName,\n"
                    + "        lnReference    [1] IMPLICIT ObjectReference\n"
                    + "    },\n"
                    + "    fc               [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
                    + "    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetAllDataDefinition-ResponsePDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT SubReference,\n"
                    + "        cdcType       [1] IMPLICIT VisibleString OPTIONAL,\n"
                    + "        definition    [2] IMPLICIT DataDefinition\n"
                    + "    },\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetAllDataDefinition-ErrorPDU ::= ServiceError"),
    GET_ALL_CB_VALUES("get-all-cb", "8.3.6", 0x9C, "Get All CB Values", "读所有控制块",
            "Retrieve all control block values for a logical device or node",
            "GetAllCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference        [0] IMPLICIT CHOICE {\n"
                    + "        ldName         [0] IMPLICIT ObjectName,\n"
                    + "        lnReference    [1] IMPLICIT ObjectReference\n"
                    + "    },\n"
                    + "    acsiClass        [1] IMPLICIT ACSIClass,\n"
                    + "    referenceAfter   [2] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetAllCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    cbValue          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT SubReference,\n"
                    + "        value         [1] IMPLICIT CHOICE {\n"
                    + "            brcb        [0] IMPLICIT BRCB,\n"
                    + "            urcb        [1] IMPLICIT URCB,\n"
                    + "            lcb         [2] IMPLICIT LCB,\n"
                    + "            sgcb        [3] IMPLICIT SGCB,\n"
                    + "            gocb        [4] IMPLICIT GOCB,\n"
                    + "            msvcb       [5] IMPLICIT MSVCB\n"
                    + "        }\n"
                    + "    },\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetAllCBValues-ErrorPDU ::= ServiceError"),

    // ==================== 8.4 Data access services ====================
    GET_DATA_VALUES("get-data-values", "8.4.1", 0x30, "Get Data Values", "读数据值",
            "Read values of specified data references",
            "GetDataValues-RequestPDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT ObjectReference,\n"
                    + "        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "GetDataValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    value            [0] IMPLICIT SEQUENCE OF Data,\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetDataValues-ErrorPDU ::= ServiceError"),
    SET_DATA_VALUES("set-data-values", "8.4.2", 0x31, "Set Data Values", "写数据值",
            "Write values to specified data references",
            "SetDataValues-RequestPDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT ObjectReference,\n"
                    + "        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
                    + "        value         [2] IMPLICIT Data\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetDataValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetDataValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result           [0] IMPLICIT SEQUENCE OF ServiceError\n"
                    + "}"),
    GET_DATA_DIRECTORY("get-data-dir", "8.4.3", 0x32, "Get Data Directory", "读数据目录",
            "Retrieve child data references under a data reference",
            "GetDataDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    dataReference    [0] IMPLICIT ObjectReference,\n"
                    + "    referenceAfter   [1] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetDataDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    dataAttribute    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT SubReference,\n"
                    + "        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
                    + "    },\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetDataDirectory-ErrorPDU ::= ServiceError"),
    GET_DATA_DEFINITION("get-data-def", "8.4.4", 0x33, "Get Data Definition", "读数据定义",
            "Retrieve type definitions for specified data references",
            "GetDataDefinition-RequestPDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference     [0] IMPLICIT ObjectReference,\n"
                    + "        fc            [1] IMPLICIT FunctionalConstraint OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "GetDataDefinition-ResponsePDU ::= SEQUENCE {\n"
                    + "    data             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        cdcType       [0] IMPLICIT VisibleString OPTIONAL,\n"
                    + "        definition    [1] IMPLICIT DataDefinition\n"
                    + "    },\n"
                    + "    moreFollows      [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetDataDefinition-ErrorPDU ::= ServiceError"),

    // ==================== 8.5 Data set services ====================
    GET_DATA_SET_VALUES("get-dataset-values", "8.5.1", 0x3A, "Get Data Set Values", "读数据集值",
            "Read the values of all members in a data set",
            "GetDataSetValues-RequestPDU ::= SEQUENCE {\n"
                    + "    datasetReference    [0] IMPLICIT ObjectReference,\n"
                    + "    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetDataSetValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    value               [0] IMPLICIT SEQUENCE OF Data,\n"
                    + "    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetDataSetValues-ErrorPDU ::= ServiceError"),
    SET_DATA_SET_VALUES("set-dataset-values", "8.5.2", 0x3B, "Set Data Set Values", "写数据集值",
            "Write values to members of a data set",
            "SetDataSetValues-RequestPDU ::= SEQUENCE {\n"
                    + "    datasetReference    [0] IMPLICIT ObjectReference,\n"
                    + "    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    value               [2] IMPLICIT SEQUENCE OF Data\n"
                    + "}\n"
                    + "\n"
                    + "SetDataSetValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetDataSetValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result              [0] IMPLICIT SEQUENCE OF ServiceError\n"
                    + "}"),
    CREATE_DATA_SET("create-dataset", "8.5.3", 0x36, "Create Data Set", "创建数据集",
            "Dynamically create a new data set",
            "CreateDataSet-RequestPDU ::= SEQUENCE {\n"
                    + "    datasetReference    [0] IMPLICIT ObjectReference,\n"
                    + "    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    memberData          [2] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference       [0] IMPLICIT ObjectReference,\n"
                    + "        fc              [1] IMPLICIT FunctionalConstraint\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "CreateDataSet-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "CreateDataSet-ErrorPDU ::= ServiceError"),
    DELETE_DATA_SET("delete-dataset", "8.5.4", 0x37, "Delete Data Set", "删除数据集",
            "Delete a previously created data set",
            "DeleteDataSet-RequestPDU ::= SEQUENCE {\n"
                    + "    datasetReference    [0] IMPLICIT ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "DeleteDataSet-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "DeleteDataSet-ErrorPDU ::= ServiceError"),
    GET_DATA_SET_DIRECTORY("get-dataset-dir", "8.5.5", 0x39, "Get Data Set Directory", "读数据集目录",
            "Retrieve member data references of a data set",
            "GetDataSetDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    datasetReference    [0] IMPLICIT ObjectReference,\n"
                    + "    referenceAfter      [1] IMPLICIT ObjectReference OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetDataSetDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    memberData          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference       [0] IMPLICIT ObjectReference,\n"
                    + "        fc              [1] IMPLICIT FunctionalConstraint\n"
                    + "    },\n"
                    + "    moreFollows         [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetDataSetDirectory-ErrorPDU ::= ServiceError"),

    // ==================== 8.6 Setting group services ====================
    SELECT_ACTIVE_SG("select-active-sg", "8.6.1", 0x54, "Select Active SG", "选择激活定值组",
            "Select the active setting group",
            "SelectActiveSG-RequestPDU ::= SEQUENCE {\n"
                    + "    sgcbReference       [0] IMPLICIT ObjectReference,\n"
                    + "    settingGroupNumber  [1] IMPLICIT Int8U\n"
                    + "}\n"
                    + "\n"
                    + "SelectActiveSG-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SelectActiveSG-ErrorPDU ::= ServiceError"),
    SELECT_EDIT_SG("select-edit-sg", "8.6.2", 0x55, "Select Edit SG", "选择编辑定值组",
            "Select the edit setting group",
            "SelectEditSG-RequestPDU ::= SEQUENCE {\n"
                    + "    sgcbReference       [0] IMPLICIT ObjectReference,\n"
                    + "    settingGroupNumber  [1] IMPLICIT Int8U\n"
                    + "}\n"
                    + "\n"
                    + "SelectEditSG-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SelectEditSG-ErrorPDU ::= ServiceError"),
    SET_EDIT_SG_VALUE("set-edit-sg-value", "8.6.3", 0x56, "Set Edit SG Value", "设置编辑定值值",
            "Set values in the edit buffer of a setting group",
            "SetEditSGValue-RequestPDU ::= SEQUENCE {\n"
                    + "    data    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        value       [2] IMPLICIT Data\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetEditSGValue-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetEditSGValue-ErrorPDU ::= SEQUENCE {\n"
                    + "    result  [0] IMPLICIT SEQUENCE OF ServiceError\n"
                    + "}"),
    CONFIRM_EDIT_SG_VALUES("confirm-edit-sg", "8.6.4", 0x57, "Confirm Edit SG Values", "确认编辑定值",
            "Commit the edit buffer values to the setting group",
            "ConfirmEditSGValues-RequestPDU ::= SEQUENCE {\n"
                    + "    sgcbReference       [0] IMPLICIT ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "ConfirmEditSGValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "ConfirmEditSGValues-ErrorPDU ::= ServiceError"),
    GET_EDIT_SG_VALUE("get-edit-sg-value", "8.6.5", 0x58, "Get Edit SG Value", "读编辑定值值",
            "Read values from the edit buffer of a setting group",
            "GetEditSGValue-RequestPDU ::= SEQUENCE {\n"
                    + "    data    [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        fc          [1] IMPLICIT FunctionalConstraint\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "GetEditSGValue-ResponsePDU ::= SEQUENCE {\n"
                    + "    value           [0] IMPLICIT SEQUENCE OF Data,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetEditSGValue-ErrorPDU ::= ServiceError"),
    GET_SGCB_VALUES("get-sgcb-values", "8.6.6", 0x59, "Get SGCB Values", "读定值组控制块",
            "Retrieve SGCB (Setting Group Control Block) values",
            "GetSGCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    sgcbReference  [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetSGCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    sgscb          [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT SGCB\n"
                    + "    },\n"
                    + "    moreFollows    [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetSGCBValues-ErrorPDU ::= ServiceError"),

    // ==================== 8.7 Reporting services ====================
    REPORT("report", "8.7.1", 0x5A, "Report", "推送报告",
            "Unconfirmed report notification from server to client",
            "ReportPDU ::= SEQUENCE {\n"
                    + "    rptID           [0] IMPLICIT VisibleString (SIZE (0..129)),\n"
                    + "    optFlds         [1] IMPLICIT RcbOptFlds,\n"
                    + "    sqNum           [2] IMPLICIT Int16U OPTIONAL,\n"
                    + "    subSeqNum       [3] IMPLICIT Int16U OPTIONAL,\n"
                    + "    moreSegmentsFollow [4] IMPLICIT Boolean OPTIONAL,\n"
                    + "    dataSet         [5] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    bufOvfl         [6] IMPLICIT Boolean OPTIONAL,\n"
                    + "    confRev         [7] IMPLICIT Int32U OPTIONAL,\n"
                    + "    entry           [8] IMPLICIT SEQUENCE {\n"
                    + "        timeOfEntry     [0] IMPLICIT EntryTime OPTIONAL,\n"
                    + "        entryID         [1] IMPLICIT EntryID OPTIONAL,\n"
                    + "        entryData       [2] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "            reference   [0] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "            fc          [1] IMPLICIT FunctionalConstraint OPTIONAL,\n"
                    + "            id          [2] IMPLICIT Int16U,\n"
                    + "            value       [3] IMPLICIT Data,\n"
                    + "            reason      [4] IMPLICIT ReasonCode OPTIONAL\n"
                    + "        }\n"
                    + "    }\n"
                    + "}"),
    GET_BRCB_VALUES("get-brcb-values", "8.7.2", 0x5B, "Get BRCB Values", "读缓存报告控制块",
            "Retrieve BRCB (Buffered Report CB) values",
            "GetBRCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetBRCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    brcb            [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT BRCB\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetBRCBValues-ErrorPDU ::= ServiceError"),
    SET_BRCB_VALUES("set-brcb-values", "8.7.3", 0x5C, "Set BRCB Values", "写缓存报告控制块",
            "Modify BRCB (Buffered Report CB) attributes",
            "SetBRCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    brcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        rptID       [1] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,\n"
                    + "        rptEna      [2] IMPLICIT Boolean OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "        optFlds     [5] IMPLICIT RcbOptFlds OPTIONAL,\n"
                    + "        bufTm       [6] IMPLICIT Int32U OPTIONAL,\n"
                    + "        trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,\n"
                    + "        intgPd      [9] IMPLICIT Int32U OPTIONAL,\n"
                    + "        gi          [10] IMPLICIT Boolean OPTIONAL,\n"
                    + "        purgeBuf    [11] IMPLICIT Boolean OPTIONAL,\n"
                    + "        entryID     [12] IMPLICIT EntryID OPTIONAL,\n"
                    + "        resvTms     [13] IMPLICIT Int16 OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetBRCBValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetBRCBValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        error       [0] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        rptID       [1] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        rptEna      [2] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        optFlds     [5] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        bufTm       [6] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        trgOps      [8] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        intgPd      [9] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        gi          [10] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        purgeBuf    [11] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        entryID     [12] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        resvTms     [14] IMPLICIT ServiceError OPTIONAL\n"
                    + "    }\n"
                    + "}"),
    GET_URCB_VALUES("get-urcb-values", "8.7.4", 0x5D, "Get URCB Values", "读非缓存报告控制块",
            "Retrieve URCB (Unbuffered Report CB) values",
            "GetURCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetURCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    urcb            [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT URCB\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetURCBValues-ErrorPDU ::= ServiceError"),
    SET_URCB_VALUES("set-urcb-values", "8.7.5", 0x5E, "Set URCB Values", "写非缓存报告控制块",
            "Modify URCB (Unbuffered Report CB) attributes",
            "SetURCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    urcb            [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        rptID       [1] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,\n"
                    + "        rptEna      [2] IMPLICIT Boolean OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "        optFlds     [5] IMPLICIT RcbOptFlds OPTIONAL,\n"
                    + "        bufTm       [6] IMPLICIT Int32U OPTIONAL,\n"
                    + "        trgOps      [8] IMPLICIT TriggerConditions OPTIONAL,\n"
                    + "        intgPd      [9] IMPLICIT Int32U OPTIONAL,\n"
                    + "        gi          [10] IMPLICIT Boolean OPTIONAL,\n"
                    + "        resv        [13] IMPLICIT Boolean OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetURCBValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetURCBValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        error       [0] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        rptID       [1] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        rptEna      [2] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        optFlds     [5] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        bufTm       [6] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        trgOps      [8] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        intgPd      [9] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        gi          [10] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        resv        [13] IMPLICIT ServiceError OPTIONAL\n"
                    + "    }\n"
                    + "}"),

    // ==================== 8.8 Logging services ====================
    GET_LCB_VALUES("get-lcb-values", "8.8.2", 0x5F, "Get LCB Values", "读日志控制块",
            "Retrieve LCB (Log Control Block) values",
            "GetLCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetLCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    lcb             [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT LCB\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetLCBValues-ErrorPDU ::= ServiceError"),
    SET_LCB_VALUES("set-lcb-values", "8.8.3", 0x60, "Set LCB Values", "写日志控制块",
            "Modify LCB (Log Control Block) attributes",
            "SetLCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    lcb             [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        logEna      [1] IMPLICIT Boolean OPTIONAL,\n"
                    + "        datSet      [2] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "        trgOps      [3] IMPLICIT TriggerConditions OPTIONAL,\n"
                    + "        intgPd      [4] IMPLICIT Int32U OPTIONAL,\n"
                    + "        logRef      [5] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "        optFlds     [6] IMPLICIT LcbOptFlds OPTIONAL,\n"
                    + "        bufTm       [7] IMPLICIT Int32U OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetLCBValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetLCBValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        error       [0] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        logEna      [1] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        datSet      [2] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        trgOps      [3] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        intgPd      [4] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        logRef      [5] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        optFlds     [6] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        bufTm       [7] IMPLICIT ServiceError OPTIONAL\n"
                    + "    }\n"
                    + "}"),
    QUERY_LOG_BY_TIME("query-log-time", "8.8.4", 0x61, "Query Log By Time", "按时间查询日志",
            "Query log entries within a time range",
            "QueryLogByTime-RequestPDU ::= SEQUENCE {\n"
                    + "    logReference    [0] IMPLICIT ObjectReference,\n"
                    + "    startTime       [1] IMPLICIT EntryTime OPTIONAL,\n"
                    + "    stopTime        [2] IMPLICIT EntryTime OPTIONAL,\n"
                    + "    entryAfter      [3] IMPLICIT EntryID OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "QueryLogByTime-ResponsePDU ::= SEQUENCE {\n"
                    + "    logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "QueryLogByTime-ErrorPDU ::= ServiceError"),
    QUERY_LOG_AFTER("query-log-after", "8.8.5", 0x62, "Query Log After", "按条目查询日志",
            "Query log entries after a given entry",
            "QueryLogAfter-RequestPDU ::= SEQUENCE {\n"
                    + "    logReference    [0] IMPLICIT ObjectReference,\n"
                    + "    startTime       [1] IMPLICIT EntryTime OPTIONAL,\n"
                    + "    entry           [2] IMPLICIT EntryID\n"
                    + "}\n"
                    + "\n"
                    + "QueryLogAfter-ResponsePDU ::= SEQUENCE {\n"
                    + "    logEntry        [0] IMPLICIT SEQUENCE OF LogEntry,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "QueryLogAfter-ErrorPDU ::= ServiceError"),
    GET_LOG_STATUS_VALUES("get-log-status", "8.8.6", 0x63, "Get Log Status Values", "读日志状态值",
            "Retrieve log status information (oldest/newest entries)",
            "GetLogStatusValues-RequestPDU ::= SEQUENCE {\n"
                    + "    logReference    [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetLogStatusValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    log             [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT SEQUENCE {\n"
                    + "            oldEntrTm   [0] IMPLICIT EntryTime,\n"
                    + "            newEntrTm   [1] IMPLICIT EntryTime,\n"
                    + "            oldEntr     [2] IMPLICIT EntryID,\n"
                    + "            newEntr     [3] IMPLICIT EntryID\n"
                    + "        }\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetLogStatusValues-ErrorPDU ::= ServiceError"),

    // ==================== 8.9 GOOSE services ====================
    // Unconfirmed services (SendGOOSEMessage / GetGoReference / GetGOOSEElementNumber) have no code in standard Table 1; 0x00 placeholder
    SEND_GOOSE_MESSAGE("send-goose", "8.9.1", 0x00, "Send GOOSE Message", "发送GOOSE报文",
            "Unconfirmed GOOSE message from server",
            "SendGOOSEMessage-PDU ::= SEQUENCE {\n"
                    + "    goID            [0] IMPLICIT VisibleString (SIZE (0..129)),\n"
                    + "    datSet          [1] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    goRef           [2] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    t               [3] IMPLICIT TimeStamp,\n"
                    + "    stNum           [4] IMPLICIT Int32U,\n"
                    + "    sqNum           [5] IMPLICIT Int32U,\n"
                    + "    simulation      [6] IMPLICIT Boolean,\n"
                    + "    confRev         [7] IMPLICIT Int32U,\n"
                    + "    ndsCom          [8] IMPLICIT Boolean,\n"
                    + "    data            [9] IMPLICIT SEQUENCE OF Data\n"
                    + "}"),
    GET_GO_REFERENCE("get-go-ref", "8.9.2", 0x00, "Get Go Reference", "读GOOSE引用",
            "Retrieve the data references mapped to GOOSE members",
            "GetGoReference-RequestPDU ::= SEQUENCE {\n"
                    + "    gocbReference   [0] IMPLICIT ObjectReference,\n"
                    + "    memberOfs       [1] IMPLICIT SEQUENCE OF Int16U\n"
                    + "}\n"
                    + "\n"
                    + "GetGoReference-ResponsePDU ::= SEQUENCE {\n"
                    + "    gocbReference   [0] IMPLICIT ObjectReference,\n"
                    + "    confRev         [1] IMPLICIT Int32U,\n"
                    + "    datSet          [2] IMPLICIT ObjectReference,\n"
                    + "    memberData      [3] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        fc          [1] IMPLICIT FunctionalConstraint\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "GetGoReference-ErrorPDU ::= ServiceError"),
    GET_GOOSE_ELEMENT_NUMBER("get-goose-elem", "8.9.3", 0x00, "Get GOOSE Element Number", "读GOOSE元素编号",
            "Retrieve GOOSE element numbers for data references",
            "GetGOOSEElementNumber-RequestPDU ::= SEQUENCE {\n"
                    + "    gocbReference   [0] IMPLICIT ObjectReference,\n"
                    + "    memberData      [1] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        fc          [1] IMPLICIT FunctionalConstraint\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "GetGOOSEElementNumber-ResponsePDU ::= SEQUENCE {\n"
                    + "    gocbReference   [0] IMPLICIT ObjectReference,\n"
                    + "    confRev         [1] IMPLICIT Int32U,\n"
                    + "    datSet          [2] IMPLICIT ObjectReference,\n"
                    + "    memberOffset    [3] IMPLICIT SEQUENCE OF Int16U\n"
                    + "}\n"
                    + "\n"
                    + "GetGOOSEElementNumber-ErrorPDU ::= ServiceError"),
    GET_GOCB_VALUES("get-gocb-values", "8.9.4", 0x66, "Get GoCB Values", "读GOOSE控制块",
            "Retrieve GoCB (GOOSE Control Block) values",
            "GetGoCbValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetGoCbValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    gocb            [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT GoCB\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetGoCbValues-ErrorPDU ::= ServiceError"),
    SET_GOCB_VALUES("set-gocb-values", "8.9.5", 0x67, "Set GoCB Values", "写GOOSE控制块",
            "Modify GoCB (GOOSE Control Block) attributes",
            "SetGoCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    gocb            [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        goEna       [1] IMPLICIT Boolean OPTIONAL,\n"
                    + "        goID        [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ObjectReference OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetGoCBValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetGoCBValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        error       [0] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        goEna       [1] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        goID        [2] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ServiceError OPTIONAL\n"
                    + "    }\n"
                    + "}"),

    // ==================== 8.10 Sampled Value services ====================
    // Unconfirmed service SendMSVMessage has no code in standard Table 1; 0x00 placeholder
    SEND_MSV_MESSAGE("send-msv", "8.10.1", 0x00, "Send MSV Message", "发送采样值报文",
            "Unconfirmed multicast sampled value message from server",
            "SendMSVMessage-PDU ::= SEQUENCE {\n"
                    + "    msvID           [0] IMPLICIT VisibleString (SIZE (0..129)),\n"
                    + "    datSet          [1] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "    smpCnt          [2] IMPLICIT Int16U,\n"
                    + "    confRev         [3] IMPLICIT Int32U,\n"
                    + "    refTm           [4] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    smpSynch        [5] IMPLICIT Int8U,\n"
                    + "    smpRate         [6] IMPLICIT Int16U OPTIONAL,\n"
                    + "    simulation      [7] IMPLICIT Boolean,\n"
                    + "    sample          [8] IMPLICIT SEQUENCE OF Data,\n"
                    + "    smpMod          [9] IMPLICIT SmpMod OPTIONAL\n"
                    + "}"),
    GET_MSVCB_VALUES("get-msvcb-values", "8.10.2", 0x69, "Get MSVCB Values", "读采样值控制块",
            "Retrieve MSVCB (Multicast SV CB) values",
            "GetMSVCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "GetMSVCBValues-ResponsePDU ::= SEQUENCE {\n"
                    + "    msvcb           [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        value       [1] IMPLICIT MSVCB\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetMSVCBValues-ErrorPDU ::= ServiceError"),
SET_MSVCB_VALUES("set-msvcb-values", "8.10.3", 0x6A, "Set MSVCB Values", "写采样值控制块",
            "Modify MSVCB (Multicast SV CB) attributes",
            "SetMSVCBValues-RequestPDU ::= SEQUENCE {\n"
                    + "    msvcb           [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        reference   [0] IMPLICIT ObjectReference,\n"
                    + "        svEna       [1] IMPLICIT Boolean OPTIONAL,\n"
                    + "        msvID       [2] IMPLICIT VisibleString (SIZE (0..129)) OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ObjectReference OPTIONAL,\n"
                    + "        smpMod      [5] IMPLICIT SmpMod OPTIONAL,\n"
                    + "        smpRate     [6] IMPLICIT Int16U OPTIONAL,\n"
                    + "        optFlds     [7] IMPLICit MsvcbOptFlds OPTIONAL\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "SetMSVCBValues-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetMSVCBValues-ErrorPDU ::= SEQUENCE {\n"
                    + "    result          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        error       [0] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        svEna       [1] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        msvID       [2] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        datSet      [3] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        smpMod      [5] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        smpRate     [6] IMPLICIT ServiceError OPTIONAL,\n"
                    + "        optFlds     [7] IMPLICIT ServiceError OPTIONAL\n"
                    + "    }\n"
                    + "}"),

    // ==================== 8.11 Control services ====================
    SELECT("select", "8.11.1", 0x44, "Select", "选择",
            "Select a control object for subsequent operate",
            "Select-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "Select-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "Select-ErrorPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference\n"
                    + "}"),
    SELECT_WITH_VALUE("select-with-value", "8.11.2", 0x45, "Select With Value", "带值选择",
            "Select a control object with a control value",
            "SelectWithValue-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check\n"
                    + "}\n"
                    + "\n"
                    + "SelectWithValue-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check\n"
                    + "}\n"
                    + "\n"
                    + "SelectWithValue-ErrorPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check,\n"
                    + "    addCause        [8] IMPLICIT AddCause\n"
                    + "}"),
    OPERATE("operate", "8.11.3", 0x47, "Operate", "执行",
            "Execute a control operation",
            "Operate-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check\n"
                    + "}\n"
                    + "\n"
                    + "Operate-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference\n"
                    + "}\n"
                    + "\n"
                    + "Operate-ErrorPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check,\n"
                    + "    addCause        [8] IMPLICIT AddCause\n"
                    + "}"),
    CANCEL("cancel", "8.11.4", 0x46, "Cancel", "取消",
            "Cancel a pending control operation",
            "Cancel-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean\n"
                    + "}\n"
                    + "\n"
                    + "Cancel-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean\n"
                    + "}\n"
                    + "\n"
                    + "Cancel-ErrorPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    addCause        [8] IMPLICIT AddCause\n"
                    + "}"),
    COMMAND_TERMINATION("cmd-term", "8.11.5", 0x48, "Command Termination", "命令终止",
            "Notification of control command execution completion",
            "CommandTermination-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check,\n"
                    + "    addCause        [8] IMPLICIT AddCause OPTIONAL\n"
                    + "}"),
    TIME_ACTIVATED_OPERATE("time-act-ope", "8.11.6", 0x49, "Time Activated Operate", "定时执行",
            "Schedule a control operation at a specified time",
            "TimeActivatedOperate-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check\n"
                    + "}\n"
                    + "\n"
                    + "TimeActivatedOperate-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check\n"
                    + "}\n"
                    + "\n"
                    + "TimeActivatedOperate-ErrorPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check,\n"
                    + "    addCause        [8] IMPLICIT AddCause\n"
                    + "}"),
    TIME_ACTIVATED_OPERATE_TERMINATION("time-act-ope-term", "8.11.7", 0x4A, "Time Activated Operate Termination", "定时执行终止",
            "Termination of a time-activated control operation",
            "TimeActivatedOperateTermination-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT ObjectReference,\n"
                    + "    ctlVal          [1] IMPLICIT Data,\n"
                    + "    operTm          [2] IMPLICIT TimeStamp,\n"
                    + "    origin          [3] IMPLICIT Originator,\n"
                    + "    ctlNum          [4] IMPLICIT Int8U,\n"
                    + "    t               [5] IMPLICIT TimeStamp,\n"
                    + "    test            [6] IMPLICIT Boolean,\n"
                    + "    check           [7] IMPLICIT Check,\n"
                    + "    addCause        [8] IMPLICIT AddCause OPTIONAL\n"
                    + "}"),

    // ==================== 8.12 File services ====================
    GET_FILE("get-file", "8.12.1", 0x80, "Get File", "读文件",
            "Read a file from the server",
            "GetFile-RequestPDU ::= SEQUENCE {\n"
                    + "    filename        [0] IMPLICIT VisibleString (SIZE (0..255)),\n"
                    + "    startPosition   [1] IMPLICIT Int32U\n"
                    + "}\n"
                    + "\n"
                    + "GetFile-ResponsePDU ::= SEQUENCE {\n"
                    + "    fileData        [0] IMPLICIT OCTET STRING,\n"
                    + "    endOfFile       [1] IMPLICIT Boolean DEFAULT FALSE\n"
                    + "}\n"
                    + "\n"
                    + "GetFile-ErrorPDU ::= ServiceError"),
    SET_FILE("set-file", "8.12.2", 0x81, "Set File", "写文件",
            "Write a file to the server",
            "SetFile-RequestPDU ::= SEQUENCE {\n"
                    + "    filename        [0] IMPLICIT VisibleString (SIZE (0..255)),\n"
                    + "    startPosition   [1] IMPLICIT Int32U,\n"
                    + "    fileData        [2] IMPLICIT OCTET STRING,\n"
                    + "    endOfFile       [3] IMPLICIT Boolean DEFAULT 0\n"
                    + "}\n"
                    + "\n"
                    + "SetFile-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "SetFile-ErrorPDU ::= ServiceError"),
    DELETE_FILE("delete-file", "8.12.3", 0x82, "Delete File", "删除文件",
            "Delete a file on the server",
            "DeleteFile-RequestPDU ::= SEQUENCE {\n"
                    + "    filename        [0] IMPLICIT VisibleString (SIZE (0..255))\n"
                    + "}\n"
                    + "\n"
                    + "DeleteFile-ResponsePDU ::= NULL\n"
                    + "\n"
                    + "DeleteFile-ErrorPDU ::= ServiceError"),
    GET_FILE_ATTRIBUTE_VALUES("get-file-attrs", "8.12.4", 0x83, "Get File Attribute Values", "读文件属性",
            "Retrieve file attributes (FileEntry)",
            "GetFileAttributeValues-RequestPDU ::= SEQUENCE {\n"
                    + "    filename        [0] IMPLICIT VisibleString (SIZE (0..255))\n"
                    + "}\n"
                    + "\n"
                    + "GetFileAttributeValues-ResponsePDU ::= FileEntry\n"
                    + "\n"
                    + "GetFileAttributeValues-ErrorPDU ::= ServiceError"),
    GET_FILE_DIRECTORY("get-file-dir", "8.12.5", 0x84, "Get File Directory", "读文件目录",
            "List files in a directory",
            "GetFileDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    pathName        [0] IMPLICIT VisibleString (SIZE (0..255)),\n"
                    + "    startTime       [1] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    stopTime        [2] IMPLICIT TimeStamp OPTIONAL,\n"
                    + "    fileAfter       [3] IMPLICIT VisibleString (SIZE (0..255)) OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetFileDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    fileEntry       [0] IMPLICIT SEQUENCE OF FileEntry,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetFileDirectory-ErrorPDU ::= ServiceError"),

    // ==================== 8.13 RPC services ====================
    GET_RPC_INTERFACE_DIRECTORY("rpc-if-dir", "8.13.2", 0x6E, "Get RPC Interface Directory", "读RPC接口目录",
            "List available RPC interfaces",
            "GetRpcInterfaceDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    referenceAfter  [0] IMPLICIT VisibleString OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcInterfaceDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF VisibleString,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcInterfaceDirectory-ErrorPDU ::= ServiceError"),
    GET_RPC_METHOD_DIRECTORY("rpc-method-dir", "8.13.3", 0x6F, "Get RPC Method Directory", "读RPC方法目录",
            "List RPC methods for an interface",
            "GetRpcMethodDirectory-RequestPDU ::= SEQUENCE {\n"
                    + "    interface       [0] IMPLICIT VisibleString OPTIONAL,\n"
                    + "    referenceAfter  [1] IMPLICIT VisibleString OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcMethodDirectory-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF VisibleString,\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcMethodDirectory-ErrorPDU ::= ServiceError"),
    GET_RPC_INTERFACE_DEFINITION("rpc-if-def", "8.13.4", 0x70, "Get RPC Interface Definition", "读RPC接口定义",
            "Retrieve detailed RPC interface definition",
            "GetRpcInterfaceDefinition-RequestPDU ::= SEQUENCE {\n"
                    + "    interface       [0] IMPLICIT VisibleString,\n"
                    + "    referenceAfter  [1] IMPLICIT VisibleString OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcInterfaceDefinition-ResponsePDU ::= SEQUENCE {\n"
                    + "    method          [0] IMPLICIT SEQUENCE OF SEQUENCE {\n"
                    + "        name        [0] IMPLICIT VisibleString,\n"
                    + "        version     [1] IMPLICIT Int32U,\n"
                    + "        timeout     [2] IMPLICIT Int32U,\n"
                    + "        request     [3] IMPLICIT DataDefinition,\n"
                    + "        response    [4] IMPLICIT DataDefinition\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcInterfaceDefinition-ErrorPDU ::= ServiceError"),
    GET_RPC_METHOD_DEFINITION("rpc-method-def", "8.13.5", 0x71, "Get RPC Method Definition", "读RPC方法定义",
            "Retrieve detailed RPC method definition",
            "GetRpcMethodDefinition-RequestPDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF VisibleString\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcMethodDefinition-ResponsePDU ::= SEQUENCE {\n"
                    + "    reference       [0] IMPLICIT SEQUENCE OF CHOICE {\n"
                    + "        error       [0] IMPLICIT ServiceError,\n"
                    + "        method      [1] IMPLICIT SEQUENCE {\n"
                    + "            version     [0] IMPLICIT Int32U,\n"
                    + "            timeout     [1] IMPLICIT Int32U,\n"
                    + "            request     [2] IMPLICIT DataDefinition,\n"
                    + "            response    [3] IMPLICIT DataDefinition\n"
                    + "        }\n"
                    + "    },\n"
                    + "    moreFollows     [1] IMPLICIT BOOLEAN DEFAULT TRUE\n"
                    + "}\n"
                    + "\n"
                    + "GetRpcMethodDefinition-ErrorPDU ::= ServiceError"),
    RPC_CALL("rpc-call", "8.13.6", 0x72, "RPC Call", "RPC调用",
            "Execute an RPC call",
            "RpcCall-RequestPDU ::= SEQUENCE {\n"
                    + "    method          [0] IMPLICIT VisibleString,\n"
                    + "    req             [1] IMPLICIT CHOICE {\n"
                    + "        reqData     [0] IMPLICIT Data,\n"
                    + "        callID      [1] IMPLICIT OCTET STRING\n"
                    + "    }\n"
                    + "}\n"
                    + "\n"
                    + "RpcCall-ResponsePDU ::= SEQUENCE {\n"
                    + "    rspData         [0] IMPLICIT Data,\n"
                    + "    nextCallID      [1] IMPLICIT OCTET STRING OPTIONAL\n"
                    + "}\n"
                    + "\n"
                    + "RpcCall-ErrorPDU ::= ServiceError"),

    // ==================== 8.14 Test service ====================
    TEST("test", "8.14", 0x99, "Test", "测试", "Test connection — no fields", "");

    private static final Map<String, CmsServiceInfo> BY_NAME = new HashMap<>();
    private static final Map<Integer, CmsServiceInfo> BY_CODE = new HashMap<>();

    static {
        for (CmsServiceInfo s : values()) {
            BY_NAME.put(s.cliName, s);
            if (s.serviceCode == 0) continue; // no code in standard Table 1 (unconfirmed services); skip by-code lookup
            CmsServiceInfo prev = BY_CODE.put(s.serviceCode, s);
            if (prev != null) {
                throw new IllegalStateException("Duplicate service code 0x"
                        + Integer.toHexString(s.serviceCode) + ": " + prev.name() + " vs " + s.name());
            }
        }
    }

    private final String cliName;
    private final String section;
    private final int serviceCode;
    private final String enName;
    private final String cnName;
    private final String description;
    private final String asn1;

    CmsServiceInfo(String cliName, String section, int serviceCode, String enName, String cnName, String description, String asn1) {
        this.cliName = cliName;
        this.section = section;
        this.serviceCode = serviceCode;
        this.enName = enName;
        this.cnName = cnName;
        this.description = description;
        this.asn1 = asn1;
    }

    public static CmsServiceInfo byName(String cliName) {
        return BY_NAME.get(cliName);
    }

    public static CmsServiceInfo byCode(int code) {
        return BY_CODE.get(code);
    }
}
