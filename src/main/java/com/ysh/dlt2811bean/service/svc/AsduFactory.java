package com.ysh.dlt2811bean.service.svc;

import com.ysh.dlt2811bean.service.protocol.enums.ServiceName;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.*;
import com.ysh.dlt2811bean.service.svc.data.*;
import com.ysh.dlt2811bean.service.svc.negotiation.*;
import com.ysh.dlt2811bean.service.svc.dataset.*;
import com.ysh.dlt2811bean.service.svc.directory.*;
import com.ysh.dlt2811bean.service.svc.report.*;
import com.ysh.dlt2811bean.service.svc.setting.*;
import com.ysh.dlt2811bean.service.svc.goose.*;
import com.ysh.dlt2811bean.service.svc.control.*;
import com.ysh.dlt2811bean.service.svc.sv.*;
import com.ysh.dlt2811bean.service.svc.test.*;
import com.ysh.dlt2811bean.service.svc.file.*;
import com.ysh.dlt2811bean.service.svc.rpc.*;

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
            case GET_GOCBVALUES: return new CmsGetGoCBValues(isResp, isErr);
            case SET_GOCBVALUES: return new CmsSetGoCBValues(isResp, isErr);

            // 8.10 SV Services
            // case Send_MSVMessage
            case GET_MSVCBVALUES: return new CmsGetMSVCBValues(isResp, isErr);
            case SET_MSVCBVALUES: return new CmsSetMSVCBValues(isResp, isErr);

            // 8.11 Control Services
            case SELECT: return new CmsSelect(isResp, isErr);   
            case SELECT_WITH_VALUE: return new CmsSelectWithValue(isResp, isErr);
            case OPERATE: return new CmsOperate(isResp, isErr);
            case CANCEL: return new CmsCancel(isResp, isErr);
            case COMMAND_TERMINATION: return new CmsCommandTermination(isResp, isErr);
            case TIME_ACTIVATED_OPERATE: return new CmsTimeActivatedOperate(isResp, isErr);
            case TIME_ACTIVATED_OPERATE_TERMINATION: return new CmsTimeActivatedOperateTermination(isResp, isErr);

            // 8.12 File Services
            case GET_FILE: return new CmsGetFile(isResp, isErr);
            case SET_FILE: return new CmsSetFile(isResp, isErr);
            case DELETE_FILE: return new CmsDeleteFile(isResp, isErr);
            case GET_FILE_ATTRIBUTEVALUES: return new CmsGetFileAttributeValues(isResp, isErr);
            case GET_FILE_DIRECTORY: return new CmsGetFileDirectory(isResp, isErr);

            // 8.13 RPC Services
            case GET_RPC_INTERFACE_DIRECTORY: return new CmsGetRpcInterfaceDirectory(isResp, isErr);
            case GET_RPC_METHOD_DIRECTORY: return new CmsGetRpcMethodDirectory(isResp, isErr);
            case GET_RPC_INTERFACE_DEFINITION: return new CmsGetRpcInterfaceDefinition(isResp, isErr);
            case GET_RPC_METHOD_DEFINITION: return new CmsGetRpcMethodDefinition(isResp, isErr);
            case RPC_CALL: return new CmsRpcCall(isResp, isErr);

            // 8.14 Test Services
            case TEST: return new CmsTest();

            // 8.15 Association Negotiation
            case ASSOCIATE_NEGOTIATE: return new CmsAssociateNegotiate(isResp, isErr);

            default:
                throw new IllegalArgumentException("Unknown service code: " + serviceName);
        }
    }

    private AsduFactory() {
    }
}