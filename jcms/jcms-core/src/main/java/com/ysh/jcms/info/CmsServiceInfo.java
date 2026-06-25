package com.ysh.jcms.info;

import java.util.HashMap;
import java.util.Map;

/**
 * DL/T 2811 service definitions according to cms.asn1.
 * Each service has a name, section reference, service code (SC),
 * and corresponding PDU types.
 */
public enum CmsServiceInfo {

    // ==================== 8.2 Association services ====================
    ASSOCIATE(
            "associate",
            "8.2.1",
            0x01,
            "Associate",
            "建立应用层关联",
            "Establish an application-layer association between client and server"
    ),
    RELEASE(
            "release",
            "8.2.2",
            0x02,
            "Release",
            "释放应用层关联",
            "Release an established application-layer association"
    ),
    ABORT(
            "abort",
            "8.2.3",
            0x03,
            "Abort",
            "中止关联",
            "Abort an application-layer association"
    ),
    ASSOCIATE_NEGOTIATE(
            "negotiate",
            "8.15",
            0x04,
            "Associate Negotiate",
            "协商关联参数",
            "Negotiate association parameters (apduSize, asduSize, protocolVersion)"
    ),

    // ==================== 8.3 Directory services ====================
    GET_SERVER_DIRECTORY(
            "server-dir",
            "8.3.1",
            0x50,
            "Get Server Directory",
            "读服务器目录",
            "Retrieve the logical device directory from the server"
    ),
    GET_LOGICAL_DEVICE_DIRECTORY(
            "ld-dir",
            "8.3.2",
            0x51,
            "Get Logical Device Directory",
            "读逻辑设备目录",
            "Retrieve logical node references under a logical device"
    ),
    GET_LOGICAL_NODE_DIRECTORY(
            "ln-dir",
            "8.3.3",
            0x52,
            "Get Logical Node Directory",
            "读逻辑节点目录",
            "Retrieve data references under a logical node"
    ),
    GET_ALL_DATA_VALUES(
            "get-all-values",
            "8.3.4",
            0x53,
            "Get All Data Values",
            "读所有数据值",
            "Retrieve all data values for a logical device or node"
    ),
    GET_ALL_DATA_DEFINITION(
            "get-all-def",
            "8.3.5",
            0x9B,
            "Get All Data Definition",
            "读所有数据定义",
            "Retrieve all data type definitions for a logical device or node"
    ),
    GET_ALL_CB_VALUES(
            "get-all-cb",
            "8.3.6",
            0x9C,
            "Get All CB Values",
            "读所有控制块",
            "Retrieve all control block values for a logical device or node"
    ),

    // ==================== 8.4 Data access services ====================
    GET_DATA_VALUES(
            "get-data-values",
            "8.4.1",
            0x30,
            "Get Data Values",
            "读数据值",
            "Read values of specified data references"
    ),
    SET_DATA_VALUES(
            "set-data-values",
            "8.4.2",
            0x31,
            "Set Data Values",
            "写数据值",
            "Write values to specified data references"
    ),
    GET_DATA_DIRECTORY(
            "get-data-dir",
            "8.4.3",
            0x32,
            "Get Data Directory",
            "读数据目录",
            "Retrieve child data references under a data reference"
    ),
    GET_DATA_DEFINITION(
            "get-data-def",
            "8.4.4",
            0x33,
            "Get Data Definition",
            "读数据定义",
            "Retrieve type definitions for specified data references"
    ),

    // ==================== 8.5 Data set services ====================
    GET_DATA_SET_VALUES(
            "get-dataset-values",
            "8.5.1",
            0x44,
            "Get Data Set Values",
            "读数据集值",
            "Read the values of all members in a data set"
    ),
    SET_DATA_SET_VALUES(
            "set-dataset-values",
            "8.5.2",
            0x45,
            "Set Data Set Values",
            "写数据集值",
            "Write values to members of a data set"
    ),
    CREATE_DATA_SET(
            "create-dataset",
            "8.5.3",
            0x41,
            "Create Data Set",
            "创建数据集",
            "Dynamically create a new data set"
    ),
    DELETE_DATA_SET(
            "delete-dataset",
            "8.5.4",
            0x42,
            "Delete Data Set",
            "删除数据集",
            "Delete a previously created data set"
    ),
    GET_DATA_SET_DIRECTORY(
            "get-dataset-dir",
            "8.5.5",
            0x43,
            "Get Data Set Directory",
            "读数据集目录",
            "Retrieve member data references of a data set"
    ),

