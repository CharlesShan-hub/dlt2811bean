package com.ysh.dlt2811bean.service.svc;

import com.ysh.dlt2811bean.service.protocol.enums.MessageType;
import com.ysh.dlt2811bean.service.protocol.enums.ServiceCode;
import com.ysh.dlt2811bean.service.protocol.types.CmsAsdu;
import com.ysh.dlt2811bean.service.svc.association.CmsAbort;
import com.ysh.dlt2811bean.service.svc.association.CmsAssociate;
import com.ysh.dlt2811bean.service.svc.association.CmsRelease;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetLogicalDeviceDirectory;
import com.ysh.dlt2811bean.service.svc.directory.CmsGetServerDirectory;

public class AsduFactory {

    public static CmsAsdu<?> create(ServiceCode serviceCode, boolean isResp, boolean isErr) {
        switch (serviceCode) {
            case ASSOCIATE:
                return new CmsAssociate(isResp, isErr);
            case ABORT:
                return new CmsAbort(isResp, isErr);
            case RELEASE:
                return new CmsRelease(isResp, isErr);
            case GET_SERVER_DIRECTORY:
                return new CmsGetServerDirectory(isResp, isErr);
            case GET_LOGIC_DEVICE_DIRECTORY:
                return new CmsGetLogicalDeviceDirectory(isResp, isErr);
            default:
                throw new IllegalArgumentException("Unknown service code: " + serviceCode);
        }
    }

    private AsduFactory() {
    }
}