package com.ysh.dlt2811bean.service.svc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;
import com.ysh.dlt2811bean.service.svc.test.CmsTest;

public class AsduFactory {

    public static CmsAsdu<?> create(ServiceName serviceName, boolean isResp, boolean isErr) {
        switch (serviceName) {
            // 8.2 Association Services
            case ASSOCIATE: return new CmsAssociate(isResp, isErr);
            case ABORT: return new CmsAbort(isResp, isErr);
            case RELEASE: return new CmsRelease(isResp, isErr);

            // 8.3 Service, Login Device\node Services
            case GET_SERVER_DIRECTORY: return new CmsGetServerDirectory(isResp, isErr);
            case GET_LOGIC_DEVICE_DIRECTORY: return new CmsGetLogicalDeviceDirectory(isResp, isErr);
            case GET_LOGIC_NODE_DIRECTORY: return new CmsGetLogicalNodeDirectory(isResp, isErr);
            case GET_ALL_DATA_VALUES: return new CmsGetAllDataValues(isResp, isErr);
            // case GET_ALL_DATA_DEFINITION
            // case GET_ALL_CB_VALUES

            // 8.4 Data Services
            // case GET_DATA_VALUES
            // case SET_DATA_VALUES
            // case GET_DATA_DIRECTORY
            // case GET_DATA_DEFINITION

            // 8.5 Dataset Services
            // case GET_DATA_SET_VALUES
            // case SET_DATA_SET_VALUES
            // case CREATE_DATA_SET
            // case DELETE_DATA_SET
            // case GET_DATA_SET_DIRECTORY

            // 8.6 Static Value Services
            // case SELECT_ACTIVE_SG
            // case SELECT_EDIT_SG
            // case SET_EDIT_SG_VALUE
            // case CONFIRM_EDIT_SG_VALUES
            // case GET_EDIT_SG_VALUE
            // case GET_SGCBVALUES

            // 8.7 Report Services
            // case REPORT
            // case GET_BRCBVALUES
            // case SET_BRCBVALUES
            // case GET_URCBVALUES
            // case SET_URCBVALUES

            // 8.8 Log Services
            // case GET_LCBVALUES
            // case SET_LCBVALUES
            // case QUERY_LOG_BY_TIME
            // case QUERY_LOG_AFTER
            // case GET_LOG_STATUS_VALUES

            // 8.9 General Substation Event Services
            // case Send_GOOSE_Message
            // case Get_Go_Reference
            // case Get_GOOSE_ElementNumber
            // case GET_GOCBVALUES
            // case SET_GOCBVALUES

            // 8.10 SV Services
            // case Send_MSVMessage
            // case GET_MSVCBVALUES
            // case SET_MSVCBVALUES

            // 8.11 Control Services
            // case SELECT
            // case SELECT_WITH_VALUE
            // case OPERATE
            // case CANCEL
            // case COMMAND_TERMINATION
            // case TIME_ACTIVATED_OPERATE
            // case TIME_ACTIVATED_OPERATE_TERMINATION

            // 8.12 File Services
            // case GET_FILE
            // case SET_FILE
            // case DELETE_FILE
            // case GET_FILE_ATTRIBUTEVALUES
            // case GET_FILE_DIRECTORY

            // 8.13 RPC Services
            // case GET_RPC_INTERFACE_DIRECTORY
            // case GET_RPC_METHOD_DIRECTORY
            // case GET_RPC_INTERFACE_DEFINITION
            // case GET_RPC_METHOD_DEFINITION
            // case RPC_CALL

            // 8.14 Test Services
            case TEST: return new CmsTest();

            // 8.15 Association Negotiation
            // case ASSOCIATE_NEGOTIATE

            default:
                throw new IllegalArgumentException("Unknown service code: " + serviceName);
        }
    }

    private AsduFactory() {
    }
}