    // ==================== 8.6 Setting group services ====================
    SELECT_ACTIVE_SG(
            "select-active-sg",
            "8.6.1",
            0x62,
            "Select Active SG",
            "选择激活定值组",
            "Select the active setting group"
    ),
    SELECT_EDIT_SG(
            "select-edit-sg",
            "8.6.2",
            0x63,
            "Select Edit SG",
            "选择编辑定值组",
            "Select the edit setting group"
    ),
    SET_EDIT_SG_VALUE(
            "set-edit-sg-value",
            "8.6.3",
            0x66,
            "Set Edit SG Value",
            "设置编辑定值值",
            "Set values in the edit buffer of a setting group"
    ),
    CONFIRM_EDIT_SG_VALUES(
            "confirm-edit-sg",
            "8.6.4",
            0x64,
            "Confirm Edit SG Values",
            "确认编辑定值",
            "Commit the edit buffer values to the setting group"
    ),
    GET_EDIT_SG_VALUE(
            "get-edit-sg-value",
            "8.6.5",
            0x65,
            "Get Edit SG Value",
            "读编辑定值值",
            "Read values from the edit buffer of a setting group"
    ),
    GET_SGCB_VALUES(
            "get-sgcb-values",
            "8.6.6",
            0x61,
            "Get SGCB Values",
            "读定值组控制块",
            "Retrieve SGCB (Setting Group Control Block) values"
    ),

    // ==================== 8.7 Reporting services ====================
    REPORT(
            "report",
            "8.7.1",
            0x35,
            "Report",
            "推送报告",
            "Unconfirmed report notification from server to client"
    ),
    GET_BRCB_VALUES(
            "get-brcb-values",
            "8.7.2",
            0x31,
            "Get BRCB Values",
            "读缓存报告控制块",
            "Retrieve BRCB (Buffered Report CB) values"
    ),
    SET_BRCB_VALUES(
            "set-brcb-values",
            "8.7.3",
            0x32,
            "Set BRCB Values",
            "写缓存报告控制块",
            "Modify BRCB (Buffered Report CB) attributes"
    ),
    GET_URCB_VALUES(
            "get-urcb-values",
            "8.7.4",
            0x33,
            "Get URCB Values",
            "读非缓存报告控制块",
            "Retrieve URCB (Unbuffered Report CB) values"
    ),
    SET_URCB_VALUES(
            "set-urcb-values",
            "8.7.5",
            0x34,
            "Set URCB Values",
            "写非缓存报告控制块",
            "Modify URCB (Unbuffered Report CB) attributes"
    ),

    // ==================== 8.8 Logging services ====================
    GET_LCB_VALUES(
            "get-lcb-values",
            "8.8.2",
            0x67,
            "Get LCB Values",
            "读日志控制块",
            "Retrieve LCB (Log Control Block) values"
    ),
    SET_LCB_VALUES(
            "set-lcb-values",
            "8.8.3",
            0x68,
            "Set LCB Values",
            "写日志控制块",
            "Modify LCB (Log Control Block) attributes"
    ),
    QUERY_LOG_BY_TIME(
            "query-log-time",
            "8.8.4",
            0x6B,
            "Query Log By Time",
            "按时间查询日志",
            "Query log entries within a time range"
    ),
    QUERY_LOG_AFTER(
            "query-log-after",
            "8.8.5",
            0x6A,
            "Query Log After",
            "按条目查询日志",
            "Query log entries after a given entry"
    ),
    GET_LOG_STATUS_VALUES(
            "get-log-status",
            "8.8.6",
            0x69,
            "Get Log Status Values",
            "读日志状态值",
            "Retrieve log status information (oldest/newest entries)"
    ),

    // ==================== 8.9 GOOSE services ====================
    SEND_GOOSE_MESSAGE(
            "send-goose",
            "8.9.1",
            0x84,
            "Send GOOSE Message",
            "发送GOOSE报文",
            "Unconfirmed GOOSE message from server"
    ),
    GET_GO_REFERENCE(
            "get-go-ref",
            "8.9.2",
            0x81,
            "Get Go Reference",
            "读GOOSE引用",
            "Retrieve the data references mapped to GOOSE members"
    ),
    GET_GOOSE_ELEMENT_NUMBER(
            "get-goose-elem",
            "8.9.3",
            0x85,
            "Get GOOSE Element Number",
            "读GOOSE元素编号",
            "Retrieve GOOSE element numbers for data references"
    ),
    GET_GO_CB_VALUES(
            "get-gocb-values",
            "8.9.4",
            0x82,
            "Get GoCB Values",
            "读GOOSE控制块",
            "Retrieve GoCB (GOOSE Control Block) values"
    ),
    SET_GO_CB_VALUES(
            "set-gocb-values",
            "8.9.5",
            0x83,
            "Set GoCB Values",
            "写GOOSE控制块",
            "Modify GoCB (GOOSE Control Block) attributes"
    ),

    // ==================== 8.10 Sampled Value services ====================
    SEND_MSV_MESSAGE(
            "send-msv",
            "8.10.1",
            0x88,
            "Send MSV Message",
            "发送采样值报文",
            "Unconfirmed multicast sampled value message from server"
    ),
    GET_MSVCB_VALUES(
            "get-msvcb-values",
            "8.10.2",
            0x86,
            "Get MSVCB Values",
            "读采样值控制块",
            "Retrieve MSVCB (Multicast SV CB) values"
    ),
    SET_MSVCB_VALUES(
            "set-msvcb-values",
            "8.10.3",
            0x87,
            "Set MSVCB Values",
            "写采样值控制块",
            "Modify MSVCB (Multicast SV CB) attributes"
    ),

