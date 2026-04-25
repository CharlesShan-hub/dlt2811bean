package com.ysh.dlt2811bean.service.enums;

/**
 * Service Code enumeration for DL/T 2811 (GB/T 45906.3-2025) communication protocol.
 *
 * <p>This enumeration defines all service codes specified in Table 1 of the standard,
 * which correspond to various communication services between clients and servers
 * in substation secondary systems.</p>
 *
 * <h2>Service Categories</h2>
 * <p>The services are organized into the following functional categories:</p>
 * <ul>
 *   <li><b>Association Services</b> (0x01-0x03) - Connection establishment and teardown</li>
 *   <li><b>Model and Data Services</b> (0x30-0x9C) - Reading and writing data and metadata</li>
 *   <li><b>Data Set Services</b> (0x36-0x3B) - Data set creation, deletion, and manipulation</li>
 *   <li><b>Control Services</b> (0x44-0x4A) - Device control operations</li>
 *   <li><b>Setting Group Services</b> (0x54-0x59) - Setting group management and editing</li>
 *   <li><b>Report Services</b> (0x5A-0x60) - Report configuration and data reporting</li>
 *   <li><b>Log Services</b> (0x61-0x63) - Log querying and management</li>
 *   <li><b>GOOSE Control Block Services</b> (0x66-0x67) - GOOSE message configuration</li>
 *   <li><b>MSV Control Block Services</b> (0x69-0x6A) - Multicast Sampled Value configuration</li>
 *   <li><b>File Services</b> (0x80-0x84) - File transfer and management</li>
 *   <li><b>Remote Procedure Call Services</b> (0x6E-0x72) - Dynamic method invocation</li>
 *   <li><b>Other Services</b> (0x99-0x9A) - Testing and connection negotiation</li>
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Creating a Service Object</h3>
 * <pre>
 * {@code
 * // Create a GetDataValues request
 * ServiceCode getDataValues = ServiceCode.GET_DATA_VALUES;
 * System.out.println(getDataValues.getInterfaceName());  // "GetDataValues"
 * System.out.println(getDataValues.getDecimalValue());   // 48
 * System.out.println(String.format("0x%02X", getDataValues.getCode()));  // "0x30"
 * }
 * </pre>
 *
 * <h3>Decoding a Received Service Code</h3>
 * <pre>
 * {@code
 * // When receiving a message from the network
 * byte receivedServiceCode = 0x30;  // Received from network packet
 * ServiceCode code = ServiceCode.fromByte(receivedServiceCode);
 *
 * if (code != null) {
 *     switch (code) {
 *         case GET_DATA_VALUES:
 *             // Process GetDataValues request
 *             break;
 *         case SET_DATA_VALUES:
 *             // Process SetDataValues request
 *             break;
 *         // ... handle other services
 *     }
 * } else {
 *     // Handle unknown service code
 *     throw new IllegalArgumentException("Unknown service code: 0x" +
 *             String.format("%02X", receivedServiceCode));
 * }
 * }
 * </pre>
 *
 * <h3>Validating Service Codes</h3>
 * <pre>
 * {@code
 * // Check if a service code is valid
 * byte testCode = 0x30;
 * if (ServiceCode.fromByte(testCode) != null) {
 *     System.out.println("Valid service code");
 * } else {
 *     System.out.println("Invalid service code");
 * }
 * }
 * </pre>
 *
 * <h2>Protocol Details</h2>
 * <p>Each service code corresponds to a specific function in the DL/T 2811 protocol stack:</p>
 * <ul>
 *   <li><b>Byte Code</b> - The actual 8-bit value used in APDU headers (see section 6.1.2)</li>
 *   <li><b>Decimal Value</b> - The numeric representation of the service code for human reference</li>
 *   <li><b>Interface Name</b> - The service interface name as defined in the standard</li>
 * </ul>
 *
 * <p>Service codes are transmitted in the Application Protocol Control Header (APCH)
 * of the Application Protocol Data Unit (APDU), specifically in the Service Code (SC) field.</p>
 *
 * <h2>Notes</h2>
 * <ul>
 *   <li>All service codes are defined in the range 0x00-0xFF (0-255)</li>
 *   <li>Service code 0x00 is reserved and not used</li>
 *   <li>Some codes may be reserved for future use as per the standard</li>
 *   <li>When implementing new protocol versions, check for service code additions or changes</li>
 * </ul>
 *
 * @see <a href="GB/T 45906.3-2025">DL/T 2811 Communication Message Specification</a>
 *
 * @since 1.0
 */
public enum ServiceCode {

    // ==================== 关联服务 ====================
    ASSOCIATE((byte)0x01, 1, "Associate"),
    ABORT((byte)0x02, 2, "Abort"),
    RELEASE((byte)0x03, 3, "Release"),

    // ==================== 模型和数据服务 ====================
    GET_SERVER_DIRECTORY((byte)0x50, 80, "GetServerDirectory"),
    GET_LOGIC_DEVICE_DIRECTORY((byte)0x51, 81, "GetLogicDeviceDirectory"),
    GET_LOGIC_NODE_DIRECTORY((byte)0x52, 82, "GetLogicNodeDirectory"),
    GET_ALL_DATA_VALUES((byte)0x53, 83, "GetAllDataValues"),
    GET_ALL_DATA_DEFINITION((byte)0x9B, 155, "GetAllDataDefinition"),
    GET_ALL_CB_VALUES((byte)0x9C, 156, "GetAllCBValues"),
    GET_DATA_VALUES((byte)0x30, 48, "GetDataValues"),
    SET_DATA_VALUES((byte)0x31, 49, "SetDataValues"),
    GET_DATA_DIRECTORY((byte)0x32, 50, "GetDataDirectory"),
    GET_DATA_DEFINITION((byte)0x33, 51, "GetDataDefinition"),

