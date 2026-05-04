package com.ysh.dlt2811bean.service.svc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDefinition;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataDirectory;
import com.ysh.dlt2811bean.service.svc.data.CmsGetDataValues;
import com.ysh.dlt2811bean.service.svc.data.CmsSetDataValues;
import com.ysh.dlt2811bean.service.svc.dataset.*;
import com.ysh.dlt2811bean.service.svc.directory.*;
import com.ysh.dlt2811bean.service.svc.report.*;
import com.ysh.dlt2811bean.service.svc.setting.*;
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
            case GET_ALL_DATA_DEFINITION: return new CmsGetAllDataDefinition(isResp, isErr);
            case GET_ALL_CB_VALUES: return new CmsGetAllCBValues(isResp, isErr);

            // 8.4 Data Services
            case GET_DATA_VALUES: return new CmsGetDataValues(isResp, isErr);
            case SET_DATA_VALUES: return new CmsSetDataValues(isResp, isErr);
            case GET_DATA_DIRECTORY: return new CmsGetDataDirectory(isResp, isErr);
            case GET_DATA_DEFINITION: return new CmsGetDataDefinition(isResp, isErr);

            // 8.5 Dataset Services
            case GET_DATA_SET_VALUES: return new CmsGetDataSetValues(isResp, isErr);
            case SET_DATA_SET_VALUES: return new CmsSetDataSetValues(isResp, isErr);
            case CREATE_DATA_SET: return new CmsCreateDataSet(isResp, isErr);
            case DELETE_DATA_SET: return new CmsDeleteDataSet(isResp, isErr);
             case GET_DATA_SET_DIRECTORY: return new CmsGetDataSetDirectory(isResp, isErr);

            // 8.6 Static Value Services
            case SELECT_ACTIVE_SG: return new CmsSelectActiveSG(isResp, isErr);
            case SELECT_EDIT_SG: return new CmsSelectEditSG(isResp, isErr);
            case SET_EDIT_SG_VALUE: return new CmsSetEditSGValue(isResp, isErr);
            case CONFIRM_EDIT_SG_VALUES: return new CmsConfirmEditSGValues(isResp, isErr);
            case GET_EDIT_SG_VALUE: return new CmsGetEditSGValue(isResp, isErr);
            case GET_SGCBVALUES: return new CmsGetSGCBValues(isResp, isErr);

            // 8.7 Report Services
            case REPORT: return new CmsReport(isResp, isErr);
            case GET_BRCBVALUES: return new CmsGetBRCBValues(isResp, isErr);
            case SET_BRCBVALUES: return new CmsSetBRCBValues(isResp, isErr);
            case GET_URCBVALUES: return new CmsGetURCBValues(isResp, isErr);
            case SET_URCBVALUES: return new CmsSetURCBValues(isResp, isErr);

            // 8.8 Log Services
            case GET_LCBVALUES: return new CmsGetLCBValues(isResp, isErr);
            case SET_LCBVALUES: return new CmsSetLCBValues(isResp, isErr);
            case QUERY_LOG_BY_TIME: return new CmsQueryLogByTime(isResp,isErr);
            case QUERY_LOG_AFTER: return new CmsQueryLogAfter(isResp, isErr);
            case GET_LOG_STATUS_VALUES: return new CmsGetLogStatusValues(isResp, isErr);

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