    // ==================== 8.11 Control services ====================
    SELECT(
            "select",
            "8.11.1",
            0x21,
            "Select",
            "选择",
            "Select a control object for subsequent operate"
    ),
    SELECT_WITH_VALUE(
            "select-with-value",
            "8.11.2",
            0x22,
            "Select With Value",
            "带值选择",
            "Select a control object with a control value"
    ),
    OPERATE(
            "operate",
            "8.11.3",
            0x23,
            "Operate",
            "执行",
            "Execute a control operation"
    ),
    CANCEL(
            "cancel",
            "8.11.4",
            0x24,
            "Cancel",
            "取消",
            "Cancel a pending control operation"
    ),
    COMMAND_TERMINATION(
            "cmd-term",
            "8.11.5",
            0x26,
            "Command Termination",
            "命令终止",
            "Notification of control command execution completion"
    ),
    TIME_ACTIVATED_OPERATE(
            "time-act-ope",
            "8.11.6",
            0x25,
            "Time Activated Operate",
            "定时执行",
            "Schedule a control operation at a specified time"
    ),
    TIME_ACTIVATED_OPERATE_TERMINATION(
            "time-act-ope-term",
            "8.11.7",
            0x27,
            "Time Activated Operate Termination",
            "定时执行终止",
            "Termination of a time-activated control operation"
    ),

    // ==================== 8.12 File services ====================
    GET_FILE(
            "get-file",
            "8.12.1",
            0x71,
            "Get File",
            "读文件",
            "Read a file from the server"
    ),
    SET_FILE(
            "set-file",
            "8.12.2",
            0x72,
            "Set File",
            "写文件",
            "Write a file to the server"
    ),
    DELETE_FILE(
            "delete-file",
            "8.12.3",
            0x73,
            "Delete File",
            "删除文件",
            "Delete a file on the server"
    ),
    GET_FILE_ATTRIBUTE_VALUES(
            "get-file-attrs",
            "8.12.4",
            0x75,
            "Get File Attribute Values",
            "读文件属性",
            "Retrieve file attributes (FileEntry)"
    ),
    GET_FILE_DIRECTORY(
            "get-file-dir",
            "8.12.5",
            0x74,
            "Get File Directory",
            "读文件目录",
            "List files in a directory"
    ),

    // ==================== 8.13 RPC services ====================
    GET_RPC_INTERFACE_DIRECTORY(
            "rpc-if-dir",
            "8.13.2",
            0x91,
            "Get RPC Interface Directory",
            "读RPC接口目录",
            "List available RPC interfaces"
    ),
    GET_RPC_METHOD_DIRECTORY(
            "rpc-method-dir",
            "8.13.3",
            0x93,
            "Get RPC Method Directory",
            "读RPC方法目录",
            "List RPC methods for an interface"
    ),
    GET_RPC_INTERFACE_DEFINITION(
            "rpc-if-def",
            "8.13.4",
            0x92,
            "Get RPC Interface Definition",
            "读RPC接口定义",
            "Retrieve detailed RPC interface definition"
    ),
    GET_RPC_METHOD_DEFINITION(
            "rpc-method-def",
            "8.13.5",
            0x94,
            "Get RPC Method Definition",
            "读RPC方法定义",
            "Retrieve detailed RPC method definition"
    ),
    RPC_CALL(
            "rpc-call",
            "8.13.6",
            0x95,
            "RPC Call",
            "RPC调用",
            "Execute an RPC call"
    ),

    // ==================== 8.14 Test service ====================
    TEST(
            "test",
            "8.14",
            0xA1,
            "Test",
            "测试",
            "Test connection — no fields"
    );

    private static final Map<String, CmsServiceInfo> BY_NAME = new HashMap<>();
    private static final Map<Integer, CmsServiceInfo> BY_CODE = new HashMap<>();

    static {
        for (CmsServiceInfo s : values()) {
            BY_NAME.put(s.cliName, s);
            BY_CODE.put(s.serviceCode, s);
        }
    }

    private final String cliName;
    private final String section;
    private final int serviceCode;
    private final String enName;
    private final String cnName;
    private final String description;

    CmsServiceInfo(String cliName, String section, int serviceCode,
                   String enName, String cnName, String description) {
        this.cliName = cliName;
        this.section = section;
        this.serviceCode = serviceCode;
        this.enName = enName;
        this.cnName = cnName;
        this.description = description;
    }

    public String getCliName() { return cliName; }
    public String getSection() { return section; }
    public int getServiceCode() { return serviceCode; }
    public String getEnName() { return enName; }
    public String getCnName() { return cnName; }
    public String getDescription() { return description; }

    public static CmsServiceInfo byName(String cliName) {
        return BY_NAME.get(cliName);
    }

    public static CmsServiceInfo byCode(int code) {
        return BY_CODE.get(code);
    }
}
