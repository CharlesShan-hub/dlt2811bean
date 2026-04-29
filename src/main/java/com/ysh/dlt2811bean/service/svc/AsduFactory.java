package com.ysh.dlt2811bean.service.svc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetAllDataValues;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalNodeDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;

public class AsduFactory {

    public static CmsAsdu<?> create(ServiceCode serviceCode, boolean isResp, boolean isErr) {
        switch (serviceCode) {
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
            default:
                throw new IllegalArgumentException("Unknown service code: " + serviceCode);
        }
    }

    private AsduFactory() {
    }
}