    // ==================== 数据集服务 ====================
    CREATE_DATA_SET((byte)0x36, 54, "CreateDataSet"),
    DELETE_DATA_SET((byte)0x37, 55, "DeleteDataSet"),
    GET_DATA_SET_DIRECTORY((byte)0x39, 57, "GetDataSetDirectory"),
    GET_DATA_SET_VALUES((byte)0x3A, 58, "GetDataSetValues"),
    SET_DATA_SET_VALUES((byte)0x3B, 59, "SetDataSetValues"),

    // ==================== 控制服务 ====================
    SELECT((byte)0x44, 68, "Select"),
    SELECT_WITH_VALUE((byte)0x45, 69, "SelectWithValue"),
    CANCEL((byte)0x46, 70, "Cancel"),
    OPERATE((byte)0x47, 71, "Operate"),
    COMMAND_TERMINATION((byte)0x48, 72, "CommandTermination"),
    TIME_ACTIVATED_OPERATE((byte)0x49, 73, "TimeActivatedOperate"),
    TIME_ACTIVATED_OPERATE_TERMINATION((byte)0x4A, 74, "TimeActivatedOperateTermination"),

    // ==================== 定值组服务 ====================
    SELECT_ACTIVE_SG((byte)0x54, 84, "SelectActiveSG"),
    SELECT_EDIT_SG((byte)0x55, 85, "SelectEditSG"),
    SET_EDIT_SG_VALUE((byte)0x56, 86, "SetEditSGValue"),
    CONFIRM_EDIT_SG_VALUES((byte)0x57, 87, "ConfirmEditSGValues"),
    GET_EDIT_SG_VALUE((byte)0x58, 88, "GetEditSGValue"),
    GET_SGCBVALUES((byte)0x59, 89, "GetSGCBValues"),

    // ==================== 报告服务 ====================
    REPORT((byte)0x5A, 90, "Report"),
    GET_BRCBVALUES((byte)0x5B, 91, "GetBRCBValues"),
    SET_BRCBVALUES((byte)0x5C, 92, "SetBRCBValues"),
    GET_URCBVALUES((byte)0x5D, 93, "GetURCBValues"),
    SET_URCBVALUES((byte)0x5E, 94, "SetURCBValues"),
    GET_LCBVALUES((byte)0x5F, 95, "GetLCBValues"),
    SET_LCBVALUES((byte)0x60, 96, "SetLCBValues"),

    // ==================== 日志服务 ====================
    QUERY_LOG_BY_TIME((byte)0x61, 97, "QueryLogByTime"),
    QUERY_LOG_AFTER((byte)0x62, 98, "QueryLogAfter"),
    GET_LOG_STATUS_VALUES((byte)0x63, 99, "GetLogStatusValues"),

    // ==================== GOOSE 控制块服务 ====================
    GET_GOCBVALUES((byte)0x66, 102, "GetGoCBValues"),
    SET_GOCBVALUES((byte)0x67, 103, "SetGoCBValues"),

    // ==================== MSV 控制块服务 ====================
    GET_MSVCBVALUES((byte)0x69, 105, "GetMSVCBValues"),
    SET_MSVCBVALUES((byte)0x6A, 106, "SetMSVCBValues"),

    // ==================== 文件服务 ====================
    GET_FILE((byte)0x80, 128, "GetFile"),
    SET_FILE((byte)0x81, 129, "SetFile"),
    DELETE_FILE((byte)0x82, 130, "DeleteFile"),
    GET_FILE_ATTRIBUTEVALUES((byte)0x83, 131, "GetFileAttributeValues"),
    GET_FILE_DIRECTORY((byte)0x84, 132, "GetFileDirectory"),

    // ==================== 远程过程调用 ====================
    GET_RPC_INTERFACE_DIRECTORY((byte)0x6E, 110, "GetRpcInterfaceDirectory"),
    GET_RPC_METHOD_DIRECTORY((byte)0x6F, 111, "GetRpcMethodDirectory"),
    GET_RPC_INTERFACE_DEFINITION((byte)0x70, 112, "GetRpcInterfaceDefinition"),
    GET_RPC_METHOD_DEFINITION((byte)0x71, 113, "GetRpcMethodDefinition"),
    RPC_CALL((byte)0x72, 114, "RpcCall"),

    // ==================== 其他服务 ====================
    TEST((byte)0x99, 153, "Test"),
    ASSOCIATE_NEGOTIATE((byte)0x9A, 154, "AssociateNegotiate");

    private final byte code;
    private final int decimalValue;
    private final String interfaceName;

    ServiceCode(byte code, int decimalValue, String interfaceName) {
        this.code = code;
        this.decimalValue = decimalValue;
        this.interfaceName = interfaceName;
    }

    public byte getCode() {
        return code;
    }

    public int getDecimalValue() {
        return decimalValue;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public static ServiceCode fromByte(byte code) {
        for (ServiceCode sc : values()) {
            if (sc.getCode() == code) {
                return sc;
            }
        }
        return null;
    }
}