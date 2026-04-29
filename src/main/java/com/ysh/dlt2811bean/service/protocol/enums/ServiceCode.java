package com.ysh.dlt2811bean.service.protocol.enums;

/**
 * Service Code enumeration for DL/T 2811 (GB/T 45906.3-2025) communication protocol.
 *
 * <p>This enumeration defines all service codes used in DL/T 2811 communication protocol
 * for substation secondary systems. Each service code corresponds to a specific
 * communication function between clients and servers.</p>
 *
 * <pre>
 * {@code
 * // Get service code as integer
 * ServiceCode sc = ServiceCode.GET_SERVER_DIRECTORY;
 * int codeValue = sc.getCode();  // Returns 0x50
 * String hexString = String.format("0x%02X", codeValue);  // "0x50"
 *
 * // Get service interface name
 * String interfaceName = sc.getInterfaceName();  // "GetServerDirectory"
 *
 * // Convert to byte for network transmission
 * byte networkByte = sc.getByteCode();  // (byte)0x50
 * }
 * </pre>
 * @since 1.0
 */
public enum ServiceCode {
    // ==================== 8.2 关联服务 ====================
    ASSOCIATE(0x01, "Associate"),
    ABORT(0x02, "Abort"),
    RELEASE(0x03, "Release"),

    // ==================== 8.3 服务器、逻辑设备、逻辑节点类服务 ====================
    GET_SERVER_DIRECTORY(0x50, "GetServerDirectory"),
    GET_LOGIC_DEVICE_DIRECTORY(0x51, "GetLogicDeviceDirectory"),
    GET_LOGIC_NODE_DIRECTORY(0x52, "GetLogicNodeDirectory"),
    GET_ALL_DATA_VALUES(0x53, "GetAllDataValues"),
    GET_ALL_DATA_DEFINITION(0x9B, "GetAllDataDefinition"),
    GET_ALL_CB_VALUES(0x9C, "GetAllCBValues"),

    // ==================== 8.4 数据类服务 ====================
    GET_DATA_VALUES(0x30, "GetDataValues"),
    SET_DATA_VALUES(0x31, "SetDataValues"),
    GET_DATA_DIRECTORY(0x32, "GetDataDirectory"),
    GET_DATA_DEFINITION(0x33, "GetDataDefinition"),

    // ==================== 8.5 数据集服务 ====================
    GET_DATA_SET_VALUES(0x3A, "GetDataSetValues"),
    SET_DATA_SET_VALUES(0x3B, "SetDataSetValues"),
    CREATE_DATA_SET(0x36, "CreateDataSet"),
    DELETE_DATA_SET(0x37, "DeleteDataSet"),
    GET_DATA_SET_DIRECTORY(0x39, "GetDataSetDirectory"),

    // ==================== 8.6 定值组服务 ====================
    SELECT_ACTIVE_SG(0x54, "SelectActiveSG"),
    SELECT_EDIT_SG(0x55, "SelectEditSG"),
    SET_EDIT_SG_VALUE(0x56, "SetEditSGValue"),
    CONFIRM_EDIT_SG_VALUES(0x57, "ConfirmEditSGValues"),
    GET_EDIT_SG_VALUE(0x58, "GetEditSGValue"),
    GET_SGCBVALUES(0x59, "GetSGCBValues"),

    // ==================== 8.7 报告服务 ====================
    REPORT(0x5A, "Report"),
    GET_BRCBVALUES(0x5B, "GetBRCBValues"),
    SET_BRCBVALUES(0x5C, "SetBRCBValues"),
    GET_URCBVALUES(0x5D, "GetURCBValues"),
    SET_URCBVALUES(0x5E, "SetURCBValues"),

    // ==================== 8.8 日志服务 ====================
    GET_LCBVALUES(0x5F, "GetLCBValues"),
    SET_LCBVALUES(0x60, "SetLCBValues"),
    QUERY_LOG_BY_TIME(0x61, "QueryLogByTime"),
    QUERY_LOG_AFTER(0x62, "QueryLogAfter"),
    GET_LOG_STATUS_VALUES(0x63, "GetLogStatusValues"),

    // ==================== 8.9 通用变电站事件类服务 ====================
    GET_GOCBVALUES(0x66, "GetGoCBValues"),
    SET_GOCBVALUES(0x67, "SetGoCBValues"),

    // ==================== 控制服务 ====================
    SELECT(0x44, "Select"),
    SELECT_WITH_VALUE(0x45, "SelectWithValue"),
    CANCEL(0x46, "Cancel"),
    OPERATE(0x47, "Operate"),
    COMMAND_TERMINATION(0x48, "CommandTermination"),
    TIME_ACTIVATED_OPERATE(0x49, "TimeActivatedOperate"),
    TIME_ACTIVATED_OPERATE_TERMINATION(0x4A, "TimeActivatedOperateTermination"),

    // ==================== MSV 控制块服务 ====================
    GET_MSVCBVALUES(0x69, "GetMSVCBValues"),
    SET_MSVCBVALUES(0x6A, "SetMSVCBValues"),

    // ==================== 文件服务 ====================
    GET_FILE(0x80, "GetFile"),
    SET_FILE(0x81, "SetFile"),
    DELETE_FILE(0x82, "DeleteFile"),
    GET_FILE_ATTRIBUTEVALUES(0x83, "GetFileAttributeValues"),
    GET_FILE_DIRECTORY(0x84, "GetFileDirectory"),

    // ==================== 远程过程调用 ====================
    GET_RPC_INTERFACE_DIRECTORY(0x6E, "GetRpcInterfaceDirectory"),
    GET_RPC_METHOD_DIRECTORY(0x6F, "GetRpcMethodDirectory"),
    GET_RPC_INTERFACE_DEFINITION(0x70, "GetRpcInterfaceDefinition"),
    GET_RPC_METHOD_DEFINITION(0x71, "GetRpcMethodDefinition"),
    RPC_CALL(0x72, "RpcCall"),

    // ==================== 其他服务 ====================
    TEST(0x99, "Test"),
    ASSOCIATE_NEGOTIATE(0x9A, "AssociateNegotiate");

    private final int code;
    private final String interfaceName;

    ServiceCode(int code, String interfaceName) {
        this.code = code;
        this.interfaceName = interfaceName;
    }

    public int getCode() {
        return code;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public byte getByteCode() {
        return (byte) code;
    }

    public static ServiceCode fromInt(int code) {
        for (ServiceCode sc : values()) {
            if (sc.code == code) {
                return sc;
            }
        }
        return null;
    }

    public static ServiceCode fromByte(byte code) {
        return fromInt(code & 0xFF);
    